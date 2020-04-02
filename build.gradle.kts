import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("maven-publish")
}

fun KotlinDependencyHandler.coreLibs(platform: String) {
    api(asoftCore("klock", platform))
    api(asoftCore("persist", platform))
}

android {
    configureAndroid()
}

val platformAttribute = Attribute.of("tz.co.asoft.logging.platform", String::class.java)

fun KotlinJsTargetDsl.configureJs(platform: String) {
    attributes.attribute(platformAttribute, platform)
    compilations.all {
        kotlinOptions {
            metaInfo = true
            sourceMap = true
            moduleKind = "commonjs"
        }
    }
}

kotlin {
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

    js { configureJs("browser") }

    js("node") { configureJs("nodejs") }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                coreLibs("metadata")
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
                coreLibs("android")
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
                coreLibs("jvm")
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
                coreLibs("js")
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