package dev.drzepka.smarthome.terminal.server.infrastructure.service

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import dev.drzepka.smarthome.terminal.server.domain.service.EnvironmentService

class KtorEnvironmentService(private val applicationConfig: Config = ConfigFactory.load()) : EnvironmentService {

    private val devEnv by lazy { isDevEnv() }

    override fun isDev(): Boolean = devEnv

    private fun isDevEnv(): Boolean {
        val envType = applicationConfig.getString(ENV_TYPE_PROPERTY)
        return envType == ENV_DEV
    }

    companion object {
        private const val ENV_TYPE_PROPERTY = "ktor.environment"
        private const val ENV_DEV = "dev"
    }
}