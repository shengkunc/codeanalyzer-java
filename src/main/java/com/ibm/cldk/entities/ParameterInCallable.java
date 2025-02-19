package com.ibm.cldk.entities;

import lombok.Data;

import java.util.List;

/**
 * Represents a parameter in a callable entity (e.g., method or constructor).
 *
 * <p>
 * This class encapsulates information about the parameter's type, name, annotations,
 * modifiers, and its position within the source file.
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
 *     ParameterInCallable param = new ParameterInCallable();
 *     param.setType("String");
 *     param.setName("exampleParam");
 *     param.setAnnotations(Arrays.asList("NotNull"));
 *     param.setModifiers(Arrays.asList("final"));
 *     param.setStartLine(10);
 *     param.setEndLine(10);
 *     param.setStartColumn(5);
 *     param.setEndColumn(20);
 * </pre>
 * </p>
 *
 * @author Rahul Krishna
 * @version 2.3.0
 */
@Data
public class ParameterInCallable {
    /** The type of the parameter (e.g., int, String). */
    private String type;

    /** The name of the parameter. */
    private String name;

    /** A list of annotations applied to the parameter. */
    private List<String> annotations;

    /** A list of modifiers applied to the parameter (e.g., final, static). */
    private List<String> modifiers;

    /** The starting line number of the parameter in the source file. */
    private int startLine;

    /** The ending line number of the parameter in the source file. */
    private int endLine;

    /** The starting column number of the parameter in the source file. */
    private int startColumn;

    /** The ending column number of the parameter in the source file. */
    private int endColumn;
}