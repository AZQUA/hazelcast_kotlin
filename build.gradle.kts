plugins {
	kotlin("jvm") version "1.9.25"
	id("application")
}

// Remplace "com.example.hazelcastproject.HazelcastProjectApplicationKt"
// par le vrai nom de ton fichier principal.
// Astuce : c'est souvent "NomDuFichierKt".
// Regarde dans src/main/kotlin/... pour trouver le nom de ton fichier .kt
application {
    mainClass.set("com.example.hazelcast_project.HazelcastProjectApplicationKt")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("com.hazelcast:hazelcast:5.3.6")
	implementation("org.jetbrains.kotlin:kotlin-stdlib")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
	implementation("com.h2database:h2:2.2.224")
}


tasks.withType<Test> {
	useJUnitPlatform()
}