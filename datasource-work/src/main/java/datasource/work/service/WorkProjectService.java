package datasource.work.service;

import datasource.work.config.excpetion.type.NotFoundProjectException;
import datasource.work.model.entity.WorkProject;
import datasource.work.model.entity.WorkProjectSetting;
import datasource.work.model.enumeration.ProjectStatus;
import datasource.work.repository.WorkProjectRepository;
import datasource.work.repository.WorkProjectSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkProjectService {
    private final WorkProjectRepository projectRepository;
    private final WorkProjectSettingRepository settingRepository;

    public WorkProjectService(WorkProjectRepository projectRepository, WorkProjectSettingRepository settingRepository) {
        this.projectRepository = projectRepository;
        this.settingRepository = settingRepository;
    }

    public WorkProject getWorkProjectById(String projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(NotFoundProjectException::new);
    }

    @Transactional
    public void createWorkProject(WorkProject newWorkProject, WorkProjectSetting setting) {
        projectRepository.save(newWorkProject);
        settingRepository.save(setting);
    }

    @Transactional
    public void deleteWorkProject(WorkProject deletedWorkProject) {
        WorkProjectSetting setting = getSettingByWorkProjectId(deletedWorkProject.getId());
        settingRepository.delete(setting);
        projectRepository.delete(deletedWorkProject);
    }

    public WorkProjectSetting getSettingByWorkProjectId(String projectId) {
        return settingRepository.findById(projectId)
                .orElseThrow(NotFoundProjectException::new);
    }

    public void updateWorkProject(WorkProjectSetting existingSetting, WorkProjectSetting updatedSetting) {
        existingSetting.setBuildType(updatedSetting.getBuildType());
        existingSetting.setJavaVersion(updatedSetting.getJavaVersion());
        existingSetting.setNodePort(updatedSetting.getNodePort());
        existingSetting.setWorkDirectory(updatedSetting.getWorkDirectory());
        existingSetting.setEnvVariables(updatedSetting.getEnvVariables());
        existingSetting.setExecuteParameters(updatedSetting.getExecuteParameters());

        settingRepository.save(existingSetting);
    }

    public List<WorkProject> getAllWorkProject() {
        return projectRepository.findAll();
    }

    public void setClonedPath(WorkProject workProject, String clonePath) {
        workProject.setClonePath(clonePath);
        workProject.setStatus(ProjectStatus.CLONED);
        projectRepository.save(workProject);
    }

    public void failedCloneRepo(WorkProject workProject) {
        workProject.setStatus(ProjectStatus.FAILED_CLONE);
        projectRepository.save(workProject);
    }
}
