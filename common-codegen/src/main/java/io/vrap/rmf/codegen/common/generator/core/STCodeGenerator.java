package io.vrap.rmf.codegen.common.generator.core;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.squareup.javapoet.ClassName;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vrap.rmf.codegen.common.generator.util.TypeNameSwitch;
import io.vrap.rmf.raml.model.elements.NamedElement;
import io.vrap.rmf.raml.model.resources.Resource;
import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch;
import io.vrap.rmf.raml.model.types.AnyType;
import io.vrap.rmf.raml.model.types.StringType;
import io.vrap.rmf.raml.model.types.util.TypesSwitch;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.ComposedSwitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.AutoIndentWriter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.misc.ErrorBuffer;

import javax.inject.Named;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class STCodeGenerator {

    private final JavaSTFileSwitch javaSTFileSwitch = new JavaSTFileSwitch();
    private static final Logger LOGGER = LoggerFactory.getLogger(STCodeGenerator.class);

    private Injector injector;

    private List<AnyType> alltypes;

    private List<Resource> allResources;

    private Path outputDir;

    private TypeNameSwitch typeNameSwitch;

    private Map<String, String> customMapping;

    @Inject
    public STCodeGenerator(Injector injector, List<AnyType> alltypes,
                           @Named(GeneratorConfig.OUTPUT_FOLDER) Path outputDir,
                           TypeNameSwitch typeNameSwitch,
                           Map<String, String> customMapping,
                           List<Resource> allResources) {

        this.injector = injector;
        this.alltypes = alltypes;
        this.outputDir = outputDir;
        this.typeNameSwitch = typeNameSwitch;
        this.customMapping = customMapping;
        this.allResources = allResources;
        injector.injectMembers(javaSTFileSwitch);

    }

    public Single<GenerationResult> generateClasses() {

        final FilterSwitch filterSwitch = new FilterSwitch();

        final List<EObject> concernedEntities = new ArrayList<>();
        concernedEntities.addAll(alltypes);
        concernedEntities.addAll(allResources);

        final Single<GenerationResult> typesFlowable = Flowable.fromIterable(concernedEntities)
                .filter(filterSwitch::doSwitch)
                .flatMap(anyType ->
                        Flowable.just(anyType).map(javaSTFileSwitch::doSwitch)
                                .map(STCodeGenerator::getTemplate)
                                .map(st -> st.add("input", anyType))
                                .map(st -> generateFile(anyType, st)))
                .toList()
                .map(GenerationResult::of);

        return typesFlowable;
    }

    private Path generateFile(EObject eObject, ST st) throws Exception {
        ClassName className = (ClassName) typeNameSwitch.doSwitch(eObject);
        String packagePath = className.reflectionName().replaceAll("\\.", "/") + ".java";
        Path outputPath = Paths.get(outputDir.toAbsolutePath().toString(), packagePath);
        outputPath.getParent().toFile().mkdirs();
        PrintWriter printWriter = new PrintWriter(outputPath.toFile());
        ErrorBuffer errorBuffer = new ErrorBuffer();
        st.write(new AutoIndentWriter(printWriter), errorBuffer);
        printWriter.close();
        if (!errorBuffer.errors.isEmpty()) {
            LOGGER.error(errorBuffer.toString());
        }
        return outputPath;

    }


    public static ST getTemplate(STGroupFile stGroupFile) throws Exception {
        final ST st = stGroupFile.getInstanceOf("template");
        if (st == null) {
            throw new Exception(stGroupFile + " must contain at least one function named 'template'");
        }
        return st;
    }


    private class FilterSwitch extends ComposedSwitch<Boolean> {


        FilterSwitch() {
            addSwitch(new FilterTypeSwitch());
            addSwitch(new FilterResourcesSwitch());
        }

        /**
         * This filter is used to filter files that are explicitly provided by the sdk developer
         */
        private class FilterTypeSwitch extends TypesSwitch<Boolean> {
            @Override
            public Boolean caseNamedElement(NamedElement object) {
                return customMapping.get(object.getName()) == null;
            }

            @Override
            public Boolean caseStringType(StringType stringType) {
                if (stringType.getEnum() == null || stringType.getEnum().isEmpty()) {
                    return false;
                }
                return true;
            }

            @Override
            public Boolean defaultCase(EObject object) {
                return false;
            }
        }

        private class FilterResourcesSwitch extends ResourcesSwitch<Boolean> {
            @Override
            public Boolean caseResource(Resource resource) {
//                if (StringUtils.isNotEmpty(resource.getResourcePathName())) {
//                    System.out.println();
//                }
                return StringUtils.isNotEmpty(resource.getResourcePathName());
            }
            @Override
            public Boolean defaultCase(EObject object) {
                return false;
            }
        }
    }

}