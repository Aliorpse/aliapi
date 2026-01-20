plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinx.serialization)
    application
}

group = "tech.aliorpse.api"

repositories {
    mavenCentral()
    maven("https://mvn.vendeli.eu/re.this")
}

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

    implementation(libs.kotlinx.serialization)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.server.contentNegotiation)

    implementation(libs.ktor.clientCore)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.contentNegotiation)
}
