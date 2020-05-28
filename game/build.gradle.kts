import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.72"
    jacoco
    application
    id ("org.openjfx.javafxplugin") version "0.0.8"
}

application {
    mainClassName = "com.github.wakingrufus.rpg.RpgGameKt"
}

repositories {
    mavenCentral()
    jcenter()
    maven {
        setUrl("https://nexus.gluonhq.com/nexus/content/repositories/releases/")
    }
}

dependencies {
    implementation("org.slf4j:slf4j-api:1.7.25")
    implementation("org.slf4j:slf4j-log4j12:1.7.25")
    implementation("com.github.almasb:fxgl:11.9")


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.22")
    testImplementation("org.assertj:assertj-core:3.11.1")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

javafx {
    version = "13.0.2"
    modules("javafx.controls", "javafx.fxml", "javafx.swing", "javafx.media")
}

val testTask = tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    dependsOn(testTask)
    reports {
        xml.isEnabled = true
    }
}

tasks.withType<KotlinCompile> {
 //   kotlinOptions.languageVersion = "1.4"
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
}

tasks.findByPath("build")?.dependsOn("jacocoTestReport")