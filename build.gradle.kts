import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES

plugins {
    id("io.spring.dependency-management")
    id("org.springframework.boot") apply false
}

allprojects {
    group = "ru.otus.homework"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    val guava: String by project
    val glassfishJson: String by project
    val reflections: String by project

    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        dependencies {
            imports{
                mavenBom(BOM_COORDINATES)
            }
            dependency("com.google.guava:guava:$guava")
            dependency("org.glassfish:jakarta.json:$glassfishJson")
            dependency("org.reflections:reflections:$reflections")
        }
    }
}

subprojects {
    plugins.apply(JavaPlugin::class.java)
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing"))
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging.showExceptions = true
        reports {
            junitXml.required.set(true)
            html.required.set(true)
        }
    }
}
