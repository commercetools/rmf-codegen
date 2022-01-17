package io.vrap.rmf.codegen.rendring

import io.swagger.v3.oas.models.PathItem

interface OasPathItemRenderer : Renderer<Map.Entry<String, PathItem>> {
}
