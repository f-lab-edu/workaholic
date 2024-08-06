package com.project.work.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.config.exception.type.NotFoundProjectException;
import com.project.work.model.entity.WorkProject;
import com.project.work.model.entity.WorkProjectSetting;
import com.project.work.repository.WorkProjectRepository;
import com.project.work.repository.WorkProjectSettingRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkProjectService {
    private final WorkProjectRepository workProjectRepository;
    private final WorkProjectSettingRepository settingRepository;
    private final RabbitTemplate rabbitTemplate;

    public WorkProjectService(WorkProjectRepository workProjectRepository, WorkProjectSettingRepository settingRepository, RabbitTemplate rabbitTemplate) {
        this.workProjectRepository = workProjectRepository;
        this.settingRepository = settingRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public WorkProject getWorkProjectById(String projectId) {
        return workProjectRepository.findById(projectId)
                .orElseThrow(NotFoundProjectException::new);
    }

    @Transactional
    public WorkProject createWorkProject(WorkProject newWorkProject, WorkProjectSetting setting) {
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
        return workProjectRepository.findAll();
    }

    public void sendMessageQueue(String routingKey, Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(obj);

        rabbitTemplate.convertAndSend("workaholic.exchange", routingKey, json);
    }

    public String sendMessage(String routingKey, Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(obj);

        rabbitTemplate.convertAndSend("workaholic.exchange", routingKey, json);
        return json;
    }
}
