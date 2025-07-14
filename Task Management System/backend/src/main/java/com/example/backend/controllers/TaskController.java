package com.example.backend.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.TaskCreateDto;
import com.example.backend.dto.TaskDto;
import com.example.backend.entity.User;
import com.example.backend.services.AuthService;
import com.example.backend.services.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private AuthService authService;
    
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskCreateDto taskCreateDto, 
                                            Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        TaskDto createdTask = taskService.createTask(taskCreateDto, user);
        return ResponseEntity.ok(createdTask);
    }
    
    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks(Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        List<TaskDto> tasks = taskService.getAllTasks(user.getId());
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id, Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        TaskDto task = taskService.getTaskById(id, user.getId());
        
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(task);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, 
                                            @Valid @RequestBody TaskCreateDto taskCreateDto,
                                            Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        TaskDto updatedTask = taskService.updateTask(id, taskCreateDto, user.getId());
        
        if (updatedTask == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(updatedTask);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id, Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        boolean deleted = taskService.deleteTask(id, user.getId());
        
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok("Task deleted successfully");
    }
}