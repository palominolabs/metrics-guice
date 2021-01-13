import java.util.Date

plugins {
    `java-library`
    `maven-publish`
    id("biz.aQute.bnd.builder") version "5.2.0"
    id("com.jfrog.bintray") version "1.8.5"
    id("net.researchgate.release") version "2.8.1"
    id("com.github.ben-manes.versions") version "0.36.0"
    id("ru.vyarus.animalsniffer") version "1.5.2"
}

repositories {
    jcenter()
}

group = "com.palominolabs.metrics"

val deps by extra {
    mapOf(
            "metrics" to "5.0.0",
            "slf4j" to "1.7.30",
            "guice" to "4.2.3")
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
