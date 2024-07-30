package com.project.gitlab.model;

import lombok.Getter;

@Getter
public class GitlabBranch {
    private String name;
    private Commit commit;

    @Getter
    private static class Commit {
        private String id;
        private String web_url;
        private String title;
    }
}
