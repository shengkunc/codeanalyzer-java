package com.ibm.cldk.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Represents a component of a record in the source code.
 *
 * <p>
 * This class encapsulates information about the component's name, type, modifiers,
 * annotations, default value, and whether it is a varargs parameter.
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
 *     RecordComponent component = new RecordComponent();
 *     component.setName("exampleComponent");
 *     component.setType("String");
 *     component.setModifiers(Arrays.asList("private"));
 *     component.setAnnotations(Arrays.asList("NotNull"));
 *     component.setDefaultValue("defaultValue");
 *     component.setVarArgs(false);
 * </pre>
 * </p>
 *
 * @author Rahul Krishna
 * @version 2.3.0
 */
@Data
public class RecordComponent {
    /** The comment associated with the record component. */
    private Comment comment;

    /** The name of the record component. */
    private String name;

    /** The type of the record component. */
    private String type;

    /** A list of modifiers applied to the record component (e.g., final, static). */
    private List<String> modifiers;

    /** A list of annotations applied to the record component. */
    private List<String> annotations = new ArrayList<>();

    /** The default value of the record component, stored as a string representation. */
    private Object defaultValue = null;

    /** Indicates whether the record component is a varargs parameter. */
    private boolean isVarArgs = false;
}
