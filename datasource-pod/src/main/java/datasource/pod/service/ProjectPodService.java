package datasource.pod.service;

import datasource.pod.exception.NotFoundProjectPodException;
import datasource.pod.model.entity.ProjectPod;
import datasource.pod.repository.ProjectPodRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectPodService {
    private final ProjectPodRepository podRepository;

    public ProjectPodService(ProjectPodRepository podRepository) {
        this.podRepository = podRepository;
    }

    public ProjectPod getPodByProjectId(String projectId) {
        return podRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundProjectPodException(projectId));
    }

    public void createProjectPod(ProjectPod createdPod) {
        podRepository.save(createdPod);
    }

    public void setAccessAddress(ProjectPod projectPod, String address) {
        projectPod.setAddress(address);
        podRepository.save(projectPod);
    }
}
