package com.taskqora.controller;

import com.taskqora.model.Project;
import com.taskqora.model.User;
import com.taskqora.service.ProjectService;
import com.taskqora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listProjects(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName()).get();
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("user", user);
        model.addAttribute("projects", projects);
        
        // Add stats for summary cards
        long total = projects.size();
        long completed = projects.stream().filter(p -> "Completed".equals(p.getStatus())).count();
        long inProgress = projects.stream().filter(p -> "In Progress".equals(p.getStatus())).count();
        long onHold = projects.stream().filter(p -> "On Hold".equals(p.getStatus())).count();

        model.addAttribute("totalProjects", total);
        model.addAttribute("completedProjects", completed);
        model.addAttribute("inProgressProjects", inProgress);
        model.addAttribute("onHoldProjects", onHold);
        
        model.addAttribute("projectsFooter", inProgress + " active projects");
        model.addAttribute("updatedFooter", "Updated recently");
        
        return "projects";
    }

    @GetMapping("/create")
    public String showCreateForm(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName()).get();
        if (!"ADMIN".equals(user.getRole())) return "redirect:/projects";
        model.addAttribute("user", user);
        model.addAttribute("project", new Project());
        model.addAttribute("allUsers", userService.getAllUsers());
        return "create-project";
    }

    @PostMapping("/create")
    public String createProject(@ModelAttribute Project project, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).get();
        if (!"ADMIN".equals(user.getRole())) return "redirect:/projects";
        projectService.createProject(project, user);
        return "redirect:/projects";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName()).get();
        if (!"ADMIN".equals(user.getRole())) return "redirect:/projects";
        Project project = projectService.getProjectById(id);
        model.addAttribute("user", user);
        model.addAttribute("project", project);
        return "edit-project";
    }

    @PostMapping("/update/{id}")
    public String updateProject(@PathVariable Long id, @ModelAttribute Project project, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).get();
        if (!"ADMIN".equals(user.getRole())) return "redirect:/projects";
        projectService.updateProject(id, project);
        return "redirect:/projects";
    }

    @GetMapping("/view/{id}")
    public String viewProject(@PathVariable Long id, Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName()).get();
        Project project = projectService.getProjectById(id);
        model.addAttribute("user", user);
        model.addAttribute("project", project);
        return "project-details";
    }

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/projects";
    }
}


