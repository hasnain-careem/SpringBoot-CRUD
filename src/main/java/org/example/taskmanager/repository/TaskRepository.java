package org.example.taskmanager.repository;

import org.example.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // No need to write implementation – JpaRepository provides:
    // findAll(), findById(), save(), deleteById(), existsById(), etc.
}
