package com.project.work.repository;

import com.project.work.model.entity.WorkProjectSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkProjectSettingRepository extends JpaRepository<WorkProjectSetting, String> {
}
