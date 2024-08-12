package workaholic.config;

import datasource.work.repository.WorkProjectRepository;
import datasource.work.repository.WorkProjectSettingRepository;
import datasource.work.service.WorkProjectService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    private final WorkProjectRepository workProjectRepository;
    private final WorkProjectSettingRepository projectSettingRepository;

    public ApplicationConfig(WorkProjectRepository workProjectRepository, WorkProjectSettingRepository projectSettingRepository) {
        this.workProjectRepository = workProjectRepository;
        this.projectSettingRepository = projectSettingRepository;
    }

    @Bean
    public WorkProjectService workProjectService() {
        return new WorkProjectService(this.workProjectRepository, this.projectSettingRepository);
    }
}
