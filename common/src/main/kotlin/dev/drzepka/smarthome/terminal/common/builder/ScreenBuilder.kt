package dev.drzepka.smarthome.terminal.common.builder

import dev.drzepka.smarthome.terminal.common.api.element.ScreenModel
import dev.drzepka.smarthome.terminal.common.util.IdSpace

fun screen(idSpace: IdSpace, init: ScreenModel.() -> Unit): ScreenModel {
    val model = ScreenModel()
    model.idSpace = idSpace
    init(model)
    return model
}