package com.taskqora.repository;

import com.taskqora.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByCreatedById(Long userId);

    @org.springframework.data.jpa.repository.Modifying
    @jakarta.transaction.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE Project p SET p.createdBy = null WHERE p.createdBy.id = :userId")
    void unassignCreator(@org.springframework.data.repository.query.Param("userId") Long userId);
}


