package com.project.github.model;

import lombok.Getter;

@Getter
public class GithubBranch {
    private String name;
    private Commit commit;

    @Getter
    private static class Commit {
        private String sha;
        private String url;
    }
}
