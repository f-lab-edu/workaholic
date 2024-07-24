package com.project.workaholic.deploy.service;

import com.project.workaholic.config.exception.type.FailedBuildDockerFileException;
import com.project.workaholic.config.exception.type.FailedCreateDockerFile;
import com.project.workaholic.config.exception.type.NotSetTemplateModelException;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class DockerService {
    private static final String TEMPLATE_PATH = "/templates";
    private static final String DOCKER_TEMPLATE_FILE = "DockerTemplate.ftl";
    private static final String DOCKER_FILE_NAME = "Dockerfile";

    private Configuration configuration;
    private Template template;

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

    public DockerService() {
        initConfiguration();
        loadDockerFileTemplate();
    }

    private String renderTemplate(Map<String, Object> model) {
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

    public String generateDockerFile(String workDirectoryPath, Map<String, Object> model) {
        if( model == null)
            throw new NotSetTemplateModelException();

        Path dockerFilePath = Paths.get(workDirectoryPath);
        if(!Files.exists(dockerFilePath))
            makeDirectories(dockerFilePath);

        String dockerFileContent = renderTemplate(model);
        Path dockerFile = dockerFilePath.resolve(DOCKER_FILE_NAME);
        try {
            Files.writeString(dockerFile, dockerFileContent);
        } catch (IOException e) {
            throw new FailedCreateDockerFile();
        }

        return dockerFilePath.toFile().getAbsolutePath();
    }

    public void buildDockerImage(String imageName, String dockerfilePath) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("docker", "build", dockerfilePath, "--tag", imageName);

        try {
            Process process = processBuilder.start();

            // 표준 출력과 오류를 읽기 위한 BufferedReader
            BufferedReader stdOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            StringBuilder output = new StringBuilder();
            StringBuilder error = new StringBuilder();
            String line;

            // 표준 출력을 읽고 저장
            while ((line = stdOutput.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 표준 오류를 읽고 저장
            while ((line = stdError.readLine()) != null) {
                error.append(line).append("\n");
            }


            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Docker build failed with exit code " + exitCode);
                System.err.println("Error output:\n" + error.toString());
                throw new FailedBuildDockerFileException();
            }
        } catch (IOException | InterruptedException e) {
            throw new FailedBuildDockerFileException();
        }
    }
}
