package com.ibm.cldk.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Represents a callable entity in the source code, such as a method or constructor.
 *
 * <p>
 * This class encapsulates information about the callable's file path, signature, comments,
 * annotations, modifiers, thrown exceptions, declaration, parameters, code, position within
 * the source file, return type, and various properties that characterize the callable.
 * </p>
 *
 * <p>
 * This class leverages Lombok's {@code @Data} annotation to automatically generate
 * getters, setters, {@code toString()}, {@code equals()}, and {@code hashCode()} methods.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>
 *     Callable callable = new Callable();
 *     callable.setFilePath("src/main/java/com/ibm/cldk/entities/Example.java");
 *     callable.setSignature("public void exampleMethod()");
 *     callable.setStartLine(10);
 *     callable.setEndLine(20);
 *     callable.setReturnType("void");
 *     callable.setConstructor(false);
 * </pre>
 * </p>
 *
 * @author Rahul Krishna
 * @version 2.3.0
 */
@Data
public class Callable {
    /** The file path where the callable entity is defined. */
    private String filePath;

    /** The signature of the callable entity. */
    private String signature;

    /** A list of comments associated with the callable entity. */
    private List<Comment> comments;

    /** A list of annotations applied to the callable entity. */
    private List<String> annotations;

    /** A list of modifiers applied to the callable entity (e.g., public, private). */
    private List<String> modifiers;

    /** A list of exceptions thrown by the callable entity. */
    private List<String> thrownExceptions;

    /** The declaration of the callable entity. */
    private String declaration;

    /** A list of parameters for the callable entity. */
    private List<ParameterInCallable> parameters;

    /** The code of the callable entity. */
    private String code;

    /** The starting line number of the callable entity in the source file. */
    private int startLine;

    /** The ending line number of the callable entity in the source file. */
    private int endLine;

    /** The starting line number of the callable code in the source file. */
    private int codeStartLine;

    /** The return type of the callable entity. */
    private String returnType = null;

    /** Indicates whether the callable entity is implicit. */
    private boolean isImplicit = false;

    /** Indicates whether the callable entity is a constructor. */
    private boolean isConstructor = false;

    /** A list of types referenced by the callable entity. */
    private List<String> referencedTypes;

    /** A list of fields accessed by the callable entity. */
    private List<String> accessedFields;

    /** A list of call sites within the callable entity. */
    private List<CallSite> callSites;

    /** A list of variable declarations within the callable entity. */
    private List<VariableDeclaration> variableDeclarations;

    /** A list of CRUD operations associated with the callable entity. */
    private List<CRUDOperation> crudOperations = new ArrayList<>();

    /** A list of CRUD queries associated with the callable entity. */
    private List<CRUDQuery> crudQueries = new ArrayList<>();

    /** The cyclomatic complexity of the callable entity. */
    private int cyclomaticComplexity;

    /** Indicates whether the callable entity is an entry point. */
    private boolean isEntrypoint = false;
}
