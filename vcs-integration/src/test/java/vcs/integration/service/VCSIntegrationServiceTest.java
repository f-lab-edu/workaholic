package vcs.integration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VCSIntegrationServiceTest {

    @InjectMocks
    private VCSIntegrationService vcsIntegrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRepositoryBranches() throws Exception {
        String repoPath = "C:\\Users\\Tmax\\Desktop\\temp\\testProject";
        List<String> expectedBranches = Arrays.asList("master", "develop", "feature-branch");

        // Act
        List<String> actualBranches = vcsIntegrationService.getRepositoryBranches(repoPath);

        System.out.println(actualBranches);
    }

    @Test
    void branchCheckout() throws IOException {
        String repoPath = "C:\\Users\\Tmax\\Desktop\\temp\\testProject";
        String branchName = "develop";

        vcsIntegrationService.checkoutRepositoryByBranchName(repoPath, branchName);
    }
}