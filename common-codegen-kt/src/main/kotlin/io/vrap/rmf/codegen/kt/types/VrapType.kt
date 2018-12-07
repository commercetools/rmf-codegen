package io.vrap.rmf.codegen.kt.types

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

open class VrapEnumType(val `package` :String, val simpleClassName:String) : VrapType() {


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

open class VrapScalarType(val scalarType:String) : VrapType() {


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

/**
 * Represent a tpe that comes from the default package
 */
class VrapDefaultObjectType(`package` :String, simpleClassName:String) :VrapObjectType(`package` ,simpleClassName ){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false
        return true
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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

}
