package com.taskqora.service;

import com.taskqora.model.User;
import com.taskqora.repository.ProjectRepository;
import com.taskqora.repository.TaskRepository;
import com.taskqora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public User registerUser(User user) {
        user.setEmail(user.getEmail().toLowerCase());
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(userRepository.count() == 0 ? "ADMIN" : "MEMBER");
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @jakarta.transaction.Transactional
    public void updateLastLogin(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setLastLogin(java.time.LocalDateTime.now());
            userRepository.save(user);
        });
    }

    @jakarta.transaction.Transactional
    public void deleteUser(Long id) {
        // Unassign tasks and projects first
        taskRepository.unassignUser(id);
        projectRepository.unassignCreator(id);
        
        userRepository.deleteById(id);
    }
}


