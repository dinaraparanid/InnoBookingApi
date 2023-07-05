val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.8.22"
    id("io.ktor.plugin") version "2.3.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.22"
}

group = "com.paranid5.innobookingfakeapi"
version = "0.0.1"

val mainClassTitle = "com.paranid5.innobookingfakeapi.ApplicationKt"

application {
    mainClass.set(mainClassTitle)

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-compression-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("org.jetbrains.exposed", "exposed-core", "0.40.1")
    implementation("org.jetbrains.exposed", "exposed-dao", "0.40.1")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.40.1")
    implementation("org.jetbrains.exposed", "exposed-java-time", "0.40.1")

    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("org.xerial:sqlite-jdbc:3.42.0.0")
}

tasks.wrapper {
    gradleVersion = "8.2"
    distributionType = Wrapper.DistributionType.ALL
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = mainClassTitle
    }

    configurations["compileClasspath"].forEach { from(zipTree(it.absoluteFile)) }
}

val fatJar = task("fatJar", type = Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveBaseName.set("${project.name}-fat")

    manifest {
        attributes["Implementation-Title"] = "Gradle Jar File Example"
        attributes["Implementation-Version"] = archiveVersion
        attributes["Main-Class"] = mainClassTitle
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "19"
    }

    "build" { dependsOn(fatJar) }
}