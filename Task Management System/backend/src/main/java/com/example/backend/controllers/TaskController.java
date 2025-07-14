package com.example.backend.controllers;

import com.example.backend.exception.TaskNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks(Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        List<TaskDto> tasks = taskService.getAllTasks(user.getId());
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id, Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        TaskDto task = taskService.getTaskById(id, user.getId());

        if (task == null) {
            throw new TaskNotFoundException("Task not found with ID: " + id);
        }

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id,
                                              @Valid @RequestBody TaskCreateDto taskCreateDto,
                                              Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        TaskDto updatedTask = taskService.updateTask(id, taskCreateDto, user.getId());

        if (updatedTask == null) {
            throw new TaskNotFoundException("Cannot update. Task not found with ID: " + id);
        }

        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id, Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        boolean deleted = taskService.deleteTask(id, user.getId());

        if (!deleted) {
            throw new TaskNotFoundException("Cannot delete. Task not found with ID: " + id);
        }

        return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
    }
}
