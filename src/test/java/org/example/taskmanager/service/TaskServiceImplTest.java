package org.example.taskmanager.service;

import org.example.taskmanager.dto.TaskCreateRequest;
import org.example.taskmanager.dto.TaskDto;
import org.example.taskmanager.dto.TaskUpdateRequest;
import org.example.taskmanager.exception.TaskNotFoundException;
import org.example.taskmanager.model.Task;
import org.example.taskmanager.model.TaskStatus;
import org.example.taskmanager.repository.TaskRepository;
import org.example.taskmanager.mapper.TaskMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskDto taskDto;
    private TaskCreateRequest createRequest;
    private TaskUpdateRequest updateRequest;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Setup test data
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.PENDING);
        task.setDueDate(now.plusDays(7));
        task.setCreatedAt(now);
        task.setUpdatedAt(now);

        taskDto = new TaskDto();
        taskDto.setId(1L);
        taskDto.setTitle("Test Task");
        taskDto.setDescription("Test Description");
        taskDto.setStatus(TaskStatus.PENDING);
        taskDto.setDueDate(now.plusDays(7));
        taskDto.setCreatedAt(now);
        taskDto.setUpdatedAt(now);

        createRequest = new TaskCreateRequest();
        createRequest.setTitle("New Task");
        createRequest.setDescription("New Description");
        createRequest.setDueDate(now.plusDays(14));

        updateRequest = new TaskUpdateRequest();
        updateRequest.setTitle("Updated Task");
        updateRequest.setDescription("Updated Description");
        updateRequest.setStatus(TaskStatus.IN_PROGRESS);
        updateRequest.setDueDate(now.plusDays(10));
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        // Arrange
        List<Task> tasks = List.of(task);
        when(taskRepository.findAll()).thenReturn(tasks);

        try (MockedStatic<TaskMapper> mockedMapper = mockStatic(TaskMapper.class)) {
            mockedMapper.when(() -> TaskMapper.toDto(any(Task.class))).thenReturn(taskDto);

            // Act
            List<TaskDto> result = taskService.getAllTasks();

            // Assert
            assertEquals(1, result.size());
            assertEquals(taskDto, result.getFirst());
            verify(taskRepository).findAll();
            mockedMapper.verify(() -> TaskMapper.toDto(task));
        }
    }

    @Test
    void getTaskById_WhenTaskExists_ShouldReturnTask() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        try (MockedStatic<TaskMapper> mockedMapper = mockStatic(TaskMapper.class)) {
            mockedMapper.when(() -> TaskMapper.toDto(task)).thenReturn(taskDto);

            // Act
            TaskDto result = taskService.getTaskById(1L);

            // Assert
            assertEquals(taskDto, result);
            verify(taskRepository).findById(1L);
            mockedMapper.verify(() -> TaskMapper.toDto(task));
        }
    }

    @Test
    void getTaskById_WhenTaskDoesNotExist_ShouldThrowException() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(999L));
        verify(taskRepository).findById(999L);
    }

    @Test
    void createTask_ShouldCreateAndReturnTask() {
        // Arrange
        Task newTask = new Task();
        newTask.setTitle("New Task");

        try (MockedStatic<TaskMapper> mockedMapper = mockStatic(TaskMapper.class)) {
            mockedMapper.when(() -> TaskMapper.toEntity(createRequest)).thenReturn(newTask);
            when(taskRepository.save(any(Task.class))).thenReturn(task);
            mockedMapper.when(() -> TaskMapper.toDto(task)).thenReturn(taskDto);

            // Act
            TaskDto result = taskService.createTask(createRequest);

            // Assert
            assertEquals(taskDto, result);
            mockedMapper.verify(() -> TaskMapper.toEntity(createRequest));
            verify(taskRepository).save(any(Task.class));
            mockedMapper.verify(() -> TaskMapper.toDto(task));
        }
    }

    @Test
    void updateTask_WhenTaskExists_ShouldUpdateAndReturnTask() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        try (MockedStatic<TaskMapper> mockedMapper = mockStatic(TaskMapper.class)) {
            mockedMapper.when(() -> TaskMapper.toDto(task)).thenReturn(taskDto);

            // Act
            TaskDto result = taskService.updateTask(1L, updateRequest);

            // Assert
            assertEquals(taskDto, result);
            verify(taskRepository).findById(1L);
            verify(taskRepository).save(task);
            mockedMapper.verify(() -> TaskMapper.toDto(task));

            // Verify task was updated with values from the request
            assertEquals(updateRequest.getTitle(), task.getTitle());
            assertEquals(updateRequest.getDescription(), task.getDescription());
            assertEquals(updateRequest.getStatus(), task.getStatus());
            assertEquals(updateRequest.getDueDate(), task.getDueDate());
        }
    }

    @Test
    void updateTask_WhenTaskDoesNotExist_ShouldThrowException() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(999L, updateRequest));
        verify(taskRepository).findById(999L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_WhenTaskExists_ShouldDeleteTask() {
        // Arrange
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        // Act
        taskService.deleteTask(1L);

        // Assert
        verify(taskRepository).existsById(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void deleteTask_WhenTaskDoesNotExist_ShouldThrowException() {
        // Arrange
        when(taskRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(999L));
        verify(taskRepository).existsById(999L);
        verify(taskRepository, never()).deleteById(any());
    }
}
