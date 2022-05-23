package io.vrap.rmf.codegen.rendering

import io.vrap.rmf.codegen.io.TemplateFile

interface Renderer<T> {

    fun render(type:T):TemplateFile

}
