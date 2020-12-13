package dev.drzepka.smarthome.terminal.server.application.converter

import dev.drzepka.smarthome.terminal.common.api.category.CategoryModel
import dev.drzepka.smarthome.terminal.server.domain.converter.Converter
import dev.drzepka.smarthome.terminal.server.domain.value.Category

class CategoryEntityToModelConverter : Converter<Category, CategoryModel> {

    override fun convert(source: Category): CategoryModel? {
        return CategoryModel(source.id, source.name, source.description)
    }

}