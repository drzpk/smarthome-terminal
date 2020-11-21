package dev.drzepka.smarthome.terminal.common.api.category

import dev.drzepka.smarthome.terminal.common.api.ApiModel

open class CategoryModel : ApiModel {
    val id: Int
    val name: String
    val description: String?

    constructor(id: Int, name: String) {
        this.id = id
        this.name = name
        this.description = null
    }

    constructor(id: Int, name: String, description: String?) {
        this.id = id
        this.name = name
        this.description = description
    }
}