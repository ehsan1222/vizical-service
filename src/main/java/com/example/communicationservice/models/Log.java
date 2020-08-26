package com.example.communicationservice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Log {

    @Id
    private UUID logUid;

    private String username;
    @Enumerated(EnumType.STRING)
    private ActionType actionType;
    private Date createdDate;

    public Log(String username, ActionType actionType) {
        this.logUid = UUID.randomUUID();
        this.username = username;
        this.actionType = actionType;
        this.createdDate = new Date();
    }
}
