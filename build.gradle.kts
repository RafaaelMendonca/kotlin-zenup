plugins {
    kotlin("jvm") version "2.2.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Testes
    testImplementation(kotlin("test"))

    // Retrofit (client HTTP)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Converter JSON (Gson)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp client + logging
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Coroutines (executar chamadas assíncronas com suspend)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
