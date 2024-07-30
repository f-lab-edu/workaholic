package com.project.oauth.model;

public interface VCSRepository {
    String getHtmlUrl();
    String getApiUrl();
    String getCloneUrl();
    String getCommitsUrl();
    String getBranchesUrl();
}
