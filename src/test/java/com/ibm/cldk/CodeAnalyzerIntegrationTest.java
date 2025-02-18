package com.ibm.cldk;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.stream.StreamSupport;


@Testcontainers
@SuppressWarnings("resource")
public class CodeAnalyzerIntegrationTest {

    /**
     * Creates a Java 11 test container that mounts the build/libs folder.
     */
    static String codeanalyzerVersion;
    static final String javaVersion = "17";
    static String javaHomePath;
    static {
        // Build project first
        try {
            Process process = new ProcessBuilder("./gradlew", "fatJar")
                    .directory(new File(System.getProperty("user.dir")))
                    .start();
            if (process.waitFor() != 0) {
                throw new RuntimeException("Build failed");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to build codeanalyzer", e);
        }
    }

    @Container
    static final GenericContainer<?> container = new GenericContainer<>("ubuntu:latest")
            .withCreateContainerCmdModifier(cmd -> cmd.withEntrypoint("sh"))
            .withCommand("-c", "while true; do sleep 1; done")
            .withCopyFileToContainer(MountableFile.forHostPath(Paths.get(System.getProperty("user.dir")).resolve("build/libs")), "/opt/jars")
            .withCopyFileToContainer(MountableFile.forHostPath(Paths.get(System.getProperty("user.dir")).resolve("build/libs")), "/opt/jars")
            .withCopyFileToContainer(MountableFile.forHostPath(Paths.get(System.getProperty("user.dir")).resolve("src/test/resources/test-applications/mvnw-corrupt-test")), "/test-applications/mvnw-corrupt-test")
            .withCopyFileToContainer(MountableFile.forHostPath(Paths.get(System.getProperty("user.dir")).resolve("src/test/resources/test-applications/plantsbywebsphere")), "/test-applications/plantsbywebsphere")
            .withCopyFileToContainer(MountableFile.forHostPath(Paths.get(System.getProperty("user.dir")).resolve("src/test/resources/test-applications/call-graph-test")), "/test-applications/call-graph-test")
            .withCopyFileToContainer(MountableFile.forHostPath(Paths.get(System.getProperty("user.dir")).resolve("src/test/resources/test-applications/record-class-test")), "/test-applications/record-class-test")
            .withCopyFileToContainer(MountableFile.forHostPath(Paths.get(System.getProperty("user.dir")).resolve("src/test/resources/test-applications/mvnw-working-test")), "/test-applications/mvnw-working-test");

    @Container
    static final GenericContainer<?> mavenContainer = new GenericContainer<>("maven:3.8.3-openjdk-17")
            .withCreateContainerCmdModifier(cmd -> cmd.withEntrypoint("sh"))
            .withCommand("-c", "while true; do sleep 1; done")
            .withCopyFileToContainer(MountableFile.forHostPath(Paths.get(System.getProperty("user.dir")).resolve("build/libs")), "/opt/jars")
            .withCopyFileToContainer(MountableFile.forHostPath(Paths.get(System.getProperty("user.dir")).resolve("src/test/resources/test-applications/mvnw-corrupt-test")), "/test-applications/mvnw-corrupt-test")
            .withCopyFileToContainer(MountableFile.forHostPath(Paths.get(System.getProperty("user.dir")).resolve("src/test/resources/test-applications/mvnw-working-test")), "/test-applications/mvnw-working-test")
            .withCopyFileToContainer(MountableFile.forHostPath(Paths.get(System.getProperty("user.dir")).resolve("src/test/resources/test-applications/daytrader8")), "/test-applications/daytrader8");

    public CodeAnalyzerIntegrationTest() throws IOException, InterruptedException {
    }

    @BeforeAll
    static void setUp() {
        // Install Java 17 in the base container
        try {
            container.execInContainer("apt-get", "update");
            container.execInContainer("apt-get", "install", "-y", "openjdk-17-jdk");

            // Get JAVA_HOME dynamically
            var javaHomeResult = container.execInContainer("bash", "-c",
                    "dirname $(dirname $(readlink -f $(which java)))"
            );
            javaHomePath = javaHomeResult.getStdout().trim();
            Assertions.assertFalse(javaHomePath.isEmpty(), "Failed to determine JAVA_HOME");

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


        // Get the version of the codeanalyzer jar
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(
                Paths.get(System.getProperty("user.dir"), "gradle.properties").toFile())) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        codeanalyzerVersion = properties.getProperty("version");
    }

    @Test
    void shouldHaveCorrectJavaVersionInstalled() throws Exception {
        var baseContainerresult = container.execInContainer("java", "-version");
        var mvnContainerresult = mavenContainer.execInContainer("java", "-version");
        Assertions.assertTrue(baseContainerresult.getStderr().contains("openjdk version \"" + javaVersion), "Base container Java version should be " + javaVersion);
        Assertions.assertTrue(mvnContainerresult.getStderr().contains("openjdk version \"" + javaVersion), "Maven container Java version should be " + javaVersion);
    }

    @Test
    void shouldHaveCodeAnalyzerJar() throws Exception {
        var dirContents = container.execInContainer("ls", "/opt/jars/");
        Assertions.assertTrue(dirContents.getStdout().length() > 0, "Directory listing should not be empty");
        Assertions.assertTrue(dirContents.getStdout().contains("codeanalyzer"), "Codeanalyzer.jar not found in the container.");
    }

    @Test
    void shouldBeAbleToRunCodeAnalyzer() throws Exception {
        var runCodeAnalyzerJar = container.execInContainer(
                "bash", "-c",
                String.format("export JAVA_HOME=%s && java -jar /opt/jars/codeanalyzer-%s.jar --help",
                javaHomePath, codeanalyzerVersion
        ));

        Assertions.assertEquals(0, runCodeAnalyzerJar.getExitCode(),
                "Command should execute successfully");
        Assertions.assertTrue(runCodeAnalyzerJar.getStdout().length() > 0,
                "Should have some output");
    }

    @Test
    void callGraphShouldHaveKnownEdges() throws Exception {
        var runCodeAnalyzerOnCallGraphTest = container.execInContainer(
                "bash", "-c",
                String.format(
                        "export JAVA_HOME=%s && java -jar /opt/jars/codeanalyzer-%s.jar --input=/test-applications/call-graph-test --analysis-level=2",
                        javaHomePath, codeanalyzerVersion
                )
        );


        // Read the output JSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(runCodeAnalyzerOnCallGraphTest.getStdout(), JsonObject.class);
        JsonArray systemDepGraph = jsonObject.getAsJsonArray("system_dependency_graph");
        Assertions.assertTrue(StreamSupport.stream(systemDepGraph.spliterator(), false)
                .map(JsonElement::getAsJsonObject)
                .anyMatch(entry ->
                        "CALL_DEP".equals(entry.get("type").getAsString()) &&
                                "1".equals(entry.get("weight").getAsString()) &&
                                entry.getAsJsonObject("source").get("signature").getAsString().equals("helloString()") &&
                                entry.getAsJsonObject("target").get("signature").getAsString().equals("log()")
                ), "Expected edge not found in the system dependency graph");
    }

    @Test
    void corruptMavenShouldNotBuildWithWrapper() throws IOException, InterruptedException {
        // Make executable
        mavenContainer.execInContainer("chmod", "+x", "/test-applications/mvnw-corrupt-test/mvnw");
        // Let's start by building the project by itself
        var mavenProjectBuildWithWrapper = mavenContainer.withWorkingDirectory("/test-applications/mvnw-corrupt-test").execInContainer("/test-applications/mvnw-corrupt-test/mvnw", "clean", "compile");
        Assertions.assertNotEquals(0, mavenProjectBuildWithWrapper.getExitCode());
    }

    @Test
    void corruptMavenShouldProduceAnalysisArtifactsWhenMVNCommandIsInPath() throws IOException, InterruptedException {
        // Let's start by building the project by itself
        var corruptMavenProjectBuild = mavenContainer.withWorkingDirectory("/test-applications/mvnw-corrupt-test").execInContainer("mvn", "-f", "/test-applications/mvnw-corrupt-test/pom.xml", "clean", "compile");
        Assertions.assertEquals(0, corruptMavenProjectBuild.getExitCode(), "Failed to build the project with system's default Maven.");
        // NOw run codeanalyzer and assert if analysis.json is generated.
        var runCodeAnalyzer = mavenContainer.execInContainer("java", "-jar", String.format("/opt/jars/codeanalyzer-%s.jar", codeanalyzerVersion), "--input=/test-applications/mvnw-corrupt-test", "--output=/tmp/", "--analysis-level=2", "--verbose", "--no-build");
        var codeAnalyzerOutputDirContents = mavenContainer.execInContainer("ls", "/tmp/analysis.json");
        String codeAnalyzerOutputDirContentsStdOut = codeAnalyzerOutputDirContents.getStdout();
        Assertions.assertTrue(codeAnalyzerOutputDirContentsStdOut.length() > 0, "Could not find 'analysis.json'.");
        // mvnw is corrupt, so we should see an error message in the output.
        Assertions.assertTrue(runCodeAnalyzer.getStdout().contains("[ERROR]\tCannot run program \"/test-applications/mvnw-corrupt-test/mvnw\"") && runCodeAnalyzer.getStdout().contains("/mvn."));
        // We should correctly identify the build tool used in the mvn command from the system path.
        Assertions.assertTrue(runCodeAnalyzer.getStdout().contains("[INFO]\tBuilding the project using /usr/bin/mvn."));
    }

    @Test
    void corruptMavenShouldNotTerminateWithErrorWhenMavenIsNotPresentUnlessAnalysisLevel2() throws IOException, InterruptedException {
        // When analysis level 2, we should get a Runtime Exception
        var runCodeAnalyzer = container.execInContainer(
                "bash", "-c",
                String.format(
                        "export JAVA_HOME=%s && java -jar /opt/jars/codeanalyzer-%s.jar --input=/test-applications/mvnw-corrupt-test --output=/tmp/ --analysis-level=2",
                        javaHomePath, codeanalyzerVersion
                )
        );

        Assertions.assertEquals(1, runCodeAnalyzer.getExitCode());
        Assertions.assertTrue(runCodeAnalyzer.getStderr().contains("java.lang.RuntimeException"));
    }

    @Test
    void shouldBeAbleToGenerateAnalysisArtifactForDaytrader8() throws Exception {
        var runCodeAnalyzerOnDaytrader8 = mavenContainer.execInContainer(
                "bash", "-c",
                String.format(
                        "export JAVA_HOME=%s && java -jar /opt/jars/codeanalyzer-%s.jar --input=/test-applications/daytrader8 --analysis-level=1",
                        javaHomePath, codeanalyzerVersion
                )
        );

        Assertions.assertTrue(runCodeAnalyzerOnDaytrader8.getStdout().contains("\"is_entrypoint_class\": true"), "No entry point classes found");
        Assertions.assertTrue(runCodeAnalyzerOnDaytrader8.getStdout().contains("\"is_entrypoint\": true"), "No entry point methods found");
    }

    @Test
    void shouldBeAbleToDetectCRUDOperationsAndQueriesForPlantByWebsphere() throws Exception {
        var runCodeAnalyzerOnPlantsByWebsphere = container.execInContainer(
                "bash", "-c",
                String.format(
                        "export JAVA_HOME=%s && java -jar /opt/jars/codeanalyzer-%s.jar --input=/test-applications/plantsbywebsphere --analysis-level=1 --verbose",
                        javaHomePath, codeanalyzerVersion
                )
        );


        String output = runCodeAnalyzerOnPlantsByWebsphere.getStdout();

        Assertions.assertTrue(output.contains("\"query_type\": \"NAMED\""), "No entry point classes found");
        Assertions.assertTrue(output.contains("\"operation_type\": \"READ\""), "No entry point methods found");
        Assertions.assertTrue(output.contains("\"operation_type\": \"UPDATE\""), "No entry point methods found");
        Assertions.assertTrue(output.contains("\"operation_type\": \"CREATE\""), "No entry point methods found");

        // Convert the expected JSON structure into a string
        String expectedCrudOperation =
                "\"crud_operations\": [" +
                        "{" +
                        "\"line_number\": 115," +
                        "\"operation_type\": \"READ\"," +
                        "\"target_table\": null," +
                        "\"involved_columns\": null," +
                        "\"condition\": null," +
                        "\"joined_tables\": null" +
                        "}]";

        // Expected JSON for CRUD Queries
        String expectedCrudQuery =
                "\"crud_queries\": [" +
                        "{" +
                        "\"line_number\": 141,";

        // Normalize the output and expected strings to ignore formatting differences
        String normalizedOutput = output.replaceAll("\\s+", "");
        String normalizedExpectedCrudOperation = expectedCrudOperation.replaceAll("\\s+", "");
        String normalizedExpectedCrudQuery = expectedCrudQuery.replaceAll("\\s+", "");

        // Assertions for both CRUD operations and queries
        Assertions.assertTrue(normalizedOutput.contains(normalizedExpectedCrudOperation), "Expected CRUD operation JSON structure not found");
        Assertions.assertTrue(normalizedOutput.contains(normalizedExpectedCrudQuery), "Expected CRUD query JSON structure not found");
    }

    @Test
    void symbolTableShouldHaveRecords() throws IOException, InterruptedException {
        var runCodeAnalyzerOnCallGraphTest = container.execInContainer(
                "bash", "-c",
                String.format(
                        "export JAVA_HOME=%s && java -jar /opt/jars/codeanalyzer-%s.jar --input=/test-applications/record-class-test --analysis-level=1",
                        javaHomePath, codeanalyzerVersion
                )
        );

        // Read the output JSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(runCodeAnalyzerOnCallGraphTest.getStdout(), JsonObject.class);
        JsonObject symbolTable = jsonObject.getAsJsonObject("symbol_table");
        Assertions.assertEquals(4, symbolTable.size(), "Symbol table should have 4 records");
    }

    @Test
    void symbolTableShouldHaveDefaultRecordComponents() throws IOException, InterruptedException {
        var runCodeAnalyzerOnCallGraphTest = container.execInContainer(
                "bash", "-c",
                String.format(
                        "export JAVA_HOME=%s && java -jar /opt/jars/codeanalyzer-%s.jar --input=/test-applications/record-class-test --analysis-level=1",
                        javaHomePath, codeanalyzerVersion
                )
        );

        // Read the output JSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(runCodeAnalyzerOnCallGraphTest.getStdout(), JsonObject.class);
        JsonObject symbolTable = jsonObject.getAsJsonObject("symbol_table");
        for (Map.Entry<String, JsonElement> element : symbolTable.entrySet()) {
            String key = element.getKey();
            if (!key.endsWith("PersonRecord.java")) {
                continue;
            }
            JsonObject type = element.getValue().getAsJsonObject();
            if (type.has("type_declarations")) {
                JsonObject typeDeclarations = type.getAsJsonObject("type_declarations");
                JsonArray recordComponent = typeDeclarations.getAsJsonObject("org.example.PersonRecord").getAsJsonArray("record_components");
                Assertions.assertEquals(2, recordComponent.size(), "Record component should have 2 components");
                JsonObject record = recordComponent.get(1).getAsJsonObject();
                Assertions.assertTrue(record.get("name").getAsString().equals("age") && record.get("default_value").getAsInt() == 18, "Record component should have a name");
            }
        }
    }

    @Test
    void parametersInCallableMustHaveStartAndEndLineAndColumns() throws IOException, InterruptedException {
        var runCodeAnalyzerOnCallGraphTest = container.execInContainer(
                "bash", "-c",
                String.format(
                        "export JAVA_HOME=%s && java -jar /opt/jars/codeanalyzer-%s.jar --input=/test-applications/record-class-test --analysis-level=1",
                        javaHomePath, codeanalyzerVersion
                )
        );

        // Read the output JSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(runCodeAnalyzerOnCallGraphTest.getStdout(), JsonObject.class);
        JsonObject symbolTable = jsonObject.getAsJsonObject("symbol_table");
        for (Map.Entry<String, JsonElement> element : symbolTable.entrySet()) {
            String key = element.getKey();
            if (!key.endsWith("App.java")) {
                continue;
            }
            JsonObject type = element.getValue().getAsJsonObject();
            if (type.has("type_declarations")) {
                JsonObject typeDeclarations = type.getAsJsonObject("type_declarations");
                JsonObject mainMethod = typeDeclarations.getAsJsonObject("org.example.App").getAsJsonObject("callable_declarations").getAsJsonObject("main(String[])");
                JsonArray parameters = mainMethod.getAsJsonArray("parameters");
                // There should be 1 parameter
                Assertions.assertEquals(1, parameters.size(), "Callable should have 1 parameter");
                JsonObject parameter = parameters.get(0).getAsJsonObject();
                // Start and end line and column should not be -1
                Assertions.assertTrue(parameter.get("start_line").getAsInt() == 7 && parameter.get("end_line").getAsInt() == 7 && parameter.get("start_column").getAsInt() == 29 && parameter.get("end_column").getAsInt() == 41, "Parameter should have start and end line and columns");
            }
        }
    }
}
