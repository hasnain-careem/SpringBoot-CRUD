package org.example.taskmanager.service;

import org.example.taskmanager.dto.TaskCreateRequest;
import org.example.taskmanager.dto.TaskDto;
import org.example.taskmanager.dto.TaskUpdateRequest;

import java.util.List;

public interface TaskService {
    List<TaskDto> getAllTasks();
    TaskDto getTaskById(Long id);
    TaskDto createTask(TaskCreateRequest request);
    TaskDto updateTask(Long id, TaskUpdateRequest request);
    void deleteTask(Long id);
}
