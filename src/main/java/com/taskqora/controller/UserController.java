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
@RequestMapping("/profile")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @GetMapping
    public String viewProfile(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName()).get();
        model.addAttribute("user", user);
        model.addAttribute("profileUser", user);
        return "profile";
    }

    @GetMapping("/{id}")
    public String viewUserProfile(@org.springframework.web.bind.annotation.PathVariable Long id, Authentication authentication, Model model) {
        User currentUser = userService.findByEmail(authentication.getName()).get();
        
        // Role protection: Only ADMINs can view other profiles. Members can only view their own.
        if (!currentUser.getRole().equals("ADMIN") && !currentUser.getId().equals(id)) {
            return "redirect:/profile";
        }

        User targetUser = userService.getUserById(id);
        model.addAttribute("user", currentUser);
        model.addAttribute("profileUser", targetUser);
        return "profile";
    }

    @org.springframework.web.bind.annotation.PostMapping("/update")
    public String updateProfile(@org.springframework.web.bind.annotation.ModelAttribute User userDetails, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).get();
        user.setName(userDetails.getName());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setBio(userDetails.getBio());
        
        userService.saveUser(user);
        return "redirect:/profile?updated";
    }

    @org.springframework.web.bind.annotation.PostMapping("/change-password")
    public String changePassword(@org.springframework.web.bind.annotation.RequestParam String currentPassword,
                               @org.springframework.web.bind.annotation.RequestParam String newPassword,
                               Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).get();
        
        if (passwordEncoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.saveUser(user);
            return "redirect:/profile?passwordChanged";
        } else {
            return "redirect:/profile?error";
        }
    }

    @org.springframework.web.bind.annotation.PostMapping("/delete-self")
    public String deleteSelf(Authentication authentication, jakarta.servlet.http.HttpServletRequest request) {
        User user = userService.findByEmail(authentication.getName()).get();
        userService.deleteUser(user.getId());
        
        try {
            request.logout();
        } catch (jakarta.servlet.ServletException e) {
            e.printStackTrace();
        }
        return "redirect:/login?deleted";
    }
}


