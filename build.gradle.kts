plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.1"
}

group = "Permanager"
version = "1.0-SNAPSHOT"
val jdaVersion = "5.2.2"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("net.dv8tion:JDA:$jdaVersion") // Взаимодействие с Discord
    implementation("org.reflections:reflections:0.10.2") // Reflections (Для получения классов с пакетов)
    implementation("ch.qos.logback:logback-classic:1.5.6") // Зависимость Reflections
    implementation("com.googlecode.json-simple:json-simple:1.1.1") // Взаимодействие с JSON файлами
    implementation("org.postgresql:postgresql:42.2.10") // База данных PostgreSQL
}

tasks.test {
    useJUnitPlatform()
}