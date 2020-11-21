package dev.drzepka.smarthome.terminal.common.transport.message

import dev.drzepka.smarthome.terminal.common.api.category.CategoryModel
import dev.drzepka.smarthome.terminal.common.transport.Side

class GetCategoriesMessage : Message<GetCategoriesMessageResponse>(Side.CLIENT, GetCategoriesMessageResponse::class)

class GetCategoriesMessageResponse : MessageResponse {
    @Suppress("unused")
    constructor() : super()
    constructor(message: GetCategoriesMessage) : super(message)

    var categories = ArrayList<CategoryModel>()
}
