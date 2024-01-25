plugins {
    id("java")
}

tasks.withType(Wrapper::class) {
    gradleVersion = "8.4"
}

group = "org.example"
version = "1.0-SNAPSHOT"

val allureVersion = "2.25.0"
val aspectJVersion = "1.9.21"
val kotlinVersion = "1.9.22"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

val agent: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = true
}

tasks.test {
    useTestNG()
    jvmArgs = listOf(
            "-javaagent:${agent.singleFile}"
    )
}

dependencies {
    agent("org.aspectj:aspectjweaver:$aspectJVersion")

    testImplementation("org.testng:testng:7.9.0")
    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    testImplementation("io.qameta.allure:allure-testng")
    testImplementation("org.slf4j:slf4j-simple:2.1.0-alpha1")
    implementation("org.seleniumhq.selenium:selenium-java:4.16.1")
    implementation("io.qameta.allure:allure-testng:2.25.0")

}

repositories {
    mavenCentral()
}