package dev.core.service;

import dev.core.domain.Task;
import dev.core.domain.User;
import dev.core.dto.TaskDTO;
import dev.core.mapper.TaskMapper;
import dev.core.repository.TaskRepository;
import jakarta.persistence.criteria.Join;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskService {

    private TaskRepository taskRepository;
    private TaskMapper taskMapper;
    private UserService userService;

    public Page<TaskDTO> findAll(Pageable pageable, Long userId) {
        Specification<Task> spec = Specification.where(null);

        if (userId != null) {
            spec = spec.and((root, query, builder) -> {
                Join<Task, User> taskUserJoin = root.join("user");
                return builder.equal(taskUserJoin.get("id"), userId);
            });
        }

        return taskRepository.findAll(spec, pageable).map(taskMapper::toDto);
    }

    public TaskDTO createTask(TaskDTO taskDTO, Long userId) {
        Task task = new Task();
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        User user = userService.findById(userId).orElseThrow();
        task.setUser(user);
        taskRepository.save(task);
        return taskMapper.toDto(task);
    }
}
