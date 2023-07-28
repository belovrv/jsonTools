plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.8.21"
    kotlin("jupyter.api") version "0.11.0-364"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.21")
    implementation("com.sealwu.jsontokotlin:library:3.7.4")
    implementation("org.jetbrains.kotlinx:kotlin-jupyter-lib:0.11.0-364")
    implementation("io.ktor:ktor-client-apache5-jvm:2.3.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}