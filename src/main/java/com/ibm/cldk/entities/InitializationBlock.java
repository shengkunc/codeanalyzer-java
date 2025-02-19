package com.ibm.cldk.entities;

import lombok.Data;

import java.util.List;
import java.util.stream.Collector;

@Data
public class InitializationBlock {
    private String filePath;
    private List<Comment> comments;
    private List<String> annotations;
    private List<String> thrownExceptions;
    private String code;
    private int startLine;
    private int endLine;
    private boolean isStatic;
    private List<String> referencedTypes;
    private List<String> accessedFields;
    private List<CallSite> callSites;
    private List<VariableDeclaration> variableDeclarations;
    private int cyclomaticComplexity;

}
