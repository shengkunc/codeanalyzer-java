/*
Copyright IBM Corporation 2023, 2024

Licensed under the Apache Public License 2.0, Version 2.0 (the "License");
you may not use this file except in compliance with the License.

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.ibm.cldk;

import com.ibm.cldk.entities.*;
import com.ibm.cldk.utils.AnalysisUtils;
import com.ibm.cldk.utils.Log;
import com.ibm.cldk.utils.ScopeUtils;
import com.ibm.wala.cast.ir.ssa.AstIRFactory;
import com.ibm.wala.cast.java.translator.jdt.ecj.ECJClassLoaderFactory;
import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.classLoader.JavaLanguage;
import com.ibm.wala.classLoader.Language;
import com.ibm.wala.ipa.callgraph.*;
import com.ibm.wala.ipa.callgraph.AnalysisOptions.ReflectionOptions;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.ClassHierarchyFactory;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.ipa.modref.ModRef;
import com.ibm.wala.ipa.slicer.MethodEntryStatement;
import com.ibm.wala.ipa.slicer.SDG;
import com.ibm.wala.ipa.slicer.Slicer;
import com.ibm.wala.ipa.slicer.Statement;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.util.collections.HashMapFactory;
import com.ibm.wala.util.graph.Graph;
import com.ibm.wala.util.graph.GraphSlicer;
import com.ibm.wala.util.graph.traverse.DFS;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.io.output.NullOutputStream;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.nio.json.JSONExporter;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.ibm.cldk.CodeAnalyzer.analysisLevel;
import static com.ibm.cldk.utils.AnalysisUtils.*;


@Data
abstract class Dependency {
    public CallableVertex source;
    public CallableVertex target;
}

@Data
@EqualsAndHashCode(callSuper = true)
class SDGDependency extends Dependency {
    public String sourceKind;
    public String destinationKind;
    public String type;
    public String weight;

    public SDGDependency(CallableVertex source, CallableVertex target, SystemDepEdge edge) {
        super.source = source;
        super.target = target;
        this.sourceKind = edge.getSourceKind();
        this.destinationKind = edge.getDestinationKind();
        this.type = edge.getType();
        this.weight = String.valueOf(edge.getWeight());
    }
}

@Data
@EqualsAndHashCode(callSuper = true)
class CallDependency extends Dependency {
    public String type;
    public String weight;

    public CallDependency(CallableVertex source, CallableVertex target, AbstractGraphEdge edge) {
        this.source = source;
        this.target = target;
        this.type = edge.toString();
        this.weight = String.valueOf(edge.getWeight());
    }
}

/**
 * The type Sdg 2 json.
 */
public class SystemDependencyGraph {

    /**
     * Get a JGraphT graph exporter to save graph as JSON.
     *
     * @return the graph exporter
     */

    private static JSONExporter<CallableVertex, AbstractGraphEdge> getGraphExporter() {
        JSONExporter<CallableVertex, AbstractGraphEdge> exporter = new JSONExporter<>();
        exporter.setEdgeAttributeProvider(AbstractGraphEdge::getAttributes);
        exporter.setVertexAttributeProvider(CallableVertex::getAttributes);
        return exporter;
    }

    /**
     * Convert SDG to a formal Graph representation.
     *
     * @param callGraph
     * @return
     */
    private static org.jgrapht.Graph<CallableVertex, AbstractGraphEdge> buildOnlyCallGraph(CallGraph callGraph) {

        org.jgrapht.Graph<CallableVertex, AbstractGraphEdge> graph = new DefaultDirectedGraph<>(
                AbstractGraphEdge.class);
        callGraph.getEntrypointNodes()
                .forEach(p -> {
                    // Get call statements that may execute in a given method
                    Iterator<CallSiteReference> outGoingCalls = p.iterateCallSites();
                    outGoingCalls.forEachRemaining(n -> {
                        callGraph.getPossibleTargets(p, n).stream()
                                .filter(o -> AnalysisUtils.isApplicationClass(o.getMethod().getDeclaringClass()))
                                .forEach(o -> {

                                    // Add the source nodes to the graph as vertices
                                    Map<String, String> source = Optional.ofNullable(getCallableFromSymbolTable(p.getMethod())).orElseGet(() -> createAndPutNewCallableInSymbolTable(p.getMethod()));
                                    CallableVertex source_vertex = new CallableVertex(source);

                                    // Add the target nodes to the graph as vertices
                                    Map<String, String> target = Optional.ofNullable(getCallableFromSymbolTable(o.getMethod())).orElseGet(() -> createAndPutNewCallableInSymbolTable(o.getMethod()));
                                    CallableVertex target_vertex = new CallableVertex(target);

                                    if (!source.equals(target) && target != null) {
                                        // Get the edge between the source and the target
                                        graph.addVertex(source_vertex);
                                        graph.addVertex(target_vertex);
                                        AbstractGraphEdge cgEdge = graph.getEdge(source_vertex, target_vertex);
                                        if (cgEdge instanceof CallEdge) {
                                            ((CallEdge) cgEdge).incrementWeight();
                                        } else {
                                            graph.addEdge(source_vertex, target_vertex, new CallEdge());
                                        }
                                    }
                                });
                    });
                });

        return graph;
    }

