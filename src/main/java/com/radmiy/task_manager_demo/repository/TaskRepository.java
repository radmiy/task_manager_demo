package com.radmiy.task_manager_demo.repository;

import com.radmiy.task_manager_demo.repository.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for accessing and modifying task data.
 * Defines low-level CRUD operations for persistence layer.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {

    List<Task> findByAuthorId(UUID userId);
}
