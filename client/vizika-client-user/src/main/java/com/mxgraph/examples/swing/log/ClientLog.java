package com.mxgraph.examples.swing.log;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "client_log")
@Getter
@NoArgsConstructor
public class ClientLog {

    @Id
    private UUID id;
    private String username;
    private Date createdDate;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    public ClientLog(String username, ActionType actionType) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.actionType = actionType;
        this.createdDate = new Date();
    }
}
