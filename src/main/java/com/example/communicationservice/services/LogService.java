package com.example.communicationservice.services;

import com.example.communicationservice.models.ActionType;
import com.example.communicationservice.models.Log;
import com.example.communicationservice.repositories.LogRepository;
import com.example.communicationservice.security.VizikaUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void addNewLog(ActionType actionType) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Log newLog = new Log(username, actionType);
            logRepository.save(newLog);
            return;
        }

        throw new IllegalStateException();
    }

}
