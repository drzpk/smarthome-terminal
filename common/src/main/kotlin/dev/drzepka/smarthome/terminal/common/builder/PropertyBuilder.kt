package dev.drzepka.smarthome.terminal.common.builder

import dev.drzepka.smarthome.terminal.common.api.element.ElementModel
import dev.drzepka.smarthome.terminal.common.api.element.property.simple.IntPropertyModel
import dev.drzepka.smarthome.terminal.common.api.element.property.simple.StringPropertyModel


fun ElementModel.intProperty(init: IntPropertyModel.() -> Unit): IntPropertyModel {
    val model = IntPropertyModel()
    init(model)
    addChild(model)
    return model
}

fun ElementModel.stringProperty(init: StringPropertyModel.() -> Unit): StringPropertyModel {
    val model = StringPropertyModel()
    init(model)
    addChild(model)
    return model
}