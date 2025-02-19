package com.ibm.cldk.entities;

import lombok.Data;

/**
 * Represents a variable declaration in the source code.
 *
 * <p>
 * This class encapsulates information about the variable's name, type, initializer,
 * and its position within the source file. It also includes an optional comment
 * associated with the variable declaration.
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
 *     VariableDeclaration varDecl = new VariableDeclaration();
 *     varDecl.setName("exampleVar");
 *     varDecl.setType("String");
 *     varDecl.setInitializer("\"defaultValue\"");
 *     varDecl.setStartLine(10);
 *     varDecl.setEndLine(10);
 *     varDecl.setStartColumn(5);
 *     varDecl.setEndColumn(20);
 * </pre>
 * </p>
 *
 * @author Rahul Krishna
 * @version 2.3.0
 */
@Data
public class VariableDeclaration {
    /** The comment associated with the variable declaration. */
    private Comment comment;

    /** The name of the variable. */
    private String name;

    /** The type of the variable. */
    private String type;

    /** The initializer of the variable, stored as a string representation. */
    private String initializer;

    /** The starting line number of the variable declaration in the source file. */
    private int startLine = -1;

    /** The starting column number of the variable declaration in the source file. */
    private int startColumn = -1;

    /** The ending line number of the variable declaration in the source file. */
    private int endLine = -1;

    /** The ending column number of the variable declaration in the source file. */
    private int endColumn = -1;
}
