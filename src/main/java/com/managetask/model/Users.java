package com.managetask.model;

//import jakarta.persistence.*;

//import org.springframework.data.redis.core.RedisHash;

import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@RedisHash("Users")
@Entity
@Table(name = "users")
// Table name should be in lowercase
public class Users implements Serializable {

    private String username;

    private String password;

    private boolean enabled;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
