package io.vrap.rmf.codegen.kt.types

sealed class VrapType

open class VrapObjectType(val `package` :String, val simpleClassName:String) : VrapType()

//Represent a tpe that comes from the default package
class VrapDefaultObjectType(`package` :String, simpleClassName:String) :VrapObjectType(`package` ,simpleClassName )

class VrapArrayType(val itemType: VrapType) : VrapType()

class VrapNilType : VrapType()