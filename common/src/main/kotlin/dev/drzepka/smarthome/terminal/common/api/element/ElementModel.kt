package dev.drzepka.smarthome.terminal.common.api.element

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.drzepka.smarthome.terminal.common.util.IdSpace

/**
 * Defines a screen element
 */
abstract class ElementModel(val elementType: String) {
    var id: Int = 0
    var width: Int = 100
        set(value) {
            if (value < 1 || value > 100)
                throw IllegalStateException("Element width must be between 1 and 100")
            field = value
        }

    @JsonIgnore
    var idSpace: IdSpace? = null

    var children = ArrayList<ElementModel>()

    open fun addChild(child: ElementModel) {
        if (idSpace != null)
            child.id = idSpace!!.nextId()
        child.idSpace = idSpace
        children.add(child)
    }
}