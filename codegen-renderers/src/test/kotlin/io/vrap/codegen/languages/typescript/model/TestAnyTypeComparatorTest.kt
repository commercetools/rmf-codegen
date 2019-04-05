package io.vrap.codegen.languages.typescript.model

import io.vrap.rmf.raml.model.types.TypesFactory
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*

class TestAnyTypeComparatorTest {

    @Test
    fun testComparator() {

        val baseType = TypesFactory.eINSTANCE.createObjectType()
        baseType.name = "Base"
        val aType = TypesFactory.eINSTANCE.createObjectType()
        aType.name = "AType"
        val subType1 = TypesFactory.eINSTANCE.createObjectType()
        subType1.name = "SubType1"
        subType1.type = baseType
        val subType2 = TypesFactory.eINSTANCE.createObjectType()
        subType2.name = "SubType2"
        subType2.type = baseType

        val subSubType2 = TypesFactory.eINSTANCE.createObjectType()
        subSubType2.name = "SubSubType2"
        subSubType2.type = subType1
        val cmp = TypeScriptModuleRenderer.AnyTypeComparator

        assertEquals(cmp.compare(baseType, subType1), -1)
        assertEquals(cmp.compare(subType1, baseType), 1)

        val sorted = Arrays.asList(subType1, subType2, baseType).sortedWith(cmp)
        assertEquals(sorted[0], baseType)
        assertEquals(sorted[1], aType)
        assertEquals(sorted[3], subType1)
        assertEquals(sorted[4], subType2)
        assertEquals(sorted[5], subSubType2)
    }
}
