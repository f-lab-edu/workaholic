plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
}

allprojects {
    group = "com.project"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    dependencies {
        //lombok
        "compileOnly"("org.projectlombok:lombok")
        "annotationProcessor"("org.projectlombok:lombok")

        //RabbitMQ + Spring Web
        "implementation"("org.springframework.boot:spring-boot-starter-amqp")
        "implementation"("org.springframework.boot:spring-boot-starter-web")

        "annotationProcessor"("jakarta.annotation:jakarta.annotation-api")
        "annotationProcessor"("jakarta.persistence:jakarta.persistence-api")


        "testCompileOnly"("org.projectlombok:lombok")
        "testAnnotationProcessor"("org.projectlombok:lombok")
        "testImplementation"("org.springframework.boot:spring-boot-starter-test")
        "testImplementation"("org.springframework.amqp:spring-rabbit-test")
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
    }
    tasks.named<Test>("test") {
        useJUnitPlatform()
    }
}

project(":gateway") {
    tasks {
        named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
            enabled = true
        }
        named<Jar>("jar") {
            enabled = true
        }
    }

    dependencies {
        implementation(project(":rabbitmq"))
        implementation(project(":datasource-work"))
        implementation(project(":datasource-transaction"))
    }
}

project(":vcs-integration") {
    tasks {
        named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
            enabled = true
        }
        named<Jar>("jar") {
            enabled = true
        }
    }

    dependencies {
        implementation(project(":rabbitmq"))
        implementation(project(":datasource-work"))
        implementation(project(":datasource-transaction"))
    }
}

project(":kubernetes") {
    tasks {
        named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
            enabled = true
        }
        named<Jar>("jar") {
            enabled = true
        }
    }

    dependencies {
        implementation(project(":rabbitmq"))
        implementation(project(":datasource-work"))
        implementation(project(":datasource-pod"))
        implementation(project(":datasource-transaction"))
    }
}