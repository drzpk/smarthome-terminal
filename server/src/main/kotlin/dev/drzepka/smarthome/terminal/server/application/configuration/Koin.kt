package dev.drzepka.smarthome.terminal.server.application.configuration

import dev.drzepka.smarthome.terminal.server.application.converter.CategoryEntityToModelConverter
import dev.drzepka.smarthome.terminal.server.application.converter.CategoryModelToEntityConverter
import dev.drzepka.smarthome.terminal.server.application.converter.ClientEntityToModelConverter
import dev.drzepka.smarthome.terminal.server.application.converter.element.ScreenConverter
import dev.drzepka.smarthome.terminal.server.application.converter.element.property.simple.IntPropertyConverter
import dev.drzepka.smarthome.terminal.server.application.converter.element.property.simple.StringPropertyConverter
import dev.drzepka.smarthome.terminal.server.application.service.ClientIdentityService
import dev.drzepka.smarthome.terminal.server.domain.converter.ConversionService
import dev.drzepka.smarthome.terminal.server.domain.repository.ClientRepository
import dev.drzepka.smarthome.terminal.server.domain.repository.ScreenRepository
import dev.drzepka.smarthome.terminal.server.domain.service.*
import dev.drzepka.smarthome.terminal.server.domain.service.client.ClientService
import dev.drzepka.smarthome.terminal.server.domain.service.client.ClientServiceImpl
import dev.drzepka.smarthome.terminal.server.domain.service.screen.ScreenService
import dev.drzepka.smarthome.terminal.server.infrastructure.repository.InMemoryClientRepository
import dev.drzepka.smarthome.terminal.server.infrastructure.repository.InMemoryScreenRepository
import dev.drzepka.smarthome.terminal.server.infrastructure.service.*
import org.koin.dsl.module

val koinModule = module {
    // Application service
    single { ClientIdentityService(get(), get()) }

    // Domain Service
    single<ClientService> { ClientServiceImpl(get(), get(), get(), get(), get()) }
    single { ScreenService(get(), get(), get(), get()) }

    // Infrastructure service
    single { ClientAuthenticationService(get()) }
    single<EventService> { EventServiceImpl() }

    // Components
    single<TerminalQueue.Handler> { QueueHandler() }
    single<Scheduler> { SchedulerImpl() }

    // Conversion
    single {
        val conversionService = ConversionService()
        conversionService.addConverter(CategoryModelToEntityConverter())
        conversionService.addConverter(CategoryEntityToModelConverter())
        conversionService.addConverter(ClientEntityToModelConverter())
        conversionService.addConverter(ScreenConverter(conversionService))
        conversionService.addConverter(IntPropertyConverter())
        conversionService.addConverter(StringPropertyConverter())
        conversionService
    }

    // Repository
    single<ClientRepository> { InMemoryClientRepository() }
    single<ScreenRepository> { InMemoryScreenRepository() }

    // Infrastructure
    single<TerminalQueue> { TerminalQueueImpl(get()) }
    single<EnvironmentService> { KtorEnvironmentService() }
}

