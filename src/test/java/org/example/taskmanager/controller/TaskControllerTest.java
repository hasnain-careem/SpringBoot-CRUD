package org.example.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.taskmanager.dto.TaskCreateRequest;
import org.example.taskmanager.dto.TaskDto;
import org.example.taskmanager.dto.TaskUpdateRequest;
import org.example.taskmanager.exception.TaskNotFoundException;
import org.example.taskmanager.model.TaskStatus;
import org.example.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TaskService taskService() {
            return mock(TaskService.class);
        }

        @Bean
        public MethodValidationPostProcessor methodValidationPostProcessor() {
            return new MethodValidationPostProcessor();
        }
    }

    @Autowired
    private TaskService taskService;

    private TaskDto taskDto;
    private TaskCreateRequest createRequest;
    private TaskUpdateRequest updateRequest;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Setup test data
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
    void getAllTasks_ShouldReturnTasks() throws Exception {
        // Arrange
        List<TaskDto> tasks = List.of(taskDto);
        when(taskService.getAllTasks()).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test Task")))
                .andExpect(jsonPath("$[0].description", is("Test Description")))
                .andExpect(jsonPath("$[0].status", is("PENDING")));

        verify(taskService).getAllTasks();
    }

    @Test
    void getTaskById_WhenTaskExists_ShouldReturnTask() throws Exception {
        // Arrange
        when(taskService.getTaskById(1L)).thenReturn(taskDto);

        // Act & Assert
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Task")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.status", is("PENDING")));

        verify(taskService).getTaskById(1L);
    }

    @Test
    void getTaskById_WhenTaskDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(taskService.getTaskById(999L)).thenThrow(new TaskNotFoundException("Task with ID 999 not found"));

        // Act & Assert
        mockMvc.perform(get("/tasks/999"))
                .andExpect(status().isNotFound());

        verify(taskService).getTaskById(999L);
    }

    @Test
    void createTask_WithValidRequest_ShouldCreateAndReturnTask() throws Exception {
        // Arrange
        when(taskService.createTask(any(TaskCreateRequest.class))).thenReturn(taskDto);

        // Act & Assert
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Task")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.status", is("PENDING")));

        verify(taskService).createTask(any(TaskCreateRequest.class));
    }

    @Test
    void createTask_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Arrange - invalid request with empty title
        createRequest.setTitle("");

        // Act & Assert
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any(TaskCreateRequest.class));
    }

    @Test
    void updateTask_WithValidRequest_ShouldUpdateAndReturnTask() throws Exception {
        // Arrange
        when(taskService.updateTask(eq(1L), any(TaskUpdateRequest.class))).thenReturn(taskDto);

        // Act & Assert
        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Task")));

        verify(taskService).updateTask(eq(1L), any(TaskUpdateRequest.class));
    }

    @Test
    void updateTask_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Arrange - invalid request with empty title
        updateRequest.setTitle("");

        // Reset and set up the mock since we're using a TestConfig with predefined mocks
        reset(taskService);

        // Act & Assert
        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());

        // Verify that the service method was never called
        verify(taskService, never()).updateTask(anyLong(), any(TaskUpdateRequest.class));
    }

    @Test
    void updateTask_WhenTaskDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(taskService.updateTask(eq(999L), any(TaskUpdateRequest.class)))
                .thenThrow(new TaskNotFoundException("Task with ID 999 not found"));

        // Act & Assert
        mockMvc.perform(put("/tasks/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());

        verify(taskService).updateTask(eq(999L), any(TaskUpdateRequest.class));
    }

    @Test
    void deleteTask_WhenTaskExists_ShouldDeleteAndReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(taskService).deleteTask(1L);

        // Act & Assert
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(1L);
    }

    @Test
    void deleteTask_WhenTaskDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        doThrow(new TaskNotFoundException("Task with ID 999 not found"))
                .when(taskService).deleteTask(999L);

        // Act & Assert
        mockMvc.perform(delete("/tasks/999"))
                .andExpect(status().isNotFound());

        verify(taskService).deleteTask(999L);
    }
}
