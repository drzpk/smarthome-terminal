package dev.drzepka.smarthome.terminal.server.application.converter.element

import dev.drzepka.smarthome.terminal.common.api.element.ScreenModel
import dev.drzepka.smarthome.terminal.server.domain.converter.ConversionService
import dev.drzepka.smarthome.terminal.server.domain.entity.Screen

class ScreenConverter(conversionService: ConversionService) : AbstractElementConverter<Screen, ScreenModel>(conversionService) {

    override fun convertR(source: Screen): ScreenModel? {
        val screenModel = ScreenModel()
        screenModel.id = source.id
        convertElements(source, screenModel)
        return screenModel
    }

    override fun convertL(source: ScreenModel): Screen? {
        val screen =  Screen(source.id)
        convertElements(source, screen)
        return screen
    }

}