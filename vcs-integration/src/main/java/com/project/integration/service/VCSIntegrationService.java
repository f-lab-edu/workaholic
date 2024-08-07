package com.project.integration.service;

import com.project.config.VCSProperties;
import com.project.exception.type.FailedCloneRepositoryException;
import com.project.exception.type.FailedCreateDirectory;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class VCSIntegrationService {
    private final VCSProperties properties;

    public VCSIntegrationService(VCSProperties properties) {
        this.properties = properties;
    }

    private boolean createDirectory(File directory) {
        try {
            boolean isCreated = directory.mkdirs();
            if (isCreated) {
                log.info("Directory {} created successfully.", directory.getAbsolutePath());
            } else {
                log.error("Failed to create directory {}", directory.getAbsolutePath());
            }
            return isCreated;
        } catch (SecurityException e) {
            log.error("Failed to create directory {} due to security exception", directory.getAbsolutePath(), e);
            return false;
        }
    }

    private void emptyDirectory(File directory) throws IOException {
        Path dirPath = Paths.get(directory.getAbsolutePath());
        Files.walk(dirPath)
                .map(Path::toFile)
                .forEach(file -> {
                    if (!file.delete()) {
                        log.warn("Failed to delete file: {}", file.getAbsolutePath());
                    }
                });
    }

    public String cloneRepository(String projectId, String cloneUrl, String token) throws FailedCloneRepositoryException {
        File directory = new File(properties.getBASE_URL(), projectId);
        if (!directory.exists()) {
            boolean isCreated = createDirectory(directory);
            if (!isCreated) {
                throw new FailedCreateDirectory(directory.getAbsolutePath());
            }
        } else {
            log.warn("Directory {} already exists. It will be emptied before cloning.", directory.getAbsolutePath());
            try {
                emptyDirectory(directory);
            } catch (IOException e) {
                throw new FailedCreateDirectory("Failed to empty directory: " + directory.getAbsolutePath());
            }
        }

        try {
            log.info("Cloning repository from {} to {}", cloneUrl, directory.getAbsolutePath());
            Git.cloneRepository()
                    .setURI(cloneUrl)
                    .setDirectory(directory)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, ""))
                    .call();
            log.info("Repository cloned successfully to {}", directory.getAbsolutePath());
        } catch (GitAPIException | JGitInternalException e) {
            log.error("Failed to clone repository from {}", cloneUrl, e);
            throw new FailedCloneRepositoryException("Failed to clone repository from " + cloneUrl + ": " + e.getMessage());
        }

        return directory.getAbsolutePath();
    }
}
