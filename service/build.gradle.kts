java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.0")
    implementation("org.jetbrains:annotations:20.1.0")

    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation("org.mockito:mockito-core:4.5.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("--enable-preview")
}

tasks.withType<Test> {
    jvmArgs = listOf("--enable-preview")
    useJUnitPlatform()
}

tasks.withType<JavaExec> {
    jvmArgs = listOf("--enable-preview")
}
