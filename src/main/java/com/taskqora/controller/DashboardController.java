package com.taskqora.controller;

import com.taskqora.model.User;
import com.taskqora.service.TaskService;
import com.taskqora.service.UserService;
import com.taskqora.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectService projectService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.findByEmail(email).get();
        
        model.addAttribute("user", user);
        
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            model.addAttribute("stats", taskService.getTaskStats());
            model.addAttribute("projectStats", projectService.getProjectStats());
            model.addAttribute("totalProjects", projectService.getAllProjects().size());
            model.addAttribute("totalUsers", userService.getAllUsers().size());
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("recentMembers", userService.getAllUsers().stream().limit(5).collect(java.util.stream.Collectors.toList()));
            model.addAttribute("dueSoonTasks", taskService.getAllTasks().stream()
                .filter(t -> t.getDueDate() != null)
                .limit(5).collect(java.util.stream.Collectors.toList()));
            
            // Real Activity
            java.util.List<java.util.Map<String, String>> realActivities = new java.util.ArrayList<>();
            projectService.getAllProjects().stream().limit(2).forEach(p -> {
                realActivities.add(java.util.Map.of("desc", "Project '" + p.getTitle() + "' created", "time", "Recently", "icon", "fa-plus", "color", "var(--primary)"));
            });
            taskService.getAllTasks().stream().limit(2).forEach(t -> {
                realActivities.add(java.util.Map.of("desc", "Task '" + t.getTitle() + "' assigned", "time", "Recently", "icon", "fa-check", "color", "var(--success)"));
            });
            model.addAttribute("activities", realActivities);

            // Dynamic Card Insights
            long activeProj = projectService.getAllProjects().stream()
                .filter(p -> "IN_PROGRESS".equalsIgnoreCase(p.getStatus())).count();
            model.addAttribute("projectsFooter", activeProj + " active projects");

            long totalT = taskService.getAllTasks().size();
            long compT = taskService.getTaskStats().get("completed");
            int rate = totalT > 0 ? (int)((compT * 100.0) / totalT) : 0;
            model.addAttribute("tasksFooter", rate + "% overall completion");
            
            long highP = taskService.getAllTasks().stream()
                .filter(t -> "HIGH".equalsIgnoreCase(t.getPriority()) && !"COMPLETED".equalsIgnoreCase(t.getStatus())).count();
            model.addAttribute("pendingFooter", highP + " high priority tasks");
            
            model.addAttribute("completedFooter", "Updated " + java.time.format.DateTimeFormatter.ofPattern("hh:mm a").format(java.time.LocalTime.now()));
            
            long newM = userService.getAllUsers().stream()
                .filter(u -> u.getJoinedDate() != null && u.getJoinedDate().getMonth() == java.time.LocalDate.now().getMonth()).count();
            model.addAttribute("usersFooter", newM + " new this month");

            return "admin-dashboard";
        } else {
            // Member Logic - Direct Routing
            java.util.List<com.taskqora.model.Task> myTasks = taskService.getTasksByUserId(user.getId());
            model.addAttribute("stats", taskService.getUserTaskStats(user.getId()));
            model.addAttribute("myTasks", myTasks);
            
            // Group tasks by project for the bar chart/list
            java.util.Map<String, Long> tasksByProject = myTasks.stream()
                .filter(t -> t.getProject() != null)
                .collect(java.util.stream.Collectors.groupingBy(t -> t.getProject().getTitle(), java.util.stream.Collectors.counting()));
            model.addAttribute("tasksByProject", tasksByProject);
            
            // Add extra data for member dashboard
            model.addAttribute("myProjects", projectService.getAllProjects().stream().limit(3).collect(java.util.stream.Collectors.toList()));
            model.addAttribute("dueTodayCount", taskService.getTasksDueToday(user.getId()).size()); 
            
            return "member-dashboard";
        }
    }

    @GetMapping("/search")
    public String search(@org.springframework.web.bind.annotation.RequestParam String query, Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.findByEmail(email).get();
        
        model.addAttribute("user", user);
        model.addAttribute("query", query);
        model.addAttribute("tasks", taskService.searchTasks(query));
        return "search-results";
    }
    
    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }
}


