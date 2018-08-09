package io.vrap.rmf.codegen.common.generator.core;

import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vrap.rmf.codegen.common.generator.util.TypeNameSwitch;
import io.vrap.rmf.codegen.common.processor.extension.ExtensionMapper;
import io.vrap.rmf.raml.model.elements.NamedElement;
import io.vrap.rmf.raml.model.resources.Resource;
import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch;
import io.vrap.rmf.raml.model.types.AnyType;
import io.vrap.rmf.raml.model.types.StringType;
import io.vrap.rmf.raml.model.types.util.TypesSwitch;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.ComposedSwitch;
import org.reactivestreams.Publisher;
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

/**
 * Stringtemplate (ST) based code generator.
 */
public class STCodeGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(STCodeGenerator.class);

    private final STTemplateSwitch templateSwitch;
    private final GeneratorConfig generatorConfig;

    private List<AnyType> allTypes;

    private List<Resource> allResources;

    private TypeNameSwitch typeNameSwitch;

    @Inject
    public STCodeGenerator(final List<AnyType> allTypes,
                           final GeneratorConfig generatorConfig,
                           final TypeNameSwitch typeNameSwitch,
                           final List<Resource> allResources,
                           final List<ExtensionMapper> extensionMappers) {
        this.allTypes = allTypes;
        this.generatorConfig = generatorConfig;
        this.typeNameSwitch = typeNameSwitch;
        this.allResources = allResources;
        templateSwitch = new STTemplateSwitch(generatorConfig.getGenLanguage(), extensionMappers);
    }

    public Single<GenerationResult> generateClasses() {

        final FilterSwitch filterSwitch = new FilterSwitch();

        List<Publisher<Path>> flowables = new ArrayList<>();
        //TypesGenerator
        Flowable<Path> flowable1 = Flowable.fromIterable(allTypes)
                .filter(filterSwitch::doSwitch)
                .map(type -> generateFile(type));

        //ResourcesGenerator
        Flowable.fromIterable(allResources)
                .filter(filterSwitch::doSwitch)
                .groupBy(typeNameSwitch::doSwitch)
                .subscribe(resourceGroupedFlowable -> {
                            Flowable<Path> pathFlowable = resourceGroupedFlowable
                                    .toList()
                                    .map(resources -> new EObjectsCollection(resources, ((ClassName) resourceGroupedFlowable.getKey())))
                                    .map(this::generateFile)
                                    .toFlowable();
                            flowables.add(pathFlowable);
                        }

                );

        flowables.add(flowable1);
        final Single<GenerationResult> generationResultSingle = Flowable.merge(flowables)
                .toList()
                .map(GenerationResult::of);

        return generationResultSingle;
    }

    private Path generateFile(Object object) throws Exception {

        STGroupFile groupFile = templateSwitch.getSTFileFor(object);
        ST template = getTemplate(groupFile);
        template.add("input", object);

        ClassName className;
        if (object instanceof EObject) {
            className = (ClassName) typeNameSwitch.doSwitch(((EObject) object));
        } else if (object instanceof EObjectsCollection) {
            className = ((EObjectsCollection) object).getCollectionClassName();
        } else {
            throw new IllegalArgumentException("unhandled object for templates " + object);
        }

        String packagePath = className.reflectionName().replaceAll("\\.", "/") + ".java";

        final String output = generatorConfig.getOutputFolder().toAbsolutePath().toString();
        final Path outputPath = Paths.get(output, packagePath);
        outputPath.getParent().toFile().mkdirs();
        PrintWriter printWriter = new PrintWriter(outputPath.toFile());
        ErrorBuffer errorBuffer = new ErrorBuffer();
        template.write(new AutoIndentWriter(printWriter), errorBuffer);
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
                return generatorConfig.getCustomTypeMapping().get(object.getName()) == null;
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
                return StringUtils.isNotEmpty(resource.getResourcePathName());

            }

            @Override
            public Boolean defaultCase(EObject object) {
                return false;
            }
        }
    }
}