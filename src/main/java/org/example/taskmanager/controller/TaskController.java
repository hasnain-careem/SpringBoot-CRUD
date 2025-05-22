package org.example.taskmanager.controller;

import jakarta.validation.Valid;
import org.example.taskmanager.dto.TaskCreateRequest;
import org.example.taskmanager.dto.TaskDto;
import org.example.taskmanager.dto.TaskUpdateRequest;
import org.example.taskmanager.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Get all tasks
    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // Get the task by ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    // Create a new task
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskCreateRequest request) {
        TaskDto createdTask = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    // Update a task
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id,
                                              @Valid @RequestBody TaskUpdateRequest request) {
        TaskDto updatedTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    // Delete a task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
