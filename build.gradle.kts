plugins {
	kotlin("jvm") version "2.2.21" apply false
	kotlin("plugin.spring") version "2.2.21" apply false
	kotlin("plugin.jpa") version "2.2.21" apply false
	id("org.springframework.boot") version "3.5.7" apply false
	id("io.spring.dependency-management") version "1.1.7" apply false
}

group = "su.myexe"
version = "0.0.1"
description = "Task Manager"

allprojects {
	group = rootProject.group
	version = rootProject.version

	repositories {
		mavenCentral()
	}
}

tasks.register("buildAll") {
	group = "build"
	dependsOn(":back:build", ":front:npmBuild")
}

tasks.register("runBack") {
	group = "application"
	dependsOn(":back:bootRun")
}

tasks.register("runFront") {
	group = "application"
	dependsOn(":front:npmStart")
}
