package com.ibm.cldk.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a type in the system with various characteristics.
 * This class uses Lombok's @Data annotation to generate boilerplate code.
 *
 * @author Rahul Krishna
 * @version 2.3.0
 */
@Data
public class Type {
    /** Indicates if this type is nested. */
    private boolean isNestedType;

    /** Indicates if this type is a class or interface declaration. */
    private boolean isClassOrInterfaceDeclaration;

    /** Indicates if this type is an enum declaration. */
    private boolean isEnumDeclaration;

    /** Indicates if this type is an annotation declaration. */
    private boolean isAnnotationDeclaration;

    /** Indicates if this type is a record declaration. */
    private boolean isRecordDeclaration;

    /** Indicates if this type is an interface. */
    private boolean isInterface;

    /** Indicates if this type is an inner class. */
    private boolean isInnerClass;

    /** Indicates if this type is a local class. */
    private boolean isLocalClass;

    /** List of types that this type extends. */
    private List<String> extendsList = new ArrayList<>();

    /** List of comments associated with this type. */
    private List<Comment> comments;

    /** List of interfaces that this type implements. */
    private List<String> implementsList = new ArrayList<>();

    /** List of modifiers for this type. */
    private List<String> modifiers = new ArrayList<>();

    /** List of annotations for this type. */
    private List<String> annotations = new ArrayList<>();

    /** The parent type of this type. */
    private String parentType;

    /** List of nested type declarations within this type. */
    private List<String> nestedTypeDeclarations = new ArrayList<>();

    /** Map of callable declarations within this type. */
    private Map<String, Callable> callableDeclarations = new HashMap<>();

    /** List of field declarations within this type. */
    private List<Field> fieldDeclarations = new ArrayList<>();

    /** List of enum constants within this type. */
    private List<EnumConstant> enumConstants = new ArrayList<>();

    /** List of record components within this type. */
    private List<RecordComponent> recordComponents = new ArrayList<>();

    /** List of initialization blocks within this type. */
    private List<InitializationBlock> initializationBlocks = new ArrayList<>();

    /** Indicates if this type is an entry point class. */
    private boolean isEntrypointClass = false;
}