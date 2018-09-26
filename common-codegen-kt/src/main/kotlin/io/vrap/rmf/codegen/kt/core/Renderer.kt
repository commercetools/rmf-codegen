package io.vrap.rmf.codegen.kt.core

import io.vrap.rmf.codegen.kt.io.TemplateFile

interface Renderer<T> {

    fun render(type:T):TemplateFile

}