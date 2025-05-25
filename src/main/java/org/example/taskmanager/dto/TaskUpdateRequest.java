package org.example.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.taskmanager.model.TaskStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskUpdateRequest {

    @NotBlank(message = "Title is required and cannot be blank or null")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @FutureOrPresent(message = "Due date must be in the present or future")
    private LocalDateTime dueDate;

    private TaskStatus status;
}
