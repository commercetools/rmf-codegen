//package io.vrap.rmf.codegen.spring.rest.client;
//
//import com.google.common.collect.LinkedListMultimap;
//import com.google.common.collect.Multimap;
//import com.squareup.javapoet.JavaFile;
//import com.squareup.javapoet.MethodSpec;
//import com.squareup.javapoet.TypeSpec;
//import io.vrap.rmf.codegen.common.generator.core.GenerationResult;
//import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
//import io.vrap.rmf.raml.model.modules.Api;
//import io.vrap.rmf.raml.model.resources.Method;
//import io.vrap.rmf.raml.model.resources.Resource;
//import io.vrap.rmf.raml.model.resources.ResourceContainer;
//
//import javax.lang.model.element.Modifier;
//import java.io.IOException;
//import java.net.URI;
//import java.nio.file.Path;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class SpringRestClientCodegen implements CodeGenerator<Api> {
//    private final GeneratorConfig config;
//
//    public SpringRestClientCodegen(final GeneratorConfig config) {
//        this.config = config;
//    }
//
//    @Override
//    public GenerationResult generateStub(final Api from) throws IOException {
//        final List<Resource> allResources = getAllContainedResurces(from);
//        final Multimap<String, Resource> resourcesByPathName = LinkedListMultimap.create();
//        allResources.forEach(r -> resourcesByPathName.put(r.getResourcePathName(), r));
//
//        final String pkg = config.getPackagePrefix();
//
//        final List<Path> generatedFiles = new ArrayList<>();
//        for (final String resourcePathName : resourcesByPathName.keySet()) {
//            final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(config.getMappedResourceName(resourcePathName))
//                    .addModifiers(Modifier.PUBLIC);
//
//            final Collection<Resource> resources = resourcesByPathName.get(resourcePathName);
//            for (final Resource resource : resources) {
//                for (final Method method : resource.getMethods()) {
//                    final String methodName = config.getMappedMethodName(method);
//                    final MethodSpec methodSpec = MethodSpec.methodBuilder(methodName).addModifiers(Modifier.PUBLIC).build();
//                    classBuilder.addMethod(methodSpec);
//                }
//            }
//            final TypeSpec typeSpec = classBuilder.build();
//            final JavaFile javaFile = JavaFile.builder(pkg, typeSpec).build();
//            javaFile.writeTo(config.getOutputFolder());
//            final URI uri = javaFile.toJavaFileObject().toUri();
//            final Path path = config.getOutputFolder().resolve(uri.getPath());
//            generatedFiles.add(path);
//        }
//        return GenerationResult.of(generatedFiles);
//    }
//
//    // TODO: move this method to our ResourceContainer interface
//    private List<Resource> getAllContainedResurces(final ResourceContainer resourceContainer) {
//        return resourceContainer.getResources().stream()
//                .flatMap(r -> r.getAllContainedResources().stream())
//                .collect(Collectors.toList());
//    }
//}
