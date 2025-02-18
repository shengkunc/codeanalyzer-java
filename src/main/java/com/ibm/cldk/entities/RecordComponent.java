package com.ibm.cldk.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class RecordComponent {
    private String comment;
    private String name;
    private String type;
    private List<String> modifiers;
    private List<String> annotations = new ArrayList<>();
    private Object defaultValue = null; // We will store the string representation of the default value
    private boolean isVarArgs = false;
}
