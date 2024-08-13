package datasource.pod.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PROJECT_POD")
public class ProjectPod {
    @Id
    @Column(name = "PROJECT_ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SPACE")
    private String namespace;

    @Setter
    @Column(name = "IMAGE_NAME")
    private String imageName;

    @Setter
    @Column(name = "ADDRESS")
    private String address;

    public String generateUniquePodName(String prefix) {
        String uuid = UUID.randomUUID().toString().replace("-", ""); // 대시(-) 제거
        return prefix + "-pod-" + uuid.substring(0, 12); // 필요한 경우 길이를 줄일 수 있음
    }

    public String generateUniqueNamespace(String prefix) {
        String uuid = UUID.randomUUID().toString().replace("-", ""); // 대시(-) 제거
        return prefix + "-pod-" + uuid.substring(0, 12); // 필요한 경우 길이를 줄일 수 있음
    }

    public ProjectPod(String projectId) {
        this.id = projectId;
        this.name = generateUniquePodName(projectId);
        this.namespace = generateUniqueNamespace(projectId);
        this.imageName = null;
        this.address = null;
    }

    public ProjectPod(String projectId, String imageName) {
        this.id = projectId;
        this.name = generateUniquePodName(projectId);
        this.namespace = generateUniqueNamespace(projectId);
        this.imageName = imageName;
        this.address = null;
    }
}
