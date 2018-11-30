//package io.vrap.rmf.codegen.common;
//
//import org.assertj.core.api.Assertions;
//import org.gradle.testkit.runner.BuildResult;
//import org.gradle.testkit.runner.GradleRunner;
//import org.gradle.testkit.runner.TaskOutcome;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.TemporaryFolder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.file.Paths;
//
//public class BuildLogicFunctionalTest {
//
//    @Rule
//    public final TemporaryFolder testProjectDir = new TemporaryFolder();
//    private File buildFile;
//
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Before
//    public void setup() throws IOException {
//        buildFile = testProjectDir.newFile("build.gradle");
//    }
//
//    @Test
//    public void testGenerateEntitiesTask() throws IOException {
//
//        final File apiFile = Paths.get("src/test/resources/api-spec/api.raml").toAbsolutePath().toFile();
//        System.err.println(apiFile);
//        Assertions.assertThat(apiFile).exists();
//
//        String buildFileContent =
//                        "plugins {\n" +
//                        "    id 'io.vrap.rmf.codegen-plugin'\n" +
//                        "}\n" +
//                        "generateRamlStub{\n" +
//                        "    ramlFileLocation= file('"+apiFile.getAbsolutePath()+"')\n" +
//                        "    outputFolder=file('"+testProjectDir.getRoot().toString()+"')\n" +
//                        "    packagePrefix= 'com.commercetools'\n" +
//                        "}";
//
//        writeFile(buildFile, buildFileContent);
//
//        BuildResult result = GradleRunner.create()
//                .withProjectDir(testProjectDir.getRoot())
//                .withArguments("generateRamlStub","--stacktrace")
//                .withPluginClasspath()
//                .build();
//
//        logger.warn(result.getOutput());
//
//        Assertions.assertThat(result.task(":generateRamlStub").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
//
//        Assertions.assertThat(testProjectDir.getRoot().list((dir, name) -> name.equals("com"))).isNotEmpty();
//
//
//    }
//
//    private void writeFile(File destination, String content) throws IOException {
//        BufferedWriter output = null;
//        try {
//            output = new BufferedWriter(new FileWriter(destination));
//            output.write(content);
//        } finally {
//            if (output != null) {
//                output.close();
//            }
//        }
//    }
//}