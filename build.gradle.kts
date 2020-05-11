plugins {
    kotlin("multiplatform") version "1.3.72"
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

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
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
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation(npm("react", "16.13.1"))
                implementation(npm("react-dom", "16.13.1"))
                implementation(npm("is-sorted"))
                implementation(npm("inline-style-prefixer", "^6.0.0"))
                implementation("org.jetbrains:kotlin-css:1.0.0-pre.104-kotlin-1.3.72")
                implementation("org.jetbrains:kotlin-styled:1.0.0-pre.104-kotlin-1.3.72")
                implementation("org.jetbrains:kotlin-react:16.13.1-pre.104-kotlin-1.3.72")
                implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.104-kotlin-1.3.72")
                implementation("io.ktor:ktor-client-serialization-js:$ktorVersion")
                implementation("io.ktor:ktor-client-json-js:$ktorVersion")
                implementation("io.ktor:ktor-client-websockets-js:$ktorVersion")
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
