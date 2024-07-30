package com.project.github.model;

import com.project.oauth.model.VCSRepository;
import lombok.Getter;

@Getter
public class GithubRepository implements VCSRepository {
    private String name;
    private String fullName;
    private String url;
    private String htmlUrl;
    private String cloneUrl;
    private String commitsUrl;
    private String branchesUrl;
    @Override
    public String getApiUrl() {
        return this.url;
    }
}
