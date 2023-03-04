import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

apply(plugin = "com.google.protobuf")

configurations.forEach {
    if (it.name.toLowerCase().contains("proto")) {
        it.attributes.attribute(
            Usage.USAGE_ATTRIBUTE,
            objects.named(Usage::class.java, "java-runtime")
        )
    }
}

dependencies {
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")

    // grpc
    api("com.google.protobuf:protobuf-java-util:3.21.7")
    api("io.grpc:grpc-kotlin-stub:1.3.0")
    api("io.grpc:grpc-protobuf:1.51.0")
    api("io.grpc:grpc-netty-shaded:1.51.0")
}

protobuf {
    generatedFilesBaseDir = "$projectDir/build/generated/source"
    protoc { artifact = "com.google.protobuf:protoc:3.21.7" }
    plugins {
        id("grpc") { artifact = "io.grpc:protoc-gen-grpc-java:1.49.2" }
        id("grpckt") { artifact = "io.grpc:protoc-gen-grpc-kotlin:1.3.0:jdk8@jar" }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.generateDescriptorSet = true
            it.descriptorSetOptions.includeSourceInfo = true
            it.descriptorSetOptions.includeImports = true
            it.descriptorSetOptions.path =
                "$buildDir/resources/META-INF/armeria/grpc/service-name.dsc"
        }
    }
}

sourceSets {
    main {
        java.srcDir("build/generated/source/main/grpckt")
        java.srcDir("build/generated/source/main/grpc")
        java.srcDir("build/generated/source/main/java")
    }
}
