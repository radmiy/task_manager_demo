package com.radmiy.task_manager_demo.controller;

import com.radmiy.task_manager_demo.dto.TaskFilterDto;
import com.radmiy.task_manager_demo.dto.TaskRequestDto;
import com.radmiy.task_manager_demo.dto.TaskResponseDto;
import com.radmiy.task_manager_demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<TaskResponseDto> create(@RequestBody TaskRequestDto dto) {
        log.debug("Create Task: {}", dto);
        return ResponseEntity.ok().body(taskService.create(dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable UUID id) {
        log.debug("GET task by id: {}", id);
        return ResponseEntity.ok().body(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@taskSecurity.canUpdateTask(#id, principal)")
    public ResponseEntity<TaskResponseDto> update(@RequestBody TaskRequestDto dto, @PathVariable UUID id) {
        log.debug("Update task by id: {}", id);
        return ResponseEntity.ok().body(taskService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@taskSecurity.canUpdateTask(#id, principal)")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.debug("DELETE task by id: {}", id);
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<TaskResponseDto>> getTasks(@ModelAttribute TaskFilterDto filterDto) {
        log.debug("GET tasks");
        return ResponseEntity.ok().body(taskService.getTasks(filterDto));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<TaskResponseDto>> searchTasks(
            @ModelAttribute TaskFilterDto filterDto,
            @PageableDefault(page = 0, size = 10, sort = "author", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        log.debug("SEARCH tasks");
        return ResponseEntity.ok().body(taskService.search(filterDto, pageable));
    }
}
