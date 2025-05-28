dependencies {
    implementation ("ch.qos.logback:logback-classic")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.glassfish:jakarta.json")

    testImplementation ("org.junit.jupiter:junit-jupiter-api")
    testImplementation ("org.junit.jupiter:junit-jupiter-engine")
    testImplementation ("org.assertj:assertj-core")
    testImplementation ("org.mockito:mockito-junit-jupiter")
}