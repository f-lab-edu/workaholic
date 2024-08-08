package vcs.integration.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "integration")
public class VCSProperties {
    private final String NAME;
    private final String BASE_URL;

    public VCSProperties(String NAME, String BASE_URL) {
        this.NAME = NAME;
        this.BASE_URL = BASE_URL;
    }
}
