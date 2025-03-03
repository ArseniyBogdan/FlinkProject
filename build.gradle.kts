import com.github.jengelman.gradle.plugins.shadow.transformers.PropertiesFileTransformer

plugins {
    id("java")
    id("application")
    id("org.springframework.boot") version "2.7.7"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("com.gradleup.shadow") version "8.3.6"
    id("jacoco")
}

val jarBaseName = "flink_project"
group = "ru.flproject"
version = "1.0"

val flinkVersion = "1.20.1"
val jacksonVersion = "2.14.1"
val awaitilityVersion = "4.2.0"
val flinkKafkaConnectorVersion = "3.4.0-1.20"

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${jacksonVersion}")
    implementation("com.fasterxml.jackson.core:jackson-core:${jacksonVersion}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${jacksonVersion}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${jacksonVersion}")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    compileOnly ("org.projectlombok:lombok")

    implementation ("org.apache.flink:flink-java:${flinkVersion}")
    implementation ("org.apache.flink:flink-streaming-java:${flinkVersion}")
    implementation ("org.apache.flink:flink-connector-datagen:${flinkVersion}")
    implementation ("org.apache.flink:flink-connector-base:${flinkVersion}")
    implementation ("org.apache.flink:flink-connector-kafka:${flinkKafkaConnectorVersion}")
    implementation ("org.apache.flink:flink-clients:${flinkVersion}")

    testImplementation ("ch.qos.logback:logback-classic:1.4.12")
    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    testImplementation ("org.apache.flink:flink-streaming-java:${flinkVersion}:tests")
    testImplementation ("org.apache.flink:flink-test-utils:${flinkVersion}")
    testImplementation ("org.apache.flink:flink-statebackend-rocksdb:${flinkVersion}")
    testImplementation ("org.testcontainers:junit-jupiter:1.18.0")
    testImplementation ("org.awaitility:awaitility:${awaitilityVersion}")
    testImplementation ("commons-io:commons-io:2.14.0")

    testAnnotationProcessor ("org.projectlombok:lombok")
    testCompileOnly ("org.projectlombok:lombok")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    archiveBaseName.set(jarBaseName)
}

application.mainClass = "ru.flproject.Main"

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    dependsOn("check")
    isZip64 = true
    mergeServiceFiles()
    archiveClassifier.set("")
    append("META-INF/spring.handlers")
    append("META-INF/spring.schemas")
    append("META-INF/spring.tooling")
    transform(PropertiesFileTransformer::class.java) {
        paths = listOf("META-INF/spring.factories")
        mergeStrategy = "append"
    }
    manifest {
        attributes["Main-Class"] = "ru.flproject.Main"
    }
}