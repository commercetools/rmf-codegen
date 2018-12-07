package io.vrap.rmf.codegen.types

import com.google.inject.Inject
import io.vrap.rmf.raml.model.elements.NamedElement
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.ComposedSwitch
import org.slf4j.LoggerFactory

class VrapTypeProvider @Inject constructor(packageProvider: PackageProvider,
                                           val languageBaseTypes: LanguageBaseTypes,
                                           val customTypeMapping: MutableMap<String, VrapType>
) : ComposedSwitch<VrapType>() {


    init {
        addSwitch(AnyTypeProvider(packageProvider, languageBaseTypes))
        addSwitch(ResourcesTypeProvider(packageProvider))
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
        private val LOGGER = LoggerFactory.getLogger(VrapTypeProvider::class.java)
    }
}
