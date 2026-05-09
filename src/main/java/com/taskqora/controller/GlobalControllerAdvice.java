package com.taskqora.controller;

import com.taskqora.model.User;
import com.taskqora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addAttributes(org.springframework.security.core.Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            userService.findByEmail(authentication.getName()).ifPresent(user -> {
                model.addAttribute("user", user);
            });
        }
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("globalAllUsers", allUsers);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}


