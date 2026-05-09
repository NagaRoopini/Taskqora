package com.taskqora.controller;

import com.taskqora.model.User;
import com.taskqora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/members")
public class TeamController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listMembers(Authentication authentication, Model model) {
        User currentUser = userService.findByEmail(authentication.getName()).orElse(null);
        if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
            return "redirect:/dashboard";
        }
        
        List<User> users = userService.getAllUsers();
        
        model.addAttribute("user", currentUser);
        model.addAttribute("members", users);
        model.addAttribute("totalMembers", users.size());
        
        // Dynamic stats
        long active = users.stream().filter(u -> "ACTIVE".equalsIgnoreCase(u.getStatus())).count();
        long leave = users.stream().filter(u -> "ON LEAVE".equalsIgnoreCase(u.getStatus())).count();
        long inactive = users.stream().filter(u -> "INACTIVE".equalsIgnoreCase(u.getStatus())).count();
        
        model.addAttribute("activeCount", active);
        model.addAttribute("leaveCount", leave);
        model.addAttribute("inactiveCount", inactive);
        
        return "members";
    }

    @GetMapping("/delete/{id}")
    public String deleteMember(@org.springframework.web.bind.annotation.PathVariable Long id, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName()).get();
        
        // Security: Only Admin can delete, and Admin cannot delete self
        if ("ADMIN".equals(currentUser.getRole()) && !currentUser.getId().equals(id)) {
            userService.deleteUser(id);
        }
        return "redirect:/members";
    }
}


