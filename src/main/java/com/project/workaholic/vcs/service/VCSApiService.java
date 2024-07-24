package com.project.workaholic.vcs.service;

import com.project.workaholic.config.exception.type.FailedCreateDirectory;
import com.project.workaholic.config.exception.type.InvalidRepositoryException;
import com.project.workaholic.config.exception.type.JGitApiException;
import com.project.workaholic.config.exception.type.TransportRepositoryException;
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

@Service
public class VCSApiService {
    private static final String BASE_DIR = "C:\\Users\\Tmax\\Desktop";
    private final VendorManager vendorManager;
    private final OAuthAccessTokenRepository tokenRepository;

    public VCSApiService(VendorManager vendorManager, OAuthAccessTokenRepository tokenRepository) {
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

    public String cloneRepository(String cloneUrl, String accessToken, String directoryName) {
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
        } catch (InvalidRemoteException e) {
            throw new InvalidRepositoryException(cloneUrl);
        } catch (TransportException e) {
            throw new TransportRepositoryException();
        } catch (GitAPIException e) {
            throw new JGitApiException();
        }

        return directory.getAbsolutePath();
    }
}
