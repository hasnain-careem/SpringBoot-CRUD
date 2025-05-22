package org.example.taskmanager.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaskCreateRequest {
    @NotBlank
    private String title;
    private String description;
    private LocalDateTime dueDate;
}

