package org.example.taskmanager.dto;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.example.taskmanager.model.TaskStatus;

@Getter
@Setter
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
