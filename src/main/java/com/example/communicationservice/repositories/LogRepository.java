package com.example.communicationservice.repositories;

import com.example.communicationservice.models.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogRepository extends JpaRepository<Log, UUID> {

}
