plugins {
//    id("org.jetbrains.kotlin.js") version "1.3.72"
//    kotlin("jvm") version "1.3.72"
    kotlin("multiplatform") version "1.3.72"
}

group = "com.ehmeed"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    kotlin("stdlib-jdk8")
    kotlin("stdlib-js")
}

kotlin {
    jvm { }
    js {
        browser()
    }

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
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
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
