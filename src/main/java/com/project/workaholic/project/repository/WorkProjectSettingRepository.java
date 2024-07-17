package com.project.workaholic.project.repository;

import com.project.workaholic.project.model.entity.WorkProjectSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkProjectSettingRepository extends JpaRepository<WorkProjectSetting, String> {
}
