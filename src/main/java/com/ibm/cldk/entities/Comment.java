package com.ibm.cldk.entities;

import lombok.Data;

/**
 * Represents a comment entity extracted from source code.
 * This class encapsulates information about the content, position,
 * and type of a comment within a source file.
 *
 * <p>
 * The comment can be of various types, including Javadoc, block comments, or line comments.
 * The class also keeps track of the comment's position within the file (line and column numbers).
 * </p>
 *
 * <p>
 * This class leverages Lombok's {@code @Data} annotation to automatically generate
 * getters, setters, {@code toString()}, {@code equals()}, and {@code hashCode()} methods.
 * </p>
 *
 * Example usage:
 * <pre>
 *     Comment comment = new Comment();
 *     comment.setContent("This is a sample comment.");
 *     comment.setStartLine(10);
 *     comment.setEndLine(12);
 *     comment.setJavadoc(true);
 * </pre>
 *
 * @author Rahul Krishna
 * @version 2.3.0
 */
@Data
public class Comment {

    /**
     * The textual content of the comment.
     */
    private String content;

    /**
     * The starting line number of the comment in the source file.
     * <p>
     * Defaults to {@code -1} if the position is unknown.
     * </p>
     */
    private int startLine = -1;

    /**
     * The ending line number of the comment in the source file.
     * <p>
     * Defaults to {@code -1} if the position is unknown.
     * </p>
     */
    private int endLine = -1;

    /**
     * The starting column number of the comment in the source file.
     * <p>
     * Defaults to {@code -1} if the position is unknown.
     * </p>
     */
    private int startColumn = -1;

    /**
     * The ending column number of the comment in the source file.
     * <p>
     * Defaults to {@code -1} if the position is unknown.
     * </p>
     */
    private int endColumn = -1;

    /**
     * Indicates whether the comment is a Javadoc comment.
     * <p>
     * Javadoc comments are special block comments used for generating documentation
     * and typically start with {@code /**}.
     * </p>
     */
    private boolean isJavadoc = false;
}
