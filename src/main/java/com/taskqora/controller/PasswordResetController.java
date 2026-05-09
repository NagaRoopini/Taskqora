package com.taskqora.controller;

import com.taskqora.model.User;
import com.taskqora.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.Random;

@Controller
public class PasswordResetController {

    @Autowired
    private UserRepository userRepository;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model, HttpSession session) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            model.addAttribute("error", "This email is not registered with us!");
            return "forgot-password";
        }

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        session.setAttribute("otp", otp);
        session.setAttribute("resetEmail", email);

        // Send Email (In a real app, this would use mailSender)
        System.out.println("DEBUG: OTP for " + email + " is: " + otp);
        
        try {
            if (mailSender != null) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email);
                message.setSubject("taskqora - Password Reset OTP");
                message.setText("Your verification code is: " + otp + "\n\nPlease use this code to reset your password.");
                mailSender.send(message);
            }
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            // We continue anyway so the user can use the console OTP for demo purposes
        }

        model.addAttribute("email", email);
        model.addAttribute("message", "OTP has been sent to your email.");
        return "redirect:/verify-otp?email=" + email;
    }

    @GetMapping("/verify-otp")
    public String showVerifyOtpForm(@RequestParam("email") String email, Model model, HttpSession session) {
        if (session.getAttribute("otp") == null) {
            return "redirect:/forgot-password";
        }
        model.addAttribute("email", email);
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp, @RequestParam("email") String email, Model model, HttpSession session) {
        String sessionOtp = (String) session.getAttribute("otp");
        String sessionEmail = (String) session.getAttribute("resetEmail");

        if (sessionOtp != null && sessionOtp.equals(otp) && sessionEmail != null && sessionEmail.equals(email)) {
            session.setAttribute("otpVerified", true);
            return "redirect:/reset-password";
        } else {
            model.addAttribute("email", email);
            model.addAttribute("error", "Invalid or expired OTP code!");
            return "verify-otp";
        }
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(HttpSession session) {
        if (session.getAttribute("otpVerified") == null) {
            return "redirect:/forgot-password";
        }
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("password") String password, 
                                     @RequestParam("confirmPassword") String confirmPassword,
                                     HttpSession session, Model model) {
        if (session.getAttribute("otpVerified") == null) {
            return "redirect:/forgot-password";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "reset-password";
        }

        String email = (String) session.getAttribute("resetEmail");
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(password); // Note: In real app, encode this
            userRepository.save(user);
            
            session.invalidate();
            return "redirect:/login?resetSuccess";
        }

        return "redirect:/login";
    }
}



