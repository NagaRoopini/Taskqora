package com.taskqora.service;

import com.taskqora.model.Project;
import com.taskqora.model.User;
import com.taskqora.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private com.taskqora.repository.TaskRepository taskRepository;

    public Project createProject(Project project, User creator) {
        project.setCreatedBy(creator);
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public Project updateProject(Long id, Project projectDetails) {
        Project project = getProjectById(id);
        project.setTitle(projectDetails.getTitle());
        project.setDescription(projectDetails.getDescription());
        project.setStatus(projectDetails.getStatus());
        project.setStartDate(projectDetails.getStartDate());
        project.setEndDate(projectDetails.getEndDate());
        project.setPriority(projectDetails.getPriority());
        project.setCategory(projectDetails.getCategory());
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        // First delete all tasks associated with this project to avoid FK constraint violations
        taskRepository.deleteByProjectId(id);
        // Then delete the project
        projectRepository.deleteById(id);
    }

    public Map<String, Long> getProjectStats() {
        List<Project> projects = projectRepository.findAll();
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", (long) projects.size());
        stats.put("completed", projects.stream().filter(p -> "Completed".equalsIgnoreCase(p.getStatus())).count());
        stats.put("inProgress", projects.stream().filter(p -> "In Progress".equalsIgnoreCase(p.getStatus())).count());
        stats.put("onHold", projects.stream().filter(p -> "On Hold".equalsIgnoreCase(p.getStatus())).count());
        return stats;
    }
}


