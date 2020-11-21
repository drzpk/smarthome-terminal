plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.21" apply false
}

allprojects {
    group = "dev.drzepka.smarthome"
}

subprojects {
    repositories {
        jcenter()
        mavenLocal()
        mavenCentral()
        google()
    }
}