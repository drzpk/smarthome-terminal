package dev.drzepka.smarthome.terminal.common.builder

import dev.drzepka.smarthome.terminal.common.api.element.property.simple.IntPropertyModel
import dev.drzepka.smarthome.terminal.common.api.element.property.simple.StringPropertyModel
import dev.drzepka.smarthome.terminal.common.util.IdSpace
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test

class PropertyBuilderTest {

    @Test
    fun `should build properties`() {
        val space = TestSpace()
        lateinit var trackedString: StringPropertyModel
        lateinit var trackedInt: IntPropertyModel

        val screen = screen(space) {
            trackedString = stringProperty { }
            trackedInt = intProperty { }
        }

        then(screen.children).hasSize(2)
        then(screen.children[0]).isSameAs(trackedString)
        then(screen.children[0].id).isEqualTo(1)
        then(screen.children[1]).isSameAs(trackedInt)
        then(screen.children[1].id).isEqualTo(2)
    }

    private class TestSpace : IdSpace {
        override var idSpaceState: Int = 0
    }
}