package vcs.integration.service;

import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.TrackingRefUpdate;
import vcs.integration.exception.type.FailedCheckoutRepositoryException;
import vcs.integration.exception.type.FailedCloneRepositoryException;
import vcs.integration.exception.type.FailedCreateDirectory;
import vcs.integration.config.VCSProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;
import vcs.integration.exception.type.FailedFetchRepositoryException;
import vcs.integration.exception.type.FailedGetBranchesException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public String cloneRepository(String projectId, String cloneUrl, String token) throws FailedCloneRepositoryException, FailedCreateDirectory {
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

    public List<String> getRepositoryBranches(String repoUrl) throws FailedGetBranchesException {
        List<String> branches = new ArrayList<>();

        try (Git git = Git.open(new File(repoUrl))) {
            List<Ref> refs = git.branchList()
                    .setListMode(ListBranchCommand.ListMode.REMOTE)
                    .call();
            for (Ref ref : refs) {
                branches.add(ref.getName());
            }
        } catch (IOException | GitAPIException e) {
            throw new FailedGetBranchesException();
        }
        return branches;
    }

    public void checkoutRepositoryByBranchName(String repoUrl, String branchName) throws FailedCheckoutRepositoryException {
        try (Git git = Git.open(new File(repoUrl))) {
            git.checkout()
                    .setName(branchName)
                    .call();
            log.info("Checked out branch : {}", branchName);
        } catch (IOException | GitAPIException e) {
            throw new FailedCheckoutRepositoryException();
        }
    }

    public void fetchRepository(String repoUrl, String token) throws FailedFetchRepositoryException {
        try (Git git = Git.open(new File(repoUrl))) {
            FetchResult result = git.fetch()
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, ""))
                    .call();

            printFetchResult(result);
        } catch (IOException | GitAPIException  e) {
            throw new FailedFetchRepositoryException();
        }
    }

    private void printFetchResult(FetchResult result) {
        // Fetch 결과 메시지 출력
        System.out.println("Messages: " + result.getMessages());

        // 원격 참조 목록 출력
        System.out.println("Advertised Refs:");
        result.getAdvertisedRefs().forEach(ref ->
                System.out.println(" - " + ref.getName() + " (" + ref.getObjectId().getName() + ")")
        );

        // 업데이트된 참조 목록 출력
        System.out.println("Tracking Ref Updates:");
        Collection<TrackingRefUpdate> refUpdates = result.getTrackingRefUpdates();
        for (TrackingRefUpdate refUpdate : refUpdates) {
            System.out.println(" - " + refUpdate.getLocalName() + " -> " + refUpdate.getRemoteName());
            System.out.println("   Old Object ID: " + refUpdate.getOldObjectId().getName());
            System.out.println("   New Object ID: " + refUpdate.getNewObjectId().getName());
            System.out.println("   Result: " + refUpdate.getResult());
        }
    }
}
