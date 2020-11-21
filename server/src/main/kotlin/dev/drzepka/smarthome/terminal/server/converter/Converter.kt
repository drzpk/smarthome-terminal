package dev.drzepka.smarthome.terminal.server.converter

import dev.drzepka.smarthome.terminal.common.api.ApiModel
import dev.drzepka.smarthome.terminal.server.model.ModelImplementation

interface Converter<Model : ApiModel, Implementation : ModelImplementation> {
    fun toImplementation(model: Model): Implementation
    fun toModel(implementation: Implementation): Model
}