buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            setUrl("https://plugins.gradle.org/m2")
        }
    }
}

allprojects {
    group = "md.vnastasi.saucelabs-log-viewer"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("--enable-preview")
}

tasks.withType<Test> {
    jvmArgs?.add("--enable-preview")
}

tasks.withType<JavaExec> {
    jvmArgs?.add("--enable-preview")
}
