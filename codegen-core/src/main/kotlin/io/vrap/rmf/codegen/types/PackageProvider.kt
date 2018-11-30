package io.vrap.rmf.codegen.types

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.rmf.codegen.di.VrapConstants
import io.vrap.rmf.raml.model.modules.Library
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch
import io.vrap.rmf.raml.model.types.AnnotationsFacet
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.util.TypesSwitch
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.ComposedSwitch


class PackageProvider @Inject constructor(
        @Named(io.vrap.rmf.codegen.di.VrapConstants.BASE_PACKAGE_NAME) val basePackage: String,
        @Named(io.vrap.rmf.codegen.di.VrapConstants.MODEL_PACKAGE_NAME) val modelPackage: String,
        @Named(io.vrap.rmf.codegen.di.VrapConstants.CLIENT_PACKAGE_NAME) val clientPackage: String
) : ComposedSwitch<String>() {

    init {
        addSwitch(TypePackageSwitch())
        addSwitch(ResourcePackageSwitch())
    }

    override fun defaultCase(eObject: EObject): String {
        return basePackage
    }

    private inner class TypePackageSwitch : TypesSwitch<String>() {
        override fun defaultCase(`object`: EObject?): String = modelPackage

        override fun caseAnyType(type: AnyType?): String {
            var currentType = type
            while (currentType != null) {

                val annotation = currentType.getAnnotation("package")
                if (annotation != null) {
                    return annotation.let { it.value }
                            ?.let { it.value }
                            ?.let { "$modelPackage.$it" }
                            ?: modelPackage
                }
                if (currentType.eContainer() is Library && (currentType.eContainer() as Library).getAnnotation("package") != null) {

                    var eContainer: EObject? = currentType.eContainer()
                    while (eContainer != null) {
                        if (eContainer is AnnotationsFacet) {
                            return eContainer.let { it.getAnnotation("package") }
                                    ?.let { it.value?.value }
                                    ?.let { "$modelPackage.$it" }
                                    ?: modelPackage
                        }
                        eContainer = eContainer.eContainer()

                    }
                }
                currentType = currentType.type
            }

            return modelPackage
        }

    }

    private inner class ResourcePackageSwitch : ResourcesSwitch<String>() {
        override fun caseMethod(`object`: Method): String {
            return "$clientPackage.resource"
        }

        override fun defaultCase(`object`: EObject): String {
            return clientPackage
        }
    }
}
