package org.example.taskmanager.util;

import org.example.taskmanager.dto.TaskCreateRequest;
import org.example.taskmanager.dto.TaskDto;
import org.example.taskmanager.model.Task;
import org.springframework.stereotype.Component;
import org.example.taskmanager.model.TaskStatus;

@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskDto toDto(Task task) {
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

    @Override
    public Task toEntity(TaskCreateRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        return task;
    }
}
