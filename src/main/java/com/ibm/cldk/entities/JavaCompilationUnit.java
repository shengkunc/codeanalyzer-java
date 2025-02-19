package com.ibm.cldk.entities;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class JavaCompilationUnit {
    private String filePath;
    private String packageName;
    private List<Comment> comments;
    private List<String> imports;
    private Map<String, Type> typeDeclarations;
    private boolean isModified;
}
