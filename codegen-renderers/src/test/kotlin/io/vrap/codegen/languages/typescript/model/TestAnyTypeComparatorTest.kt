package io.vrap.codegen.languages.typescript.model

import io.vrap.rmf.raml.model.types.TypesFactory
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*

class TestAnyTypeComparatorTest {

    @Test
    fun testComparator() {

        val baseType = TypesFactory.eINSTANCE.createObjectType()
        val subType1 = TypesFactory.eINSTANCE.createObjectType()
        subType1.type = baseType
        val subType2 = TypesFactory.eINSTANCE.createObjectType()
        subType2.type = baseType

        val cmp = TypeScriptModuleRenderer.AnyTypeComparator

        assertEquals(cmp.compare(baseType, subType1), -1)
        assertEquals(cmp.compare(subType1, baseType), 1)

        val sorted = Arrays.asList(subType1, subType2, baseType).sortedWith(cmp)
        assertEquals(sorted[0], baseType)
    }
}
