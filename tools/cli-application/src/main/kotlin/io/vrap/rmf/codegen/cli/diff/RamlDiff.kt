package io.vrap.rmf.codegen.cli.diff

import io.vrap.rmf.raml.model.modules.Api

class RamlDiff private constructor(original: Api, changed: Api, private val differs: List<Differ<Any>>) {
    private val dataProvider = DataProvider(original, changed)
    class Builder {
        private var differs: List<Differ<Any>> = listOf()
        private var original: Api? = null
        private var changed: Api? = null

        fun original(original: Api): Builder {
            this.original = original
            return this
        }

        fun changed(changed: Api): Builder {
            this.changed = changed
            return this
        }

        fun plus(differ: List<Differ<Any>>): Builder {
            differs = differs.plus(differ)
            return this
        }

        fun plus(differ: Differ<*>): Builder {
            differs = differs.plus(differ as Differ<Any>)
            return this
        }

        fun build(): RamlDiff {
            return RamlDiff(original!!, changed!!, differs)
        }
    }

    fun diff(): List<Diff<Any>> {
        return differs.flatMap {
            it.diff(dataProvider.by(it.diffDataType) as DiffData<Any>)
        }
    }
}
