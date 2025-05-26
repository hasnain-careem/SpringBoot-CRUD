package org.example.taskmanager.mapper;

import org.example.taskmanager.dto.TaskCreateRequest;
import org.example.taskmanager.dto.TaskDto;
import org.example.taskmanager.model.Task;

public class TaskMapper {

    public static TaskDto toDto(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("task must not be null");
        }

        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        return dto;
    }

    public static Task toEntity(TaskCreateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        return task;
    }
}
