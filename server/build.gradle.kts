plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinx.serialization)
    application
}

group = "tech.aliorpse.api"

application {
    mainClass.set("tech.aliorpse.api.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    // implementation(libs.reThis)
    implementation("eu.vendeli:rethis:dev-260119~1d084f0")
    implementation(libs.logback)
    implementation(libs.ksoup)
    implementation(libs.mcutils.serverStatus)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger)

    implementation(libs.kotlinx.serialization)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.server.callLogging)
    implementation(libs.ktor.server.statusPages)
    implementation(libs.ktor.server.rateLimit)
    implementation(libs.ktor.server.compression)
    implementation(libs.ktor.server.resources)
    implementation(libs.ktor.server.requestValidation)

    implementation(libs.ktor.clientCore)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.contentNegotiation)
}