    /**
     * Construct a System Dependency Graph from a given input.
     *
     * @param input        the input
     * @param dependencies the dependencies
     * @param build        The build options
     * @return A List of triples containing the source, destination, and edge type
     * @throws IOException                     the io exception
     * @throws ClassHierarchyException         the class hierarchy exception
     * @throws IllegalArgumentException        the illegal argument exception
     * @throws CallGraphBuilderCancelException the call graph builder cancel
     *                                         exception
     */
    public static List<Dependency> construct(
            String input, String dependencies, String build)
            throws IOException, ClassHierarchyException, IllegalArgumentException, CallGraphBuilderCancelException {

        // Initialize scope
        AnalysisScope scope = ScopeUtils.createScope(input, dependencies, build);
        IClassHierarchy cha = ClassHierarchyFactory.make(scope,
                new ECJClassLoaderFactory(scope.getExclusions()));
        Log.done("There were a total of " + cha.getNumberOfClasses() + " classes of which "
                + AnalysisUtils.getNumberOfApplicationClasses(cha) + " are application classes.");

        // Initialize javaee options
        AnalysisOptions options = new AnalysisOptions();
        Iterable<Entrypoint> entryPoints = AnalysisUtils.getEntryPoints(cha);
        options.setEntrypoints(entryPoints);
        options.getSSAOptions().setDefaultValues(com.ibm.wala.ssa.SymbolTable::getDefaultValue);
        options.setReflectionOptions(ReflectionOptions.NONE);
        IAnalysisCacheView cache = new AnalysisCacheImpl(AstIRFactory.makeDefaultFactory(),
                options.getSSAOptions());

        // Build call graph
        Log.info("Building call graph.");

        // Some fu to remove WALA's console out...
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        long start_time = System.currentTimeMillis();
        CallGraph callGraph;
        CallGraphBuilder<InstanceKey> builder;
        try {
            System.setOut(new PrintStream(NullOutputStream.INSTANCE));
            System.setErr(new PrintStream(NullOutputStream.INSTANCE));
            builder = Util.makeRTABuilder(options, cache, cha);
            callGraph = builder.makeCallGraph(options, null);
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        Log.done("Finished construction of call graph. Took "
                + Math.ceil((double) (System.currentTimeMillis() - start_time) / 1000) + " seconds.");

        // set cyclomatic complexity for callables in the symbol table
        callGraph.forEach(cgNode -> {
            Callable callable = getCallableObjectFromSymbolTable(cgNode.getMethod()).getRight();
            if (callable != null) {
                callable.setCyclomaticComplexity(getCyclomaticComplexity(cgNode.getIR()));
            }
        });

        org.jgrapht.Graph<CallableVertex, AbstractGraphEdge> graph;

        graph = buildOnlyCallGraph(callGraph);

        List<Dependency> edges = graph.edgeSet().stream()
                .map(abstractGraphEdge -> {
                    CallableVertex source = graph.getEdgeSource(abstractGraphEdge);
                    CallableVertex target = graph.getEdgeTarget(abstractGraphEdge);
                    if (abstractGraphEdge instanceof CallEdge) {
                        return new CallDependency(source, target, abstractGraphEdge);
                    } else {
                        return new SDGDependency(source, target, (SystemDepEdge) abstractGraphEdge);
                    }
                })
                .collect(Collectors.toList());

        return edges;
    }
}