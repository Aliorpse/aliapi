package tech.aliorpse.api.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped
import tech.aliorpse.api.controllers

val jobList = controllers.flatMap { it.jobList }

fun Application.configureScheduling() {
    monitor.subscribe(ApplicationStarted) {
        jobList.forEach { it.start() }
    }

    monitor.subscribe(ApplicationStopped) {
        jobList.forEach {
            it.cancel()
        }
    }
}
