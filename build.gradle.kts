import java.time.Duration
import java.net.URI

plugins {
    `java-library`
    `maven-publish`
    signing
    id("biz.aQute.bnd.builder") version "5.3.0"
    id("net.researchgate.release") version "2.8.1"
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("com.github.ben-manes.versions") version "0.38.0"
    id("ru.vyarus.animalsniffer") version "1.5.3"
}

repositories {
    mavenCentral()
}

group = "com.palominolabs.metrics"

val deps by extra {
    mapOf(
        "metrics" to "5.0.0",
        "slf4j" to "1.7.30",
        "guice" to "4.2.3"
    )
}

dependencies {
    implementation("io.dropwizard.metrics5:metrics-core:${deps["metrics"]}")
    implementation("io.dropwizard.metrics5:metrics-annotation:${deps["metrics"]}")
    implementation("com.google.inject:guice:${deps["guice"]}")
    implementation("com.google.code.findbugs:jsr305:3.0.2")

    testRuntimeOnly("org.slf4j:slf4j-simple:${deps["slf4j"]}")
    testRuntimeOnly("org.slf4j:jul-to-slf4j:${deps["slf4j"]}")
    testRuntimeOnly("org.slf4j:log4j-over-slf4j:${deps["slf4j"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("org.hamcrest:hamcrest-all:1.3")

    signature("org.codehaus.mojo.signature:java18:1.0@signature")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:unchecked")
    options.isDeprecation = true
    options.encoding = "UTF-8"
}

tasks {
    test {
        useJUnitPlatform()
    }

    afterReleaseBuild {
        dependsOn(provider { project.tasks.named("publishToSonatype") })
    }
}

publishing {
    publications {
        register<MavenPublication>("sonatype") {
            from(components["java"])
            // sonatype required pom elements
            pom {
                name.set("${project.group}:${project.name}")
                description.set(name)
                url.set("https://github.com/palominolabs/metrics-guice")
                licenses {
                    license {
                        name.set("Copyfree Open Innovation License 0.4")
                        url.set("https://copyfree.org/content/standard/licenses/coil/license.txt")
                    }
                }
                developers {
                    developer {
                        id.set("marshallpierce")
                        name.set("Marshall Pierce")
                        email.set("575695+marshallpierce@users.noreply.github.com")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/palominolabs/metrics-guice")
                    developerConnection.set("scm:git:ssh://git@github.com:palominolabs/metrics-guice.git")
                    url.set("https://github.com/palominolabs/metrics-guice")
                }
            }
        }
    }

    // A safe throw-away place to publish to:
    // ./gradlew publishSonatypePublicationToLocalDebugRepository -Pversion=foo
    repositories {
        maven {
            name = "localDebug"
            url = URI.create("file:///${project.buildDir}/repos/localDebug")
        }
    }
}

// don't barf for devs without signing set up
if (project.hasProperty("signing.keyId")) {
    signing {
        sign(project.extensions.getByType<PublishingExtension>().publications["sonatype"])
    }
}

nexusPublishing {
    repositories {
        sonatype {
            // sonatypeUsername and sonatypePassword properties are used automatically
            stagingProfileId.set("26c8b7fff47581") // com.palominolabs
        }
    }
    // these are not strictly required. The default timeouts are set to 1 minute. But Sonatype can be really slow.
    // If you get the error "java.net.SocketTimeoutException: timeout", these lines will help.
    connectTimeout.set(Duration.ofMinutes(3))
    clientTimeout.set(Duration.ofMinutes(3))
}
