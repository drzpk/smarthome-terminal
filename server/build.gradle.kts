plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(project(":common"))

    val ktorVersion = "1.4.1"

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")

    implementation("org.koin:koin-ktor:2.1.6")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("org.assertj:assertj-core:3.8.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.9")
    testImplementation("io.ktor:ktor-server-test-host:1.4.1")
    testImplementation("io.ktor:ktor-client-mock:1.4.1")
    testImplementation("org.koin:koin-test:2.1.6")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xuse-experimental=kotlin.Experimental"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
