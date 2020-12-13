package dev.drzepka.smarthome.terminal.server.application.converter

import dev.drzepka.smarthome.terminal.common.api.clients.ClientModel
import dev.drzepka.smarthome.terminal.server.domain.converter.Converter
import dev.drzepka.smarthome.terminal.server.domain.entity.Client

class ClientEntityToModelConverter : Converter<Client, ClientModel> {

    override fun convert(source: Client): ClientModel? {
        return ClientModel().apply {
            id = source.id
            name = source.name
        }
    }

}