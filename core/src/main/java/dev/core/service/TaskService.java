package dev.core.service;

import dev.core.dto.TaskDTO;
import dev.core.mapper.TaskMapper;
import dev.core.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskService {

    private TaskRepository taskRepository;
    private TaskMapper taskMapper;

    public Page<TaskDTO> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable).map(taskMapper::toDto);
    }
}
