package datasource.work.model.entity;

import datasource.work.model.enumeration.ProjectStatus;
import datasource.work.model.enumeration.VCSVendor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "WORK_PROJECT")
public class WorkProject {
    @Id
    @Column(name = "ID", unique = true, updatable = false, nullable = false)
    private String id;

    @Setter
    @Column(name = "REPOSITORY_URL")
    private String repoUrl;

    @Setter
    @Column(name = "CLONED_PATH")
    private String clonePath;

    @Column(name = "VENDOR")
    @Enumerated(EnumType.STRING)
    private VCSVendor vendor;

    @Setter
    @Column
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    public WorkProject(String id, String repoUrl, VCSVendor vendor, ProjectStatus status) {
        this.id = id;
        this.repoUrl = repoUrl;
        this.vendor = vendor;
        this.status = status;
    }
}
