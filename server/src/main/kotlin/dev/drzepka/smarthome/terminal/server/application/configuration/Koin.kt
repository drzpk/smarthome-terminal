package dev.drzepka.smarthome.terminal.server.application.configuration

import dev.drzepka.smarthome.terminal.server.application.converter.CategoryModelToEntityConverter
import dev.drzepka.smarthome.terminal.server.domain.converter.ConversionService
import dev.drzepka.smarthome.terminal.server.domain.repository.ClientRepository
import dev.drzepka.smarthome.terminal.server.domain.service.ClientService
import dev.drzepka.smarthome.terminal.server.domain.service.QueueHandler
import dev.drzepka.smarthome.terminal.server.domain.service.Scheduler
import dev.drzepka.smarthome.terminal.server.domain.service.TerminalQueue
import dev.drzepka.smarthome.terminal.server.infrastructure.repository.InMemoryClientRepository
import dev.drzepka.smarthome.terminal.server.infrastructure.service.InterfaceConfigurer
import dev.drzepka.smarthome.terminal.server.infrastructure.service.SchedulerImpl
import dev.drzepka.smarthome.terminal.server.infrastructure.service.TerminalQueueImpl
import org.koin.dsl.module

val koinModule = module {
    single { ClientService(get(), get(), get(), get()) }
    single<TerminalQueue.Handler> { QueueHandler() }
    single<Scheduler> { SchedulerImpl() }

    // Conversion
    single {
        val conversionService = ConversionService()
        conversionService.addConverter(CategoryModelToEntityConverter())
        conversionService
    }

    // Repository
    single<ClientRepository> { InMemoryClientRepository() }

    // Infrastructure
    single<TerminalQueue> { TerminalQueueImpl(get()) }
    single { InterfaceConfigurer(get(), get()) }
}

