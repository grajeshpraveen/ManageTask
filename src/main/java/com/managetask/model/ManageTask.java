package com.managetask.model;

//import jakarta.persistence.*;
//import org.springframework.data.redis.core.RedisHash;

import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@RedisHash("Task")
@Entity
@Table(name = "manage_task") // Table name should be in lowercase
public class ManageTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @NotBlank
    @NotNull
    private String title;

    private String description;

    private boolean completed;

    @Temporal(TemporalType.DATE)
    private Date dueDate;

    // Constructors, getters, and setters

    // Default constructor
    public ManageTask() {
    }

    // Constructor with parameters (if needed)
    public ManageTask(String title, String description, boolean completed, Date dueDate) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.dueDate = dueDate;
    }

    // Getters and setters for all fields

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
