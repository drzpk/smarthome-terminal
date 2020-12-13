package dev.drzepka.smarthome.terminal.server.application.configuration.routes

import dev.drzepka.smarthome.terminal.common.api.category.CategoryModel
import dev.drzepka.smarthome.terminal.common.api.clients.ClientModel
import dev.drzepka.smarthome.terminal.common.api.screen.Screen
import dev.drzepka.smarthome.terminal.common.api.screen.element.property.simple.StringProperty
import dev.drzepka.smarthome.terminal.server.domain.converter.ConversionService
import dev.drzepka.smarthome.terminal.server.domain.service.ClientService
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get

fun Route.applications() {
    val clientService = get<ClientService>()
    val conversionService = get<ConversionService>()

    // Application = Client
    route("/applications") {
        get("") {
            val clients = clientService.getAllClients()
            val models = clients.map { conversionService.convert<ClientModel>(it) }
            call.respond(models)
        }

        route("/{id}") {
            get("") {
                val appId = call.parameters["id"]!!
            }

            route("/categories") {
                get("") {
                    val clientId = call.parameters["id"]!!.toInt()
                    val client = clientService.findClient(clientId)
                    val categories =
                        client!!.categories?.map { conversionService.convert<CategoryModel>(it) } ?: emptyList()
                    call.respond(categories)
                }

                get("/{categoryId}/screen") {
                    val blankCategory = CategoryModel(1, "blank")
                    val screen = Screen(1)
                    screen.addElement(StringProperty(1))
                    screen.addElement(StringProperty(2))
                    screen.addElement(StringProperty(3))

                    call.respond(screen)
                }
            }
        }
    }
}