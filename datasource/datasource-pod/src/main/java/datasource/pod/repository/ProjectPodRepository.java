package datasource.pod.repository;

import datasource.pod.model.entity.ProjectPod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPodRepository extends JpaRepository<ProjectPod, String> {
}
