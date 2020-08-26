package com.example.communicationservice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class User {

    @Id
    private UUID userUid;

    @Column(unique = true)
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;

    public User(String username, String password, Roles role) {
        this.userUid = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
