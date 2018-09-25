package io.vrap.rmf.codegen.kt.types

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.rmf.codegen.kt.di.VrapConstants
import io.vrap.rmf.raml.model.modules.Library
import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch
import io.vrap.rmf.raml.model.types.AnnotationsFacet
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.util.TypesSwitch
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.ComposedSwitch



class PackageSwitch @Inject constructor(@Named(VrapConstants.PACKAGE_NAME) val basePackage: String) : ComposedSwitch<String>() {

    init {
        addSwitch(TypePackageSwitch())
        addSwitch(ResourcePackageSwitch())
    }

    override fun defaultCase(eObject: EObject): String {
        return basePackage
    }

    private inner class TypePackageSwitch : TypesSwitch<String>() {
        override fun defaultCase(`object`: EObject?): String {
            return "$basePackage.models"
        }

        override fun caseAnyType(type: AnyType): String {
            var type = type
            val modelsPackage = "$basePackage.models"
            while (type!!.type != null) {
                if (type.eContainer() is Library && (type.eContainer() as Library).getAnnotation("package") != null) {
                    break
                }
                type = type.type
            }
            var eContainer: EObject? = type.eContainer()
            while (eContainer != null) {
                if (eContainer is AnnotationsFacet) {

                    return eContainer.let { it.getAnnotation("package") }
                            ?.let { it.value }
                            ?.let { it.value }
                            ?.let { "$modelsPackage.$it" }
                            ?: modelsPackage
                }
                eContainer = eContainer.eContainer()
            }
            return modelsPackage
        }

    }

    private inner class ResourcePackageSwitch : ResourcesSwitch<String>() {
        override fun defaultCase(`object`: EObject): String {
            return "$basePackage.client"
        }
    }


}
