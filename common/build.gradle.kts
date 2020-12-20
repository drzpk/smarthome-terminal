plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.11.3")
    compileOnly("com.fasterxml.jackson.core:jackson-annotations:2.11.3")
    compileOnly("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.3")
    compileOnly("org.slf4j:slf4j-api:1.7.30")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.assertj:assertj-core:3.8.0")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.11.3")
    testImplementation("com.fasterxml.jackson.core:jackson-annotations:2.11.3")
    testImplementation("org.assertj:assertj-core:3.8.0")
    testImplementation("io.ktor:ktor-client-mock:1.4.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}