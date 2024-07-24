<#compress>
    FROM openjdk:${javaVersion}
    RUN ${buildCommand}
    ARG JAR_FILE_PATH=${jarFilePath}
    COPY ${"$"}{JAR_FILE_PATH} app.jar

    <#if envVariables?? && envVariables?size gt 0>
        <#list envVariables?keys as key>
            ENV ${key}=${envVariables[key]}
        </#list>
    </#if>

    ENTRYPOINT ["java", "-jar", "app.jar"<#if parameters?? && parameters?size gt 0>, <#list parameters as parameter>"${parameter}"<#if parameter_has_next>, </#if></#list></#if>]
</#compress>