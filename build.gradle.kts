plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta13"
}

group = "io.github.lumine1909"
version = "beta-2.0"
description = "Plugin side features for Leaves server"

repositories {
    mavenCentral()
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://repo.leavesmc.org/snapshots/")
    maven("https://minevolt.net/repo/")
}

dependencies {
    compileOnly("org.leavesmc.leaves:leaves-api:1.21.8-R0.1-SNAPSHOT")
    compileOnly("fr.xephi:authme:5.6.1-SNAPSHOT")
    compileOnly("com.mojang:authlib:3.13.56")
    compileOnly(files("libs/leaves-server-1.21.8-R0.1-SNAPSHOT.jar"))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    shadowJar {
        archiveFileName.set("LeavesAddons-${version}-MC-1.21.8.jar")
        minimize()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
            "name" to project.name,
            "version" to project.version,
            "description" to project.description,
            "apiVersion" to "1.21"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
