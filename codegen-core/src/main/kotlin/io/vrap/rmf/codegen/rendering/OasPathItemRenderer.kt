package io.vrap.rmf.codegen.rendering

import io.swagger.v3.oas.models.PathItem

interface OasPathItemRenderer : Renderer<Map.Entry<String, PathItem>> {
}
