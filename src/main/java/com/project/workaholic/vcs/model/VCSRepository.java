package com.project.workaholic.vcs.model;

public interface VCSRepository {
    String getHtmlUrl();
    String getApiUrl();
    String getCloneUrl();
    String getCommitsUrl();
    String getBranchesUrl();
}
