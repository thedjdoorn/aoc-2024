plugins {
    kotlin("jvm") version "2.1.0"
    application
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }
}

application {
    mainClass = providers.gradleProperty("day").map { "Day${it}Kt" }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}