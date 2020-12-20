package dev.drzepka.smarthome.terminal.server.application.configuration

import dev.drzepka.smarthome.terminal.server.application.converter.CategoryEntityToModelConverter
import dev.drzepka.smarthome.terminal.server.application.converter.CategoryModelToEntityConverter
import dev.drzepka.smarthome.terminal.server.application.converter.ClientEntityToModelConverter
import dev.drzepka.smarthome.terminal.server.application.converter.element.IntPropertyConverter
import dev.drzepka.smarthome.terminal.server.application.converter.element.StringPropertyConverter
import dev.drzepka.smarthome.terminal.server.domain.converter.ConversionService
import dev.drzepka.smarthome.terminal.server.domain.repository.ClientRepository
import dev.drzepka.smarthome.terminal.server.domain.service.*
import dev.drzepka.smarthome.terminal.server.infrastructure.repository.InMemoryClientRepository
import dev.drzepka.smarthome.terminal.server.infrastructure.service.InterfaceConfigurer
import dev.drzepka.smarthome.terminal.server.infrastructure.service.SchedulerImpl
import dev.drzepka.smarthome.terminal.server.infrastructure.service.TerminalQueueImpl
import org.koin.dsl.module

val koinModule = module {
    // Service
    single { ClientService(get(), get(), get(), get()) }
    single { ScreenService(get()) }

    // Components
    single<TerminalQueue.Handler> { QueueHandler() }
    single<Scheduler> { SchedulerImpl() }

    // Conversion
    single {
        val conversionService = ConversionService()
        conversionService.addConverter(CategoryModelToEntityConverter())
        conversionService.addConverter(CategoryEntityToModelConverter())
        conversionService.addConverter(ClientEntityToModelConverter())
        conversionService.addConverter(IntPropertyConverter())
        conversionService.addConverter(StringPropertyConverter())
        conversionService
    }

    // Repository
    single<ClientRepository> { InMemoryClientRepository() }

    // Infrastructure
    single<TerminalQueue> { TerminalQueueImpl(get()) }
    single { InterfaceConfigurer(get(), get()) }
}

