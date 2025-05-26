package org.example.taskmanager.dto;
import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.taskmanager.model.TaskStatus;

@Getter
@Setter
public class TaskDto {
    private Long id;
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;
    private TaskStatus status;
    @FutureOrPresent(message = "Due date must be in the present or future")
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
