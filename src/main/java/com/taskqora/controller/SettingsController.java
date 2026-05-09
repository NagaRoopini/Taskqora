package com.taskqora.controller;

import com.taskqora.model.User;
import com.taskqora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String viewSettings(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName()).get();
        model.addAttribute("user", user);
        return "settings";
    }

    @org.springframework.web.bind.annotation.PostMapping("/update-preferences")
    public String updatePreferences(@org.springframework.web.bind.annotation.RequestParam String theme,
                                  @org.springframework.web.bind.annotation.RequestParam(required = false) boolean emailNotifications,
                                  @org.springframework.web.bind.annotation.RequestParam(required = false) boolean desktopNotifications,
                                  Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).get();
        user.setTheme(theme);
        user.setEmailNotifications(emailNotifications);
        user.setDesktopNotifications(desktopNotifications);
        userService.saveUser(user);
        return "redirect:/settings?updated";
    }

    @org.springframework.web.bind.annotation.PostMapping("/toggle-2fa")
    public String toggle2FA(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).get();
        user.setTwoFactorEnabled(!user.isTwoFactorEnabled());
        userService.saveUser(user);
        return "redirect:/settings?2faChanged";
    }

    @org.springframework.web.bind.annotation.PostMapping("/update-status")
    public String updateStatus(@org.springframework.web.bind.annotation.RequestParam String status, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).get();
        user.setStatus(status);
        userService.saveUser(user);
        return "redirect:/settings?statusUpdated";
    }
}


