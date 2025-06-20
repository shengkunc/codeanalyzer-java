package com.ibm.cldk;

import com.ibm.cldk.entities.Callable;
import com.ibm.cldk.entities.JavaCompilationUnit;
import com.ibm.cldk.entities.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

public class SymbolTableTest {

    private String getJavaCodeForTestResource(String resourcePath) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        assert inputStream != null;
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    @Test
    public void testExtractSingleGenricsDuplicateSignature() throws IOException {
        String javaCode = getJavaCodeForTestResource("test-applications/generics-varargs-duplicate-signature-test/Validate.java");
        Map<String, JavaCompilationUnit> symbolTable = SymbolTable.extractSingle(javaCode).getLeft();
        Assertions.assertEquals(1, symbolTable.size());
        Map<String, Type> typeDeclaration = symbolTable.values().iterator().next().getTypeDeclarations();
        Assertions.assertEquals(1, typeDeclaration.size());
        Map<String, Callable> callables = typeDeclaration.values().iterator().next().getCallableDeclarations();
        Assertions.assertEquals(17, callables.size());
    }

}
