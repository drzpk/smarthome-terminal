plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    api(project(":common"))
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("io.ktor:ktor-client-apache:1.4.1")
    implementation("io.ktor:ktor-client-jackson:1.4.1")

    testImplementation(project(":common"))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("org.assertj:assertj-core:3.8.0")
    testImplementation("io.ktor:ktor-client-mock:1.4.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.9")
    testImplementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
