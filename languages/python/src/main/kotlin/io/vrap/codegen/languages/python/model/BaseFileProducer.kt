/**
 *  Copyright 2021 Michael van Tellingen
 */
package io.vrap.codegen.languages.python.model

import io.vrap.codegen.languages.python.pyGeneratedComment
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer

class BaseFileProducer constructor() : FileProducer {

    override fun produceFiles(): List<TemplateFile> = listOf(
        TemplateFile(
            relativePath = "models/_abstract.py",
            content = """|
                |$pyGeneratedComment
                |
                |class _BaseType:
                |    def __eq__(self, other):
                |        if other.__class__ is self.__class__:
                |            return self.__values__() == other.__values__()
                |        else:
                |            return NotImplemented
                |
                |    def __ne__(self, other):
                |        result = self.__eq__(other)
                |        if result is NotImplemented:
                |            return NotImplemented
                |        else:
                |            return not result
                |
                |    def __lt__(self, other):
                |        if other.__class__ is self.__class__:
                |            return self.__values__() < other.__values__()
                |        else:
                |            return NotImplemented
                |
                |    def __le__(self, other):
                |        if other.__class__ is self.__class__:
                |            return self.__values__() <= other.__values__()
                |        else:
                |            return NotImplemented
                |
                |    def __gt__(self, other):
                |        if other.__class__ is self.__class__:
                |            return self.__values__() > other.__values__()
                |        else:
                |            return NotImplemented
                |
                |    def __ge__(self, other):
                |        if other.__class__ is self.__class__:
                |            return self.__values__() >= other.__values__()
                |        else:
                |            return NotImplemented
                |
                |    def __values__(self):
                |        return tuple(self.__dict__.values())
                |
                |    def __hash__(self):
                |        return hash((self.__class__,) + self.__values__())
                |
                |    def __repr__(self):
                |        return "%s(%s)" % (
                |            self.__class__.__name__,
                |            ", ".join("%s=%r" % (k, v) for k, v in self.__dict__.items())
                |        )
            """.trimMargin()
        )
    )
}
