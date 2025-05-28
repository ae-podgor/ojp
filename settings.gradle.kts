rootProject.name = "ojp"
include("hw01-gradle")
include("hw04-generics")
include("hw06-annotations")
include("hw07-gc")
include("hw10-byteCodes")
include("hw12-solid")
include("hw15-patterns")
include("hw16-io")
pluginManagement {
    val dependencyManagement: String by settings
    val johnrengelmanShadow: String by settings
    val springframeworkBoot: String by settings

    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("org.springframework.boot") version springframeworkBoot
    }
}
