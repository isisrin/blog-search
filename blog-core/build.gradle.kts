plugins {
  id("com.google.osdetector") version "1.7.1"
  kotlin("plugin.jpa") version "1.7.22"
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-cache")
  implementation("com.github.ben-manes.caffeine:caffeine")
  runtimeOnly("com.h2database:h2")
  if (osdetector.classifier == "osx-aarch_64") {
    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.89.Final:${osdetector.classifier}")
  }
}
