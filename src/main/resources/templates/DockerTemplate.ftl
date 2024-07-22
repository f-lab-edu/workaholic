FROM openjdk:${javaVersion}
WORKDIR ${workDir}
ARG JAR_FILE=build/libs/*.jar
<#list envVariables?keys as key>
    ENV ${key}=${envVariables[key]}
</#list>
COPY target/${jarFile} ${workDir}/${jarFile}
ENTRYPOINT ["java", "-jar", "${workDir}/${jarFile}"]