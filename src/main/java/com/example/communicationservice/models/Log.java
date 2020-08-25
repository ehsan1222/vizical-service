package com.example.communicationservice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Log {

    @Id
    private UUID logUid;

    @ManyToOne
    private User user;
    private String content;

}
