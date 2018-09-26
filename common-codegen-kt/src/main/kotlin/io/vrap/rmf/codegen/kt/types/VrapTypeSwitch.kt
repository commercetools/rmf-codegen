package io.vrap.rmf.codegen.kt.types

import com.google.inject.Inject
import io.vrap.rmf.raml.model.elements.NamedElement
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.ComposedSwitch
import org.slf4j.LoggerFactory

class VrapTypeSwitch @Inject constructor(packageSwitch: PackageSwitch,
                                         val languageBaseTypes: LanguageBaseTypes,
                                         val customTypeMapping: MutableMap<String, VrapType>
) : ComposedSwitch<VrapType>() {


    init {
        addSwitch(ModelTypeSwitch(packageSwitch, languageBaseTypes))
        addSwitch(ResourcesTypeSwitch(packageSwitch))
    }

    override fun doSwitch(eObject: EObject): VrapType {
        if (eObject is NamedElement) {
            val className = customTypeMapping[eObject.name]
            if (className != null) {
                return className
            }
        }

        val result = super.doSwitch(eObject)

        if (result == null) {
            LOGGER.warn("No typeName was associated with {}", eObject)
            return languageBaseTypes.objectType
        }
        return result
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(VrapTypeSwitch::class.java)
    }
}