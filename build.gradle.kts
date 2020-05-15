plugins {
    kotlin("multiplatform") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.72"
}

group = "com.ehmeed"
version = "1.0-SNAPSHOT"

val ktorVersion = "1.3.2"

repositories {
    jcenter()
    mavenCentral()
}
kotlin {
    js {
        browser {
            runTask {
                devServer = org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.DevServer(
                    port = 3000,
                    contentBase = listOf("$buildDir/processedResources/js/main")
                )
            }
        }
    }
    jvm()

    val serializationVersion = "0.20.0"

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-html-builder:$ktorVersion")
                implementation("io.ktor:ktor-serialization:$ktorVersion")
                implementation("io.ktor:ktor-websockets:$ktorVersion")
                implementation("ch.qos.logback:logback-classic:1.2.3")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation(npm("is-sorted"))
                implementation("org.jetbrains:kotlin-css:1.0.0-pre.104-kotlin-1.3.72")
                implementation("org.jetbrains:kotlin-css-js:1.0.0-pre.104-kotlin-1.3.72")
                implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.6.11")
                implementation("io.ktor:ktor-client-serialization-js:$ktorVersion")
                implementation("io.ktor:ktor-client-json-js:$ktorVersion")
                implementation("io.ktor:ktor-client-websockets-js:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:$serializationVersion")
                implementation(npm("text-encoding", "0.7.0"))
                implementation(npm("abort-controller", "3.0.0"))
                implementation(npm("utf-8-validate", "5.0.2"))
                implementation(npm("bufferutil", "4.0.1"))
                implementation(npm("fs", "0.0.2"))
            }
        }
    }
}
val jvmRun by tasks.creating(JavaExec::class) {
    group = "application"
    main = "com.ehmeed.jvm.MainJvmKt"
    kotlin {
        val main = targets["jvm"].compilations["main"]
        dependsOn(main.compileAllTaskName)
        classpath(
            { main.output.allOutputs.files },
            { configurations["jvmRuntimeClasspath"] }
        )
    }
    // disable app icon on macOS
    systemProperty("java.awt.headless", "true")
}

val a = 10
