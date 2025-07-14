package com.example.backend.dto;

import java.time.LocalDateTime;

import com.example.backend.entity.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateDto {
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    private TaskStatus status = TaskStatus.PENDING;

    private LocalDateTime createdAt;
    
    
    
}