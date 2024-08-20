package workaholic.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "workaholic")
public class ApplicationProperties {
    private final String vcsApp;

    public ApplicationProperties(String vcsApp) {
        this.vcsApp = vcsApp;
    }
}
