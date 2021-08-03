package dev.drzepka.smarthome.terminal.server.infrastructure.service

import dev.drzepka.smarthome.terminal.server.infrastructure.ShutdownAware
import io.ktor.application.*
import org.koin.core.context.KoinContext

// todo: this won't work, koin can't return all registered beans
// todo: try this: https://github.com/InsertKoinIO/koin/issues/146#issuecomment-477309412
class InterfaceConfigurer(private val koinContext: KoinContext, private val application: Application) {

    init {
        setupShutdownHooks()
    }

    private fun setupShutdownHooks() {
        val koin = koinContext.get()
        for (bean in koin.getAll<Any>()) {
            if (bean is ShutdownAware) {
                application.environment.monitor.subscribe(ApplicationStopping) {
                    bean.onShutdown()
                }
            }
        }
    }
}