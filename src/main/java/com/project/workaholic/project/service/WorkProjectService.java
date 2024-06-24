package com.project.workaholic.project.service;

import com.project.workaholic.config.exception.CustomException;
import com.project.workaholic.project.model.entity.WorkProject;
import com.project.workaholic.project.repository.WorkProjectRepository;
import com.project.workaholic.response.model.enumeration.StatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkProjectService {
    private final WorkProjectRepository workProjectRepository;

    public WorkProjectService(WorkProjectRepository workProjectRepository) {
        this.workProjectRepository = workProjectRepository;
    }

    public WorkProject getWorkProjectById(Long projectId) {
        return workProjectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND_PROJECT));
    }

    public List<WorkProject> getAllWorkProjects() {
        return workProjectRepository.findAll();
    }

    public WorkProject createWorkProject(WorkProject newWorkProject) {
        if(workProjectRepository.duplicateCheckByProjectName(newWorkProject.getName())) {
            throw new CustomException(StatusCode.EXISTS_PROJECT_ID);
        }

        newWorkProject = workProjectRepository.save(newWorkProject);
        return newWorkProject;
    }

    public WorkProject updateWorkProject(WorkProject existingWorkProject, WorkProject updatedWorkProject) {
        existingWorkProject.setName(updatedWorkProject.getName());

        updatedWorkProject = workProjectRepository.save(existingWorkProject);
        return updatedWorkProject;
    }

    public void deleteWorkProject(WorkProject deletedWorkProject) {
        workProjectRepository.delete(deletedWorkProject);
    }
}
