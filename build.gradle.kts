plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("kapt") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.apache.httpcomponents.client5:httpclient5:5.3.1")
	implementation("org.apache.httpcomponents.core5:httpcore5:5.2.4")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	implementation("org.json:json:20231013")
	implementation("com.nimbusds:nimbus-jose-jwt:10.4")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
	implementation(platform("software.amazon.awssdk:bom:2.25.20"))
	implementation("software.amazon.awssdk:sns:2.25.20")
	implementation("software.amazon.awssdk:s3:2.25.20")
	implementation("software.amazon.awssdk:url-connection-client:2.25.20")
	implementation("software.amazon.awssdk:aws-query-protocol:2.25.20")
	implementation("software.amazon.awssdk:aws-core:2.25.20")
	implementation("software.amazon.awssdk:sdk-core:2.25.20")
	implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
	implementation("com.google.auth:google-auth-library-oauth2-http:1.20.0")
	implementation("com.squareup.okhttp3:okhttp:4.12.0")
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	kapt("org.mapstruct:mapstruct-processor:1.5.5.Final")

	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("io.mockk:mockk:1.13.5")
	testImplementation("org.assertj:assertj-core:3.24.2")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

configurations.all {
	resolutionStrategy.eachDependency {
		if (requested.group == "software.amazon.awssdk" && requested.version != "2.25.20") {
			useVersion("2.25.20")
		}
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

configurations.all {
	resolutionStrategy {
		force("software.amazon.awssdk:sns:2.25.20")
		force("software.amazon.awssdk:aws-core:2.25.20")
		force("software.amazon.awssdk:aws-query-protocol:2.25.20")
	}
}
