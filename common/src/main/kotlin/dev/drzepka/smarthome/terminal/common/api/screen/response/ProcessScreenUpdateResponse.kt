package dev.drzepka.smarthome.terminal.common.api.screen.response

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "status")
@JsonSubTypes(
    JsonSubTypes.Type(value = ScreenUpdatedResponse::class, name = "UPDATED"),
    JsonSubTypes.Type(value = ScreenValidationErrorResponse::class, name = "VALIDATION_ERROR"),
    JsonSubTypes.Type(value = ScreenUpdateErrorResponse::class, name = "ERROR")
)
abstract class ProcessScreenUpdateResponse {
    abstract val status: Status

    @Suppress("unused")
    enum class Status {
        UPDATED, ERROR, VALIDATION_ERROR
    }
}