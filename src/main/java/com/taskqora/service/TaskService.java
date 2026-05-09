package com.taskqora.service;

import com.taskqora.model.Task;
import com.taskqora.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByAssignedToId(userId);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task task = getTaskById(id);
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setPriority(taskDetails.getPriority());
        task.setDueDate(taskDetails.getDueDate());
        task.setStatus(taskDetails.getStatus());
        task.setAssignedTo(taskDetails.getAssignedTo());
        task.setProject(taskDetails.getProject());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Map<String, Long> getTaskStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", taskRepository.count());
        stats.put("completed", taskRepository.countByStatus("COMPLETED"));
        stats.put("pending", taskRepository.countByStatus("PENDING"));
        stats.put("inProgress", taskRepository.countByStatus("IN_PROGRESS"));
        stats.put("review", taskRepository.countByStatus("REVIEW"));
        stats.put("overdue", taskRepository.countByStatus("OVERDUE"));
        stats.put("dueToday", taskRepository.countByDueDate(java.time.LocalDate.now()));
        return stats;
    }

    public Map<String, Long> getUserTaskStats(Long userId) {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", taskRepository.countByAssignedToId(userId));
        stats.put("completed", taskRepository.countByAssignedToIdAndStatus(userId, "COMPLETED"));
        stats.put("pending", taskRepository.countByAssignedToIdAndStatus(userId, "PENDING"));
        stats.put("inProgress", taskRepository.countByAssignedToIdAndStatus(userId, "IN_PROGRESS"));
        stats.put("dueToday", taskRepository.countByAssignedToIdAndDueDate(userId, java.time.LocalDate.now()));
        return stats;
    }

    public List<Task> getTasksDueToday(Long userId) {
        return taskRepository.findByAssignedToIdAndDueDate(userId, java.time.LocalDate.now());
    }

    public long countTasksDueToday() {
        return taskRepository.countByDueDate(java.time.LocalDate.now());
    }

    public List<Task> searchTasks(String query) {
        return taskRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
    }
}


