package com.project.workaholic.vcs.service;

import com.project.workaholic.project.model.entity.WorkProject;
import com.project.workaholic.vcs.model.entity.OAuthAccessToken;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import com.project.workaholic.vcs.repository.OAuthAccessTokenRepository;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class VCSApiService {
    private static final String BASE_DIR = "src/main/resources/repository";
    private final VendorManager vendorManager;
    private final OAuthAccessTokenRepository tokenRepository;

    public VCSApiService(VendorManager vendorManager, OAuthAccessTokenRepository tokenRepository) {
        this.vendorManager = vendorManager;
        this.tokenRepository = tokenRepository;
    }

    private VendorApiService getMatchServiceByWorkProject(WorkProject workProject) {
        return vendorManager.getService(workProject.getVendor());
    }

    public VendorApiService getMatchServiceByVendor(VCSVendor vendor) {
        return vendorManager.getService(vendor);
    }

    public void registerToken(String accountId, String token, VCSVendor vendor) {
        OAuthAccessToken oAuthAccessToken = new OAuthAccessToken(accountId, vendor, token);
        tokenRepository.save(oAuthAccessToken);
    }

    public void renewAccessToken(OAuthAccessToken existingToken, String token) {
        existingToken.setToken(token);
        tokenRepository.save(existingToken);
    }

    public OAuthAccessToken getOAuthAccessTokenByAccountId(VCSVendor vcsVendor, String accountId) {
        VendorApiService service = getMatchServiceByVendor(vcsVendor);
        return service.getOAuthAccessTokenByAccountId(accountId);
    }

    private void makeDirectories(Path outputPath) {
        try {
            Files.createDirectories(outputPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void cloneRepository(String cloneUrl, String accessToken, String directoryName) {
        File directory = new File(BASE_DIR, directoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try{
            Git.cloneRepository().setURI(cloneUrl)
                    .setDirectory(directory)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(accessToken, ""))
                    .call();
        } catch (InvalidRemoteException e) {
            throw new RuntimeException(e);
        } catch (TransportException e) {
            throw new RuntimeException(e);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }
}
