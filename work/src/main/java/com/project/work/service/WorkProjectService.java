package com.project.work.service;

import com.project.config.exception.type.NotFoundProjectException;
import com.project.datasource.work.model.entity.WorkProject;
import com.project.datasource.work.model.entity.WorkProjectSetting;
import com.project.datasource.work.repository.WorkProjectRepository;
import com.project.datasource.work.repository.WorkProjectSettingRepository;
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
        newWorkProject = projectRepository.save(newWorkProject);
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
        existingSetting.setPort(updatedSetting.getPort());
        existingSetting.setWorkDirectory(updatedSetting.getWorkDirectory());
        existingSetting.setEnvVariables(updatedSetting.getEnvVariables());
        existingSetting.setExecuteParameters(updatedSetting.getExecuteParameters());

        settingRepository.save(existingSetting);
    }

    public List<WorkProject> getAllWorkProject() {
        return projectRepository.findAll();
    }


}
