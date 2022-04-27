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
