plugins {
	base
}

val npmInstall = tasks.register("npmInstall") {
	group = "build"
	doLast {
		logger.lifecycle("Skipping real npm install – placeholder front-end module.")
	}
}

tasks.register("npmBuild") {
	group = "build"
	dependsOn(npmInstall)
	doLast {
		logger.lifecycle("Skipping real npm build – placeholder front-end module.")
	}
}

tasks.register("npmStart") {
	group = "application"
	dependsOn(npmInstall)
	doLast {
		logger.lifecycle("Skipping real npm start – placeholder front-end module.")
	}
}

