package com.radmiy.task_manager_demo.repository;

import com.radmiy.task_manager_demo.repository.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/**
 * Repository interface for accessing and modifying task data.
 * Defines low-level CRUD operations for persistence layer.
 */
public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {

}
