package dev.drzepka.smarthome.terminal.server.application.configuration

import dev.drzepka.smarthome.terminal.common.api.category.CategoryModel
import dev.drzepka.smarthome.terminal.common.api.screen.Screen
import dev.drzepka.smarthome.terminal.common.api.screen.element.property.simple.StringProperty
import dev.drzepka.smarthome.terminal.server.application.configuration.routes.clients
import dev.drzepka.smarthome.terminal.server.application.configuration.routes.terminal
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing

fun Application.setupRouting() {
    routing {
        route("/api") {

            route("/applications") {
                get("") {
                    call.respond("not implemented")
                }

                route("/{id}") {
                    get("") {
                        val appId = call.parameters["id"]!!
                    }

                    route("/categories") {
                        val exampleCategories = listOf(
                            CategoryModel(1, "category 1"),
                            CategoryModel(2, "category 2", description = "category description"),
                            CategoryModel(3, "category 2")
                        )

                        get("") {
                            call.respond(exampleCategories)
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

            clients()
            terminal()
        }

    }
}