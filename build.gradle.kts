plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "3.1.0"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
    id("com.gradleup.shadow") version "9.0.0"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://mvn.lumine.io/repository/maven-public/") {
        name = "lumine"
    }
}

dependencies {
    paperweight.paperDevBundle("26.2.build.+")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(26)
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }

    runServer {
        minecraftVersion("26.2")
        jvmArgs("-Xms2G", "-Xmx2G", "--enable-preview")
    }

    processResources {
        val props = mapOf("version" to version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    withType<JavaCompile>().configureEach {
        options.compilerArgs.add("--enable-preview")
    }
}