package dev.drzepka.smarthome.terminal.server.application.converter

import dev.drzepka.smarthome.terminal.common.api.category.CategoryModel
import dev.drzepka.smarthome.terminal.server.domain.converter.Converter
import dev.drzepka.smarthome.terminal.server.domain.entity.Category

class CategoryModelToEntityConverter : Converter<CategoryModel, Category> {

    override fun convert(source: CategoryModel): Category {
        return Category(source.id, source.name, source.description)
    }

}