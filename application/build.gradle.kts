plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.10"
    id("org.beryx.jlink") version "2.24.1"
}

application {
    mainModule.set("md.vnastasi.slv.app")
    mainClass.set("md.vnastasi.slv.app.SaucelabsLogViewerApplication")
}

javafx {
    version = "17.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":service"))
    implementation("org.controlsfx:controlsfx:11.1.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

afterEvaluate {
    tasks.withType<JavaCompile> {
        doFirst {
            options.compilerArgs.addAll(listOf("--module-path", classpath.asPath))
            classpath = files()
        }
    }
}

tasks.jlink {
    extension.options.addAll((listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")))
    extension.imageName.set("SacelabsLogViewer")
    launcherData.apply {
        name = "Saucelabs-Log-Viewer"
    }
}

tasks.jlinkZip {
    group = "distribution"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("--enable-preview")
}

tasks.withType<Test> {
    jvmArgs?.add("--enable-preview")
    useJUnitPlatform()
}

tasks.withType<JavaExec> {
    jvmArgs?.add("--enable-preview")
}
