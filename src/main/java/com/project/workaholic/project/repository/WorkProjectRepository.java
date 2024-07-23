package com.project.workaholic.project.repository;

import com.project.workaholic.project.model.entity.WorkProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkProjectRepository extends JpaRepository<WorkProject, UUID> {
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM WorkProject a WHERE a.id = :id")
    boolean checkExistsProjectById(@Param("id") UUID projectId);
    @Query("SELECT a FROM WorkProject a WHERE a.owner = :id")
    List<WorkProject> findAllWorkProjectByAccountId(@Param("id") String owner);
}
