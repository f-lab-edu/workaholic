package com.project.workaholic.deploy.service;

import com.project.workaholic.config.exception.type.NotSetTemplateModelException;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class DockerFileService {
    private static final String TEMPLATE_PATH = "/templates";
    private static final String OUTPUT_PATH = "src/main/resources/docker";
    private static final String DOCKER_TEMPLATE_FILE = "DockerTemplate.ftl";
    private static final String DOCKER_FILE_NAME = "DockerFile";

    private Configuration configuration;
    private Template template;

    @Setter
    private Map<String, Object> model = new HashMap<>();

    private void initConfiguration() {
        configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setNumberFormat("0.##########");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        configuration.setClassForTemplateLoading(this.getClass(), TEMPLATE_PATH);
    }

    private void loadDockerFileTemplate() {
        try {
            this.template = configuration.getTemplate(DOCKER_TEMPLATE_FILE);
        } catch (TemplateNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (MalformedTemplateNameException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DockerFileService() {
        initConfiguration();
        loadDockerFileTemplate();
    }

    private String renderTemplate() {
        try (StringWriter writer = new StringWriter()) {
            template.process(model, writer);
            return writer.toString();
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void makeDirectories(Path outputPath) {
        try {
            Files.createDirectories(outputPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateDockerFile(String name) {
        if( model == null)
            throw new NotSetTemplateModelException();

        Path dockerFilePath = Paths.get(OUTPUT_PATH);
        if(!Files.exists(dockerFilePath))
            makeDirectories(dockerFilePath);

        String dockerFileContent = renderTemplate();
        Path dockerFile = dockerFilePath.resolve(DOCKER_FILE_NAME);
        try {
            Files.write(dockerFile, dockerFileContent.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
