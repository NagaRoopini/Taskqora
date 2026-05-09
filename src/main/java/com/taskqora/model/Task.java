package com.taskqora.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String priority; // LOW, MEDIUM, HIGH

    private LocalDate dueDate;

    private String status; // PENDING, IN_PROGRESS, COMPLETED, OVERDUE

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(columnDefinition = "TEXT")
    private String progressNotes;

    private String attachmentPath;

    private int progress = 0; // 0 to 100

    public Task() {
        this.status = "PENDING";
    }

    public Task(String title, String description, String priority, LocalDate dueDate, User assignedTo, Project project) {
        this();
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.assignedTo = assignedTo;
        this.project = project;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getAssignedTo() { return assignedTo; }
    public void setAssignedTo(User assignedTo) { this.assignedTo = assignedTo; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public String getProgressNotes() { return progressNotes; }
    public void setProgressNotes(String progressNotes) { this.progressNotes = progressNotes; }

    public String getAttachmentPath() { return attachmentPath; }
    public void setAttachmentPath(String attachmentPath) { this.attachmentPath = attachmentPath; }

    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }
}


