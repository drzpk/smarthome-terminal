package dev.drzepka.smarthome.terminal.server.application.configuration.routes

import dev.drzepka.smarthome.terminal.common.api.category.CategoryModel
import dev.drzepka.smarthome.terminal.common.api.clients.ClientModel
import dev.drzepka.smarthome.terminal.server.domain.converter.ConversionService
import dev.drzepka.smarthome.terminal.server.domain.service.ScreenService
import dev.drzepka.smarthome.terminal.server.domain.service.client.ClientService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get

fun Route.applications() {
    val clientService = get<ClientService>()
    val screenService = get<ScreenService>()
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
                    val clientId = call.parameters["id"]!!.toInt()
                    val client = clientService.findClient(clientId)

                    val screen = screenService.getScreen(client!!, call.parameters["categoryId"]!!.toInt())
                    if (screen != null)
                        call.respond(screen)
                    else
                        call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}