package com.radmiy.task_manager_demo.service.impl;

import com.radmiy.task_manager_demo.dto.TaskFilterDto;
import com.radmiy.task_manager_demo.dto.TaskRequestDto;
import com.radmiy.task_manager_demo.dto.TaskResponseDto;
import com.radmiy.task_manager_demo.dto.UserDto;
import com.radmiy.task_manager_demo.mapper.TaskMapper;
import com.radmiy.task_manager_demo.mapper.TaskMapperImpl;
import com.radmiy.task_manager_demo.mapper.TaskMapperImpl_;
import com.radmiy.task_manager_demo.mapper.UserMapper;
import com.radmiy.task_manager_demo.repository.TaskRepository;
import com.radmiy.task_manager_demo.repository.UserRepository;
import com.radmiy.task_manager_demo.repository.model.Task;
import com.radmiy.task_manager_demo.repository.model.TaskPriority;
import com.radmiy.task_manager_demo.repository.model.TaskStatus;
import com.radmiy.task_manager_demo.repository.model.User;
import com.radmiy.task_manager_demo.repository.model.UserRole;
import com.radmiy.task_manager_demo.repository.specification.TaskFilterFactory;
import com.radmiy.task_manager_demo.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskMapperImpl taskMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    private UserDto radmiyUserDto;
    private User radmiyUser;
    private Task task;
    private TaskRequestDto taskRequestDto;
    private TaskResponseDto taskResponseDto;

    @BeforeEach
    public void setUp() {
        initUser();
        initTask();
        initAuth();
    }

    @Test
    void createTaskTest() {
        //given
        when(taskRepository.saveAndFlush(any())).thenReturn(task);
        when(taskMapper.toDto(any())).thenReturn(taskResponseDto);
        when(taskMapper.toEntity(any())).thenReturn(task);

        //when
        TaskResponseDto taskResponseDto = taskService.create(taskRequestDto);

        //then
        assertNotNull(taskResponseDto);
        verify(taskMapper).toEntity(taskRequestDto);
        verify(taskMapper).toDto(task);
    }

    @Test
    void getTaskByIdTest() {
        //given
        when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        when(taskMapper.toDto(any())).thenReturn(taskResponseDto);

        //when
        TaskResponseDto actual = taskService.getTaskById(taskRequestDto.getId());

        //then
        assertNotNull(actual);
        assertEquals(taskRequestDto.getId(), actual.getId());
        verify(taskRepository).findById(taskRequestDto.getId());
        verify(taskMapper).toDto(task);

    }

    @Test
    void updateTest() {
        //given
        when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        doNothing().when(taskMapper)
                .updateEntityFromDto(isA(TaskRequestDto.class), isA(Task.class));
        when(taskMapper.toDto(any())).thenReturn(taskResponseDto);

        //when
        TaskResponseDto actual = taskService.update(taskRequestDto, taskRequestDto.getId());

        //then
        assertNotNull(actual);
        verify(taskRepository).findById(any());
        verify(taskMapper).updateEntityFromDto(isA(TaskRequestDto.class), isA(Task.class));
        verify(taskMapper).toDto(any());
    }

    @Test
    void deleteTest() {
        //given
        doNothing().when(taskRepository).deleteById(any());

        //when
        taskService.delete(taskRequestDto.getId());

        //then
        verify(taskRepository).deleteById(any());
    }

    @Test
    void getTasksTest() {
        //given
        TaskFilterDto taskFilterDto = TaskFilterDto.builder()
                .status(TaskStatus.TODO)
                .author(taskRequestDto.getAuthor())
                .assignee(taskRequestDto.getAssignee())
                .build();
        when(taskRepository.findAll(isA(Specification.class))).thenReturn(List.of(task));
        when(taskMapper.toDto(any())).thenReturn(taskResponseDto);

        //when
        List<TaskResponseDto> actual = taskService.getTasks(taskFilterDto);

        //then
        assertNotNull(actual);
        verify(taskRepository).findAll(isA(Specification.class));
        verify(taskMapper).toDto(any());
    }

    private void initUser() {
        radmiyUser = User.builder()
                .id(UUID.randomUUID())
                .username("radmiy")
                .email("radmiy@example.com")
                .password("radmiy")
                .role(UserRole.USER)
                .build();

        radmiyUserDto = UserDto.builder()
                .id(radmiyUser.getId())
                .username(radmiyUser.getUsername())
                .email(radmiyUser.getEmail())
                .build();
    }

    private void initTask() {
        task = Task.builder()
                .id(UUID.randomUUID())
                .title("Test")
                .description("Test")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.LOW)
                .author(radmiyUser)
                .assignee(radmiyUser)
                .build();

        taskRequestDto = TaskRequestDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(radmiyUser.getId())
                .assignee(radmiyUser.getId())
                .build();

        taskResponseDto = TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(radmiyUserDto)
                .assignee(radmiyUserDto)
                .build();
    }

    private void initAuth() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                radmiyUser, null, radmiyUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}