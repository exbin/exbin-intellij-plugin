plugins {
    id("java")
//    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.15.0"
}

group = "org.exbin.tool.intellij"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.2.1")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
//    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//        kotlinOptions.jvmTarget = "17"
//    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

val xbupLibraryVersion = "0.2.2"
val xbupDataLibraryVersion = "0.2.2"
val exbinFrameworkLibraryVersion = "0.3.0-SNAPSHOT"
val binedLibraryVersion = "0.2.1"
val binaryDataLibraryVersion = "0.2.1"

fun xbupLibrary(libName: String): String = if (libName.endsWith("-SNAPSHOT")) ":${libName}-${xbupLibraryVersion}" else "org.exbin.xbup:${libName}:${xbupLibraryVersion}"
fun xbupDataLibrary(libName: String): String = if (libName.endsWith("-SNAPSHOT")) ":${libName}-${xbupDataLibraryVersion}" else "org.exbin.xbup.data:${libName}:${xbupDataLibraryVersion}"
fun exbinFrameworkLibrary(libName: String): String = if (libName.endsWith("-SNAPSHOT")) ":${libName}-${exbinFrameworkLibraryVersion}" else "org.exbin.xbup.data:${libName}:${exbinFrameworkLibraryVersion}"
fun binedLibrary(libName: String): String = if (libName.endsWith("-SNAPSHOT")) ":${libName}-${binedLibraryVersion}" else "org.exbin.bined:${libName}:${binedLibraryVersion}"
fun binaryDataLibrary(libName: String): String = if (libName.endsWith("-SNAPSHOT")) ":${libName}-${binaryDataLibraryVersion}" else "org.exbin.auxiliary:${libName}:${binaryDataLibraryVersion}"

repositories {
    flatDir {
        dirs("lib")
    }
    mavenLocal()
}

dependencies {
    implementation(xbupLibrary("xbup-catalog"))
    implementation(xbupLibrary("xbup-client"))
    implementation(xbupLibrary("xbup-core"))
    implementation(xbupLibrary("xbup-operation"))
    implementation(xbupLibrary("xbup-parser-command"))
    implementation(xbupLibrary("xbup-parser-tree"))
    implementation(xbupLibrary("xbup-plugin"))
    implementation(xbupLibrary("xbup-service"))
    implementation(xbupDataLibrary("xbup-data-audio"))
    implementation(xbupDataLibrary("xbup-data-visual"))
    implementation(exbinFrameworkLibrary("exbin-framework"))
    implementation(binedLibrary("bined-core"))
    implementation(binedLibrary("bined-extended"))
    implementation(binedLibrary("bined-highlight-swing"))
    implementation(binedLibrary("bined-operation"))
    implementation(binedLibrary("bined-operation-swing"))
    implementation(binedLibrary("bined-swing"))
    implementation(binedLibrary("bined-swing-extended"))
    implementation(binaryDataLibrary("binary_data"))
    implementation(binaryDataLibrary("binary_data-paged"))
    implementation(binaryDataLibrary("binary_data-delta"))
    compileOnly(":jsr305-2.0.1")
}
