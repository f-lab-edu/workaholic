package com.project.workaholic.project.service;

import com.project.workaholic.config.exception.type.DuplicateProjectException;
import com.project.workaholic.config.exception.type.NotFoundProjectException;
import com.project.workaholic.deploy.model.entity.KubeNamespace;
import com.project.workaholic.deploy.service.DeployService;
import com.project.workaholic.project.model.entity.WorkProjectSetting;
import com.project.workaholic.project.model.entity.WorkProject;
import com.project.workaholic.project.repository.WorkProjectSettingRepository;
import com.project.workaholic.project.repository.WorkProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkProjectService {
    private final WorkProjectRepository workProjectRepository;
    private final WorkProjectSettingRepository settingRepository;
    private final DeployService deployService;

    public WorkProjectService(WorkProjectRepository workProjectRepository, WorkProjectSettingRepository settingRepository, DeployService deployService) {
        this.workProjectRepository = workProjectRepository;
        this.settingRepository = settingRepository;
        this.deployService = deployService;
    }

    public WorkProject getWorkProjectById(String projectId) {
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
        KubeNamespace kubeNamespace = deployService.getNamespaceByAccountId(newWorkProject.getOwner());
        deployService.createPod(kubeNamespace, newWorkProject.getName(),"nginx:latest");
        return newWorkProject;
    }

    public WorkProject updateWorkProject(WorkProject existingWorkProject, WorkProject updatedWorkProject) {
        updatedWorkProject = workProjectRepository.save(existingWorkProject);
        return updatedWorkProject;
    }

    public void deleteWorkProject(WorkProject deletedWorkProject) {
        workProjectRepository.delete(deletedWorkProject);
    }

    public WorkProjectSetting getSettingByWorkProjectId(String projectId) {
        return settingRepository.findById(projectId)
                .orElseThrow(NotFoundProjectException::new);
    }
}
