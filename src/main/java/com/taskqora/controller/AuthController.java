package com.taskqora.controller;

import com.taskqora.model.User;
import com.taskqora.security.JwtUtil;
import com.taskqora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, @RequestParam("confirm-password") String confirmPassword,
            Model model) {
        try {
            // Condition 1: Check if passwords match
            if (!user.getPassword().equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match!");
                return "register";
            }

            // Condition 2: Strict Email Validation
            String email = user.getEmail().toLowerCase();
            if (!email.matches("^[a-z0-9._%+-]+@(gmail|outlook|yahoo|hotmail|icloud)\\.(com|net|org)$")) {
                String error = "Invalid email format or provider.";
                if (email.contains(".xom"))
                    error = "Typo detected: Please use '.com' instead of '.xom'";
                else if (email.endsWith("@mail.com"))
                    error = "The 'mail.com' domain is not supported. Please use Gmail or Outlook.";

                model.addAttribute("error", error);
                return "register";
            }

            // Condition 3: Register user (UserService handles duplicate email check)
            userService.registerUser(user);
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // API version for JWT
    @PostMapping("/auth/login")
    @ResponseBody
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.get("email"), loginRequest.get("password")));

        if (authentication.isAuthenticated()) {
            String token = jwtUtil.generateToken(loginRequest.get("email"));
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}

