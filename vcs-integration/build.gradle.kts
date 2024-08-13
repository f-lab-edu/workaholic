plugins {

}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

    //JGit
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.7.0.202309050840-r")
}