import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  repositories {
    mavenCentral()
  }
}

plugins {
  id("org.springframework.boot") version "3.0.4" apply false
  id("io.spring.dependency-management") version "1.1.0" apply false
  kotlin("jvm") version "1.7.22"
  kotlin("kapt") version "1.7.22"
  kotlin("plugin.spring") version "1.7.22" apply false
}

allprojects {
  apply {
    plugin("org.springframework.boot")
    plugin("io.spring.dependency-management")
    plugin("kotlin")
    plugin("kotlin-kapt")
    plugin("kotlin-spring")
  }

  repositories {
    mavenCentral()
  }

  group = "com.kakaobank.homework"
  version = "0.0.1-SNAPSHOT"
  java.sourceCompatibility = JavaVersion.VERSION_17

  dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.+")

    kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=strict")
      jvmTarget = "17"
    }
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }
}
