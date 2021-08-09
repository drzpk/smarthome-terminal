package dev.drzepka.smarthome.terminal.server.domain.converter

import dev.drzepka.smarthome.terminal.common.api.screen.response.ProcessScreenUpdateResponse
import dev.drzepka.smarthome.terminal.common.api.screen.response.ScreenUpdateErrorResponse
import dev.drzepka.smarthome.terminal.common.api.screen.response.ScreenUpdatedResponse
import dev.drzepka.smarthome.terminal.common.transport.message.ScreenUpdateMessageResponse

class ScreenUpdateMessageToResponseConverter : Converter<ScreenUpdateMessageResponse, ProcessScreenUpdateResponse> {

    override fun convert(source: ScreenUpdateMessageResponse): ProcessScreenUpdateResponse {
        return when (source.status) {
            ScreenUpdateMessageResponse.Status.UPDATED -> convertToUpdatedResponse(source)
            ScreenUpdateMessageResponse.Status.ERROR -> convertToErrorResponse(source)
            ScreenUpdateMessageResponse.Status.UNKNOWN -> throwIncorrectStatusException(source)
        }
    }

    private fun convertToUpdatedResponse(source: ScreenUpdateMessageResponse): ProcessScreenUpdateResponse {
        val response = ScreenUpdatedResponse()
        response.message = source.message
        return response
    }

    private fun convertToErrorResponse(source: ScreenUpdateMessageResponse): ProcessScreenUpdateResponse {
        val response = ScreenUpdateErrorResponse()
        response.message = source.message
        return response
    }

    private fun throwIncorrectStatusException(source: ScreenUpdateMessageResponse): Nothing {
        throw IllegalStateException("Client responded with UNKNOWN status for message %s".format(source.requestMessageId))
    }
}