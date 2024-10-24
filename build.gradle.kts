plugins {
    id("java")
//    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.15.0"
}

group = "org.exbin.tool.intellij"
version = "0.3-SNAPSHOT"

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
        options.compilerArgs = options.compilerArgs + "-Xlint:unchecked" + "-Xlint:deprecation"
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
val xbupDataLibraryVersion = "0.3.0-SNAPSHOT"
val exbinFrameworkLibraryVersion = "0.3.0-SNAPSHOT"
val xbupToolsLibraryVersion = "0.3.0-SNAPSHOT"
val binedLibraryVersion = "0.3.0-SNAPSHOT"
val binaryDataLibraryVersion = "0.2.2-SNAPSHOT"
val binedAppLibraryVersion = "0.3.0-SNAPSHOT"

fun xbupLibrary(libName: String): String = if (xbupLibraryVersion.endsWith("-SNAPSHOT")) ":${libName}-${xbupLibraryVersion}" else "org.exbin.xbup:${libName}:${xbupLibraryVersion}"
fun xbupDataLibrary(libName: String): String = if (xbupDataLibraryVersion.endsWith("-SNAPSHOTX")) ":${libName}-${xbupDataLibraryVersion}" else "org.exbin.xbup.data:${libName}:${xbupDataLibraryVersion}"
fun exbinFrameworkLibrary(libName: String): String = if (exbinFrameworkLibraryVersion.endsWith("-SNAPSHOTX")) ":${libName}-${exbinFrameworkLibraryVersion}" else "org.exbin.framework:${libName}:${exbinFrameworkLibraryVersion}"
fun xbupToolsLibrary(libName: String): String = if (xbupToolsLibraryVersion.endsWith("-SNAPSHOTX")) ":${libName}-${xbupToolsLibraryVersion}" else "org.exbin.framework:${libName}:${xbupToolsLibraryVersion}"
fun binedLibrary(libName: String): String = if (binedLibraryVersion.endsWith("-SNAPSHOTX")) ":${libName}-${binedLibraryVersion}" else "org.exbin.bined:${libName}:${binedLibraryVersion}"
fun binedAppLibrary(libName: String): String = if (binedAppLibraryVersion.endsWith("-SNAPSHOTX")) ":${libName}-${binedAppLibraryVersion}" else "org.exbin.framework:${libName}:${binedAppLibraryVersion}"
fun binaryDataLibrary(libName: String): String = if (binaryDataLibraryVersion.endsWith("-SNAPSHOTX")) ":${libName}-${binaryDataLibraryVersion}" else "org.exbin.auxiliary:${libName}:${binaryDataLibraryVersion}"

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
    implementation(exbinFrameworkLibrary("exbin-framework-basic"))
    implementation(exbinFrameworkLibrary("exbin-framework-frame"))
    implementation(exbinFrameworkLibrary("exbin-framework-ui"))
    implementation(exbinFrameworkLibrary("exbin-framework-component"))
    implementation(exbinFrameworkLibrary("exbin-framework-data"))
    implementation(exbinFrameworkLibrary("exbin-framework-window"))
    implementation(exbinFrameworkLibrary("exbin-framework-action"))
    implementation(exbinFrameworkLibrary("exbin-framework-file"))
    implementation(exbinFrameworkLibrary("exbin-framework-editor"))
    implementation(exbinFrameworkLibrary("exbin-framework-about-api"))
    implementation(exbinFrameworkLibrary("exbin-framework-about"))
    implementation(exbinFrameworkLibrary("exbin-framework-operation-undo"))
    implementation(exbinFrameworkLibrary("exbin-framework-action-popup"))
    implementation(exbinFrameworkLibrary("exbin-framework-help"))
    implementation(exbinFrameworkLibrary("exbin-framework-help-online"))
    implementation(exbinFrameworkLibrary("exbin-framework-addon-update"))
    implementation(exbinFrameworkLibrary("exbin-framework-options"))
    implementation(exbinFrameworkLibrary("exbin-framework-options-api"))
    implementation(exbinFrameworkLibrary("exbin-framework-preferences-api"))
    implementation(exbinFrameworkLibrary("exbin-framework-preferences"))
    implementation(exbinFrameworkLibrary("exbin-framework-language-api"))
    implementation(exbinFrameworkLibrary("exbin-framework-language"))
    implementation(exbinFrameworkLibrary("exbin-framework-editor-text"))
    implementation(exbinFrameworkLibrary("exbin-framework-client"))
    implementation(xbupToolsLibrary("exbin-framework-xbup-catalog"))
    implementation(xbupToolsLibrary("exbin-framework-editor-xbup"))
    implementation(binedAppLibrary("exbin-framework-bined"))
    implementation(binedAppLibrary("exbin-framework-bined-inspector"))
    implementation(binedAppLibrary("exbin-framework-bined-objectdata"))
    implementation(binedLibrary("bined-core"))
    implementation(binedLibrary("bined-section"))
    implementation(binedLibrary("bined-highlight-swing"))
    implementation(binedLibrary("bined-operation"))
    implementation(binedLibrary("bined-operation-swing"))
    implementation(binedLibrary("bined-swing"))
    implementation(binedLibrary("bined-swing-section"))
    implementation(binaryDataLibrary("binary_data"))
    implementation(binaryDataLibrary("binary_data-paged"))
    implementation(binaryDataLibrary("binary_data-delta"))
    implementation("org.mariadb.jdbc:mariadb-java-client:2.6.2")
    implementation("org.eclipse.persistence:javax.persistence:2.2.1")
//    compileOnly(group = "com.google.code.findbugs", name = "jsr305", version = "3.0.2")
    implementation("org.eclipse.persistence:eclipselink:2.7.7")
    implementation(":flatlaf-desktop-3.2")
    compileOnly(":jsr305-2.0.1")
}
