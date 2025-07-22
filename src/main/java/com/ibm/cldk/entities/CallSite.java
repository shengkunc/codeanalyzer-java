package com.ibm.cldk.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Data;

/**
 * Represents a call site within source code, encapsulating information about method invocations
 * and their contextual details.
 *
 * <p>
 * A call site contains information about the method being called, its receiver,
 * arguments, return type, and various properties that characterize the method call.
 * It also tracks the position of the call site within the source file.
 * </p>
 *
 * <p>
 * This class leverages Lombok's {@code @Data} annotation to automatically generate
 * getters, setters, {@code toString()}, {@code equals()}, and {@code hashCode()} methods.
 * </p>
 *
 * @author Rahul Krishna
 * @version 2.3.0
 */
@Data
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class CallSite {
    /** Name of the method being called */
    private String methodName;

    /** Comment associated with the call site */
    private Comment comment;

    /** Expression representing the receiver of the method call */
    private String receiverExpr;

    /** Type of the receiver object */
    private String receiverType;

    /** List of argument types for the method call */
    private List<String> argumentTypes;

    /** List of argument expressions for the method call */
    private List<String> argumentExpr;

    /** Return type of the called method */
    private String returnType;

    /** Full signature of the callee method */
    private String calleeSignature;

    /** Flag indicating if the method has public access */
    private boolean isPublic = false;

    /** Flag indicating if the method has protected access */
    private boolean isProtected = false;

    /** Flag indicating if the method has private access */
    private boolean isPrivate = false;

    /** Flag indicating if the method has unspecified access */
    private boolean isUnspecified = false;

    /** Flag indicating if this is a static method call */
    private boolean isStaticCall;

    /** Flag indicating if this is a constructor call */
    private boolean isConstructorCall;

    /** CRUD operation associated with this call site, if any */
    private CRUDOperation crudOperation = null;

    /** CRUD query associated with this call site, if any */
    private CRUDQuery crudQuery = null;

    /** Starting line number of the call site in the source file */
    private int startLine;

    /** Starting column number of the call site in the source file */
    private int startColumn;

    /** Ending line number of the call site in the source file */
    private int endLine;

    /** Ending column number of the call site in the source file */
    private int endColumn;
}
