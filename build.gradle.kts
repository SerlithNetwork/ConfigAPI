import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.nio.charset.StandardCharsets
import kotlin.io.path.Path

plugins {
    java
    `maven-publish`
    `java-library`
    id("io.freefair.lombok") version "8.12.1"
    id("com.gradleup.shadow") version "9.0.0-beta8"
}

object Project {
    const val NAME = "ConfigAPI"
    const val GROUP = "net.j4c0b3y"
    const val VERSION = "1.2.5"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "io.freefair.lombok")
    apply(plugin = "maven-publish")
    apply(plugin = "com.gradleup.shadow")


    if (project.name == "paper") {
        java {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
            withSourcesJar()
        }
    } else {
        java {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
            withSourcesJar()
        }
    }

    tasks {
        compileJava {
            options.encoding = StandardCharsets.UTF_8.name()
        }

        register<Copy>("copy") {
            from(shadowJar)
            rename("(.*)-all.jar", "${Project.NAME}-${this@subprojects.name}-${Project.VERSION}.jar")
            into(Path(rootDir.path, "jars"))
        }

        shadowJar {
            exclude("org/jetbrains/**")
            exclude("org/intellij/**")
        }

        build { dependsOn(named("copy")) }
    }

    publishing {
        repositories {
            maven("https://repo.j4c0b3y.net/public/") {
                name = "j4c0b3yPublic"

                credentials(PasswordCredentials::class)

                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
        }

        publications {
            create<MavenPublication>(name) {
                artifactId = Project.NAME + "-" + name
                groupId = Project.GROUP
                version = Project.VERSION

                artifact(tasks.named<ShadowJar>("shadowJar").get().archiveFile)

                artifact(tasks.named<Jar>("sourcesJar").get().archiveFile) {
                    classifier = "sources"
                }
            }
        }
    }
}


