package com.taskqora.controller;

import com.taskqora.model.Task;
import com.taskqora.model.User;
import com.taskqora.service.TaskService;
import com.taskqora.service.ProjectService;
import com.taskqora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listTasks(org.springframework.security.core.Authentication authentication, Model model) {
        com.taskqora.model.User user = userService.findByEmail(authentication.getName()).get();
        List<Task> tasks;
        
        java.util.Map<String, Long> stats;
        if ("ADMIN".equals(user.getRole())) {
            tasks = taskService.getAllTasks();
            stats = taskService.getTaskStats();
        } else {
            tasks = taskService.getTasksByUserId(user.getId());
            stats = taskService.getUserTaskStats(user.getId());
        }
        
        model.addAttribute("totalTasks", stats.get("total"));
        model.addAttribute("completedTasks", stats.get("completed"));
        model.addAttribute("pendingTasks", stats.get("pending"));
        model.addAttribute("inProgress", stats.get("inProgress"));
        model.addAttribute("dueToday", stats.getOrDefault("dueToday", taskService.countTasksDueToday()));
        
        model.addAttribute("user", user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("users", userService.getAllUsers());
        
        return "tasks";
    }

    @GetMapping("/create")
    public String showCreateForm(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName()).get();
        if (!"ADMIN".equals(user.getRole())) return "redirect:/tasks";
        model.addAttribute("user", user);
        model.addAttribute("task", new Task());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("users", userService.getAllUsers());
        return "create-task";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).get();
        if (!"ADMIN".equals(user.getRole())) return "redirect:/tasks";
        taskService.createTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName()).get();
        if (!"ADMIN".equals(user.getRole())) return "redirect:/tasks";
        Task task = taskService.getTaskById(id);
        model.addAttribute("user", user);
        model.addAttribute("task", task);
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("users", userService.getAllUsers());
        return "edit-task";
    }

    @GetMapping("/view/{id}")
    public String viewTask(@PathVariable Long id, org.springframework.security.core.Authentication authentication, Model model) {
        com.taskqora.model.User user = userService.findByEmail(authentication.getName()).get();
        model.addAttribute("user", user);
        model.addAttribute("task", taskService.getTaskById(id));
        return "task-details";
    }

    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute Task task) {
        taskService.updateTask(id, task);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
    
    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id, 
                               @RequestParam String status, 
                               @RequestParam(required = false, defaultValue = "0") int progress,
                               @RequestParam(required = false) String progressNotes,
                               Authentication authentication) {
        Task task = taskService.getTaskById(id);
        User user = userService.findByEmail(authentication.getName()).get();
        
        // Security Check: Only Admin or the Assigned User can update status
        if (!"ADMIN".equals(user.getRole()) && (task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(user.getId()))) {
            return "redirect:/tasks?error=unauthorized";
        }

        task.setStatus(status);
        task.setProgress(progress);
        if (progressNotes != null) {
            task.setProgressNotes(progressNotes);
        }
        taskService.updateTask(id, task);
        return "redirect:/tasks/view/" + id + "?updated";
    }
}


