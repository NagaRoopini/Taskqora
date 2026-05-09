package com.taskqora.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String role; // ADMIN or MEMBER

    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String status = "ACTIVE"; // ACTIVE, BUSY, ON_LEAVE, INACTIVE

    private java.time.LocalDate joinedDate = java.time.LocalDate.now();
    private java.time.LocalDateTime lastLogin;

    // Settings & Preferences
    private String theme = "LIGHT"; // LIGHT or DARK
    private boolean emailNotifications = true;
    private boolean desktopNotifications = true;
    private boolean twoFactorEnabled = false;

    public User() {}

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public java.time.LocalDate getJoinedDate() { return joinedDate; }
    public void setJoinedDate(java.time.LocalDate joinedDate) { this.joinedDate = joinedDate; }

    public java.time.LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(java.time.LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public boolean isEmailNotifications() { return emailNotifications; }
    public void setEmailNotifications(boolean emailNotifications) { this.emailNotifications = emailNotifications; }

    public boolean isDesktopNotifications() { return desktopNotifications; }
    public void setDesktopNotifications(boolean desktopNotifications) { this.desktopNotifications = desktopNotifications; }

    public boolean isTwoFactorEnabled() { return twoFactorEnabled; }
    public void setTwoFactorEnabled(boolean twoFactorEnabled) { this.twoFactorEnabled = twoFactorEnabled; }
}


