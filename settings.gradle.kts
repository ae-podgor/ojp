rootProject.name = "ojp"
include("hw01-gradle")
include("hw04-generics")
include("hw06-annotations")
include("hw07-gc")
include("hw10-byteCodes")
include("hw12-solid")
include("hw15-patterns")
include("hw16-io")
include("hw18-jdbc")
include("hw18-jdbc:demo")
include("hw21-jpql")
include("hw22-cache")
include("hw24-webserver")
include("hw25-di")
include("hw28-springDataJdbc")
include("hw31-executors")
include("hw32-concurrentCollections")
include("hw34-multiprocess")
include("hw38-webflux")
include("hw38-webflux:client-service")
include("hw38-webflux:datastore-service")
include("realtime-order-analytics:producer")
include("realtime-order-analytics:streams")


pluginManagement {
    val dependencyManagement: String by settings
    val johnrengelmanShadow: String by settings
    val springframeworkBoot: String by settings
    val protobufVer: String by settings

    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("org.springframework.boot") version springframeworkBoot
        id("com.google.protobuf") version protobufVer
    }
}
