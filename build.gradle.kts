import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "8.1.1"
  id("io.freefair.lombok") version "8.4"
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.5.0"
val junitJupiterVersion = "5.9.1"
val log4jVersion = "2.22.0"

val mainVerticleName = "com.example.starter.VerticleInitializer"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web:${vertxVersion}")
  implementation("io.vertx:vertx-auth-jwt:${vertxVersion}")
  implementation("org.apache.logging.log4j:log4j-api:${log4jVersion}")
  implementation("org.apache.logging.log4j:log4j-slf4j-impl:${log4jVersion}")
  implementation("org.apache.logging.log4j:log4j-core:${log4jVersion}")
  implementation("org.slf4j:slf4j-simple:2.0.5")
  implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
  implementation("io.vertx:vertx-mongo-client:${vertxVersion}")
  implementation("io.vertx:vertx-web-validation:${vertxVersion}")
  implementation("io.vertx:vertx-json-schema:${vertxVersion}")
  implementation("io.vertx:vertx-auth-common:${vertxVersion}")
  implementation("org.apache.commons:commons-lang3:3.14.0")
  implementation("io.vertx:vertx-config:${vertxVersion}")


  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
