package com.example.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.dto.TaskCreateDto;
import com.example.backend.dto.TaskDto;
import com.example.backend.entity.Task;
import com.example.backend.entity.User;
import com.example.backend.repo.TaskRepository;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    
    public TaskDto createTask(TaskCreateDto taskCreateDto, User user) {
        Task task = new Task();
        task.setTitle(taskCreateDto.getTitle());
        task.setDescription(taskCreateDto.getDescription());
        task.setStatus(taskCreateDto.getStatus());
        task.setUser(user);
        task.setCreatedAt(taskCreateDto.getCreatedAt().now());
        
        Task savedTask = taskRepository.save(task);
        return convertToDto(savedTask);
    }
    
    public List<TaskDto> getAllTasks(Long userId) {
        List<Task> tasks = taskRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return tasks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public TaskDto getTaskById(Long taskId, Long userId) {
        Optional<Task> taskOptional = taskRepository.findByIdAndUserId(taskId, userId);
        return taskOptional.map(this::convertToDto).orElse(null);
    }
    
    public TaskDto updateTask(Long taskId, TaskCreateDto taskCreateDto, Long userId) {
        Optional<Task> taskOptional = taskRepository.findByIdAndUserId(taskId, userId);
        
        if (taskOptional.isEmpty()) {
            return null;
        }
        
        Task task = taskOptional.get();
        task.setTitle(taskCreateDto.getTitle());
        task.setDescription(taskCreateDto.getDescription());
        task.setStatus(taskCreateDto.getStatus());
//        task.setCreatedAt(taskCreateDto.getCreatedAt());
        task.setCreatedAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);
        return convertToDto(updatedTask);
    }
    
    public boolean deleteTask(Long taskId, Long userId) {
        Optional<Task> taskOptional = taskRepository.findByIdAndUserId(taskId, userId);
        
        if (taskOptional.isEmpty()) {
            return false;
        }
        
        taskRepository.delete(taskOptional.get());
        return true;
    }
    
    private TaskDto convertToDto(Task task) {
        return new TaskDto(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getCreatedAt()
        );
    }
}