package com.project.workaholic.project.repository;

import com.project.workaholic.project.model.entity.ProjectSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectSettingRepository extends JpaRepository<ProjectSetting, String> {
}
