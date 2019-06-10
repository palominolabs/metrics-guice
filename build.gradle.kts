import java.util.Date

plugins {
    `java-library`
    `maven-publish`
    id("osgi")
    id("com.jfrog.bintray") version "1.8.4"
    id("net.researchgate.release") version "2.8.0"
    id("com.github.ben-manes.versions") version "0.21.0"
    id("ru.vyarus.animalsniffer") version "1.5.0"
}

repositories {
    jcenter()
}

group = "com.palominolabs.metrics"

val deps by extra {
    mapOf(
            "metrics" to "4.1.0",
            "slf4j" to "1.7.26",
            "guice" to "4.2.2")
}

dependencies {
    implementation("io.dropwizard.metrics:metrics-core:${deps["metrics"]}")
    implementation("io.dropwizard.metrics:metrics-annotation:${deps["metrics"]}")
    implementation("com.google.inject:guice:${deps["guice"]}")
    implementation("com.google.code.findbugs:jsr305:3.0.2")

    testRuntime("org.slf4j:slf4j-simple:${deps["slf4j"]}")
    testRuntime("org.slf4j:jul-to-slf4j:${deps["slf4j"]}")
    testRuntime("org.slf4j:log4j-over-slf4j:${deps["slf4j"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.4.2")
    testImplementation("org.hamcrest:hamcrest-all:1.3")

    signature("org.codehaus.mojo.signature:java18:1.0@signature")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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

    register<Jar>("sourceJar") {
        from(project.the<SourceSetContainer>()["main"].allJava)
        archiveClassifier.set("sources")
    }

    register<Jar>("docJar") {
        from(project.tasks["javadoc"])
        archiveClassifier.set("javadoc")
    }

    afterReleaseBuild {
        dependsOn(bintrayUpload)
    }
}

publishing {
    publications {
        register<MavenPublication>("bintray") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
            artifact(tasks["sourceJar"])
            artifact(tasks["docJar"])
        }
    }
}

bintray {
    user = rootProject.findProperty("bintrayUser")?.toString()
    key = rootProject.findProperty("bintrayApiKey")?.toString()
    setPublications("bintray")

    with(pkg) {
        repo = "maven"
        setLicenses("Copyfree")
        vcsUrl = "https://github.com/palominolabs/metrics-guice"
        name = "com.palominolabs.metrics:metrics-guice"

        with(version) {
            name = project.version.toString()
            released = Date().toString()
            vcsTag = "v" + project.version.toString()
        }
    }
}

release {
    tagTemplate = "v\$version"
}
