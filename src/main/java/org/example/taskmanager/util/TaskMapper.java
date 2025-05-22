package org.example.taskmanager.util;

import org.example.taskmanager.dto.TaskCreateRequest;
import org.example.taskmanager.dto.TaskDto;
import org.example.taskmanager.model.Task;

public interface TaskMapper {
    TaskDto toDto(Task task);
    Task toEntity(TaskCreateRequest request);
}
