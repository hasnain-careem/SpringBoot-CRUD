package org.example.taskmanager.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.example.taskmanager.model.TaskStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskUpdateRequest {
    @NotBlank
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private TaskStatus status;
}

