plugins {
    application
    kotlin("jvm") version "1.3.61"
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

group = "com.justai.jaicf"
version = "1.0.0"

val jaicf = "0.4.0"
val slf4j = "1.7.30"
val ktor = "1.3.1"
val jUnit = "5.6.0"

application {
    mainClassName = "com.justai.jaicf.template.WebhookKt"
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.slf4j:slf4j-simple:$slf4j")
    implementation("org.slf4j:slf4j-log4j12:$slf4j")

    implementation("com.justai.jaicf:core:$jaicf")
    implementation("com.justai.jaicf:yandex-alice:$jaicf")
    implementation("com.justai.jaicf:mongo:$jaicf")

    implementation("io.ktor:ktor-server-netty:$ktor")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnit")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:$jUnit")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    test {
        useJUnitPlatform()
    }
}

tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to application.mainClassName
            )
        )
    }
}

tasks.create("stage") {
    dependsOn("shadowJar")
}
