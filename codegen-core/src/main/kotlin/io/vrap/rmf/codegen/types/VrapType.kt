package io.vrap.rmf.codegen.types

sealed class VrapType

open class VrapObjectType(val `package` :String, val simpleClassName:String) : VrapType() {


    override fun toString(): String {
        return "VrapObjectType(`package`='$`package`', simpleClassName='$simpleClassName')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VrapObjectType

        if (`package` != other.`package`) return false
        if (simpleClassName != other.simpleClassName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = `package`.hashCode()
        result = 31 * result + simpleClassName.hashCode()
        return result
    }
}

class VrapDateTimeType(`package` :String, simpleClassName:String, val format: String): VrapObjectType(`package`,simpleClassName) {

    override fun toString(): String {
        return "VrapDateTimeType(format='$format')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as VrapDateTimeType

        if (format != other.format) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + format.hashCode()
        return result
    }
}

class VrapLibraryType( `package` :String, simpleClassName:String) : VrapObjectType(`package`,simpleClassName)

class VrapEnumType(val `package` :String, val simpleClassName:String) : VrapType() {


    override fun toString(): String {
        return "VrapEnumType(`package`='$`package`', simpleClassName='$simpleClassName')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VrapEnumType

        if (`package` != other.`package`) return false
        if (simpleClassName != other.simpleClassName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = `package`.hashCode()
        result = 31 * result + simpleClassName.hashCode()
        return result
    }
}

/**
 * Represent a type that comes from the default package
 */
class VrapScalarType(val scalarType:String) : VrapType() {


    override fun toString(): String {
        return "VrapScalarType(scalarType='$scalarType')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VrapScalarType

        if (scalarType != other.scalarType) return false

        return true
    }

    override fun hashCode(): Int {
        return scalarType.hashCode()
    }
}

class VrapArrayType(val itemType: VrapType) : VrapType(){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VrapArrayType

        if (itemType != other.itemType) return false

        return true
    }

    override fun hashCode(): Int {
        return itemType.hashCode()
    }
}

class VrapNilType : VrapType(){

    val name = "void"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

}
