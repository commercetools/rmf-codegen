package io.vrap.rmf.codegen.kt.io

import java.lang.IllegalStateException

open class TemplateFile(val content:String, val relativePath:String){
    init {
        if(relativePath.isNullOrBlank()){
            throw IllegalStateException("The path shouldn't be nor blank or empty")
        }
    }
}