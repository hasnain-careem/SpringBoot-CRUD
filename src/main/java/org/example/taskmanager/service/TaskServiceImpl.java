package org.example.taskmanager.service;

import org.example.taskmanager.dto.TaskCreateRequest;
import org.example.taskmanager.dto.TaskDto;
import org.example.taskmanager.dto.TaskUpdateRequest;
import org.example.taskmanager.exception.TaskNotFoundException;
import org.example.taskmanager.model.Task;
import org.example.taskmanager.model.TaskStatus;
import org.example.taskmanager.repository.TaskRepository;
import org.example.taskmanager.util.TaskMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
        return taskMapper.toDto(task);
    }

    @Override
    public TaskDto createTask(TaskCreateRequest request) {
        Task task = taskMapper.toEntity(request);
        task.setStatus(TaskStatus.PENDING); // default status
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public TaskDto updateTask(Long id, TaskUpdateRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setStatus(request.getStatus());

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task with ID " + id + " not found");
        }
        taskRepository.deleteById(id);
    }
}
