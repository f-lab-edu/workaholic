package com.project.work.repository;

import com.project.work.model.entity.WorkProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkProjectRepository extends JpaRepository<WorkProject, UUID> {
}
