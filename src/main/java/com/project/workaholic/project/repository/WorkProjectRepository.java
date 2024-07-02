package com.project.workaholic.project.repository;

import com.project.workaholic.project.model.entity.WorkProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkProjectRepository extends JpaRepository<WorkProject, String> {
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM WorkProject a WHERE a.id = :id")
    boolean checkExistsProjectById(@Param("id") String projectId);
}
