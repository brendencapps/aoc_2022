import kotlinx.benchmark.gradle.*
import org.jetbrains.kotlin.allopen.gradle.*
import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.allopen") version "1.7.22"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.4"
    id("me.champeau.jmh") version "0.6.6"
}

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlinx")
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
            resources.setSrcDirs(listOf("resources"))
        }
        test {
            java.srcDirs("test")
            resources.setSrcDirs(listOf("resources"))

        }
    }

    wrapper {
        gradleVersion = "7.6"
    }
}

configure<AllOpenExtension> {
    annotation("org.openjdk.jmh.annotations.State")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.openjdk.jmh:jmh-core:1.36")
    implementation("org.openjdk.jmh:jmh-generator-annprocess:1.36")
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.6")
    testImplementation("junit:junit:4.13.2")
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

benchmark {
    configurations {
        named("main") {
            warmups = 1
            iterations = 5
            iterationTime = 5
            iterationTimeUnit = "sec"

        }
    }
    targets {
        register("main") {
            this as JvmBenchmarkTarget
            jmhVersion = "1.36"
        }
    }
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}
