import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget

plugins {
    kotlin("multiplatform") version "1.3.70"
    kotlin("plugin.serialization") version "1.3.70"
    id("com.android.library") version "3.6.0"
    id("maven-publish")
}

object versions {
    object asoft {
        val test = "4.2.1"
        val persist = "16.0.0"
        val klock = "1.0.0"
    }

    object kotlinx {
        val serialization = "0.20.0"
        val coroutines = "1.3.4"
    }
}

fun andylamax(lib: String, platform: String, ver: String): String {
    return "com.github.andylamax.$lib:$lib-$platform:$ver"
}

fun KotlinDependencyHandler.asoftLibs(platform: String) {
    api(andylamax("asoft-klock", platform, versions.asoft.klock))
    api(andylamax("asoft-persist", platform, versions.asoft.persist))
}

fun asoftTest(platform: String) = andylamax("asoft-test", platform, versions.asoft.test)

group = "tz.co.asoft"
version = "8.0.0"

repositories {
    google()
    jcenter()
    maven(url = "https://jitpack.io")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        minSdkVersion(1)
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    sourceSets {
        val main by getting {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            resources.srcDirs("src/androidMain/resources")
        }
    }

    lintOptions {

    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

kotlin {
    val platformAttribute = Attribute.of("tz.co.asoft.logging.platform", String::class.java)
    android {
        attributes.attribute(platformAttribute, "android")
        compilations.all {
            kotlinOptions { jvmTarget = "1.8" }
        }
        publishLibraryVariants("release")
    }

    jvm {
        attributes.attribute(platformAttribute, "jvm")
        compilations.all {
            kotlinOptions { jvmTarget = "1.8" }
        }
    }

    fun KotlinJsTarget.configureJs(platform: String) {
        attributes.attribute(platformAttribute, platform)
        compilations.all {
            kotlinOptions {
                metaInfo = true
                sourceMap = true
                moduleKind = "commonjs"
            }
        }
    }

    js { configureJs("browser") }

    js("node") { configureJs("node") }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:${versions.kotlinx.serialization}")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${versions.kotlinx.coroutines}")
                asoftLibs("metadata")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(asoftTest("metadata"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${versions.kotlinx.serialization}")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${versions.kotlinx.coroutines}")
                asoftLibs("android")
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(asoftTest("android"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${versions.kotlinx.serialization}")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${versions.kotlinx.coroutines}")
                asoftLibs("jvm")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(asoftTest("jvm"))
            }
        }

        val jsCommonMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(kotlin("stdlib-js"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:${versions.kotlinx.serialization}")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${versions.kotlinx.coroutines}")
                asoftLibs("js")
            }
        }

        val jsCommonTest by creating {
            dependsOn(jsCommonMain)
            dependencies {
                implementation(asoftTest("js"))
            }
        }

        val jsMain by getting {
            dependsOn(jsCommonMain)
        }

        val jsTest by getting {
            dependsOn(jsCommonTest)
        }

        val nodeMain by getting {
            dependsOn(jsCommonMain)
        }

        val nodeTest by getting {
            dependsOn(jsCommonTest)
        }
    }
}