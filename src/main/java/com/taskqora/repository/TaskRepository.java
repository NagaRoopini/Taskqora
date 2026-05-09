package com.taskqora.repository;

import com.taskqora.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedToId(Long userId);
    List<Task> findByProjectId(Long projectId);
    List<Task> findByAssignedToIdAndDueDate(Long userId, java.time.LocalDate date);
    
    // For dashboard analytics
    @org.springframework.data.jpa.repository.Query("SELECT COUNT(t) FROM Task t WHERE UPPER(t.status) = UPPER(:status)")
    long countByStatus(@org.springframework.data.repository.query.Param("status") String status);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(t) FROM Task t WHERE t.assignedTo.id = :userId AND UPPER(t.status) = UPPER(:status)")
    long countByAssignedToIdAndStatus(@org.springframework.data.repository.query.Param("userId") Long userId, @org.springframework.data.repository.query.Param("status") String status);
    long countByAssignedToId(Long userId);
    long countByAssignedToIdAndDueDate(Long userId, java.time.LocalDate date);
    long countByDueDate(java.time.LocalDate date);
    
    List<Task> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

    @org.springframework.data.jpa.repository.Modifying
    @jakarta.transaction.Transactional
    void deleteByProjectId(Long projectId);

    @org.springframework.data.jpa.repository.Modifying
    @jakarta.transaction.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE Task t SET t.assignedTo = null WHERE t.assignedTo.id = :userId")
    void unassignUser(@org.springframework.data.repository.query.Param("userId") Long userId);
}


