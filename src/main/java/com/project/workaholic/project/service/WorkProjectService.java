package com.project.workaholic.project.service;

import com.project.workaholic.config.exception.CustomException;
import com.project.workaholic.deploy.model.entity.KubeNamespace;
import com.project.workaholic.deploy.service.DeployService;
import com.project.workaholic.project.model.entity.WorkProject;
import com.project.workaholic.project.repository.WorkProjectRepository;
import com.project.workaholic.response.model.enumeration.StatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkProjectService {
    private final WorkProjectRepository workProjectRepository;
    private final DeployService deployService;

    public WorkProjectService(WorkProjectRepository workProjectRepository, DeployService deployService) {
        this.workProjectRepository = workProjectRepository;
        this.deployService = deployService;
    }

    public WorkProject getWorkProjectById(String projectId) {
        return workProjectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND_PROJECT));
    }

    public List<WorkProject> getAllWorkProjectsByAccountId(String accountId) {
        return workProjectRepository.findAllWorkProjectByAccountId(accountId);
    }

    public WorkProject createWorkProject(WorkProject newWorkProject) {
        if(workProjectRepository.checkExistsProjectById(newWorkProject.getId())) {
            throw new CustomException(StatusCode.EXISTS_PROJECT_ID);
        }

        newWorkProject = workProjectRepository.save(newWorkProject);
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
}
