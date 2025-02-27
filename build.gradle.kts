plugins {
    id("java")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

val jarBaseName = "flink_project"
group = "ru.flproject"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.apache.flink:flink-java:1.20.1")
    implementation("org.apache.flink:flink-streaming-java:1.20.1")
    implementation("org.apache.flink:flink-connector-datagen:1.20.1")
    implementation("org.apache.flink:flink-connector-base:1.20.1")
    implementation("org.apache.flink:flink-clients:1.20.1")
    implementation("org.slf4j:slf4j-log4j12:2.0.7")
    implementation("log4j:log4j:1.2.17")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    archiveBaseName.set(jarBaseName)
}

tasks.register("fatJar", type = Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Implementation-Title"] = "Gradle Jar File"
        attributes["Main-Class"] = "ru.flproject.Main"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get())
}