package com.project.oauth.service;

import com.project.exception.type.FailedCloneRepositoryException;
import com.project.exception.type.FailedCreateDirectory;
import com.project.oauth.model.entity.OAuthAccessToken;
import com.project.oauth.model.enumeration.VCSVendor;
import com.project.oauth.repository.OAuthAccessTokenRepository;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class OAuthApiService {
    private static final String BASE_DIR = "C:\\Users\\Tmax\\Desktop";
    private final VendorManager vendorManager;
    private final OAuthAccessTokenRepository tokenRepository;

    public OAuthApiService(VendorManager vendorManager, OAuthAccessTokenRepository tokenRepository) {
        this.vendorManager = vendorManager;
        this.tokenRepository = tokenRepository;
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

    public String cloneRepository(String cloneUrl, String accessToken, String directoryName) throws FailedCloneRepositoryException {
        File directory = new File(BASE_DIR, directoryName);
        if (!directory.exists()) {
            boolean isCreated = directory.mkdirs();
            if (!isCreated) {
                throw new FailedCreateDirectory(directory.getAbsolutePath());
            }
        }

        try{
            Git.cloneRepository().setURI(cloneUrl)
                    .setDirectory(directory)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(accessToken, ""))
                    .call();
        } catch (GitAPIException e) {
            throw new FailedCloneRepositoryException(cloneUrl);
        }

        return directory.getAbsolutePath();
    }
}
