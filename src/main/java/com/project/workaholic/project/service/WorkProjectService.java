package com.project.workaholic.project.service;

import com.project.workaholic.config.exception.type.DuplicateProjectException;
import com.project.workaholic.config.exception.type.NotFoundProjectException;
import com.project.workaholic.project.model.entity.WorkProjectSetting;
import com.project.workaholic.project.model.entity.WorkProject;
import com.project.workaholic.project.repository.WorkProjectSettingRepository;
import com.project.workaholic.project.repository.WorkProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WorkProjectService {
    private final WorkProjectRepository workProjectRepository;
    private final WorkProjectSettingRepository settingRepository;

    public WorkProjectService(WorkProjectRepository workProjectRepository, WorkProjectSettingRepository settingRepository) {
        this.workProjectRepository = workProjectRepository;
        this.settingRepository = settingRepository;
    }

    public WorkProject getWorkProjectById(UUID projectId) {
        return workProjectRepository.findById(projectId)
                .orElseThrow(NotFoundProjectException::new);
    }

    public List<WorkProject> getAllWorkProjectsByAccountId(String accountId) {
        return workProjectRepository.findAllWorkProjectByAccountId(accountId);
    }

    @Transactional
    public WorkProject createWorkProject(WorkProject newWorkProject, WorkProjectSetting setting) {
        if(workProjectRepository.checkExistsProjectById(newWorkProject.getId())) {
            throw new DuplicateProjectException();
        }
        newWorkProject = workProjectRepository.save(newWorkProject);
        settingRepository.save(setting);
        return newWorkProject;
    }

    @Transactional
    public void deleteWorkProject(WorkProject deletedWorkProject) {
        WorkProjectSetting setting = getSettingByWorkProjectId(deletedWorkProject.getId());
        settingRepository.delete(setting);
        workProjectRepository.delete(deletedWorkProject);
    }

    public WorkProjectSetting getSettingByWorkProjectId(UUID projectId) {
        return settingRepository.findById(projectId)
                .orElseThrow(NotFoundProjectException::new);
    }

    public WorkProject updateWorkProject(WorkProject existingWorkProject, WorkProject updatedWorkProject) {
        updatedWorkProject = workProjectRepository.save(existingWorkProject);
        return updatedWorkProject;
    }
}
