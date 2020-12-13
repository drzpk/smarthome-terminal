package dev.drzepka.smarthome.terminal.common.api.category

import dev.drzepka.smarthome.terminal.common.api.ApiModel

class CategoryModel() : ApiModel {
    var id: Int = 0
    var name: String = ""
    var description: String? = null

    constructor(id: Int, name: String) : this() {
        this.id = id
        this.name = name
        this.description = null
    }

    constructor(id: Int, name: String, description: String?) : this() {
        this.id = id
        this.name = name
        this.description = description
    }
}