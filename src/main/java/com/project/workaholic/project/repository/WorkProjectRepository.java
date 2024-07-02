package com.project.workaholic.project.repository;

import com.project.workaholic.project.model.entity.WorkProject;
import com.project.workaholic.project.repository.custom.CustomWorkProjectRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkProjectRepository extends JpaRepository<WorkProject, String>, CustomWorkProjectRepository {
}
