package org.example.taskmanager.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.taskmanager.model.TaskStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskUpdateRequest {

    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    private LocalDateTime dueDate;

    private TaskStatus status;

    @AssertTrue(message = "Due date must be in the present or future.")
    public boolean isDueDateValid() {
        return dueDate == null || !dueDate.isBefore(LocalDateTime.now());
    }
}
