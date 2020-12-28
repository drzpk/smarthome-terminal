package dev.drzepka.smarthome.terminal.server.infrastructure.service

import com.typesafe.config.ConfigFactory
import io.ktor.util.*
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test

@KtorExperimentalAPI
class KtorEnvironmentServiceTest {

    @Test
    fun `should detect dev environment`() {
        val devConfigString = """
            ktor {
                environment = dev
            }
        """.trimIndent()
        val devConfig = ConfigFactory.parseString(devConfigString)
        then(KtorEnvironmentService(devConfig).isProd()).isFalse()

        val prodConfigString = """
            ktor {
                environment = something else
            }
        """.trimIndent()
        val prodConfig = ConfigFactory.parseString(prodConfigString)
        then(KtorEnvironmentService(prodConfig).isProd()).isTrue()
    }
}