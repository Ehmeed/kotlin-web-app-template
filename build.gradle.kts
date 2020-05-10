plugins {
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
    jvm()
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
                implementation("org.jetbrains:kotlin-styled:1.0.0-pre.104-kotlin-1.3.72")
                implementation("org.jetbrains:kotlin-react:16.13.1-pre.104-kotlin-1.3.72")
                implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.104-kotlin-1.3.72")
                implementation(npm("react", "16.13.1"))
                implementation(npm("react-dom", "16.13.1"))
                implementation(npm("is-sorted"))
                implementation(npm("inline-style-prefixer", "^6.0.0"))
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
