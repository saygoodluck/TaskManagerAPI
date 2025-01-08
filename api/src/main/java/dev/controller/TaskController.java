package dev.controller;

import dev.core.dto.TaskDTO;
import dev.core.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private TaskService taskService;

    @GetMapping
    public ResponseEntity<Page<TaskDTO>> findAll(Pageable pageable) {
        Page<TaskDTO> tasks = taskService.findAll(pageable);
        return ResponseEntity.ok(tasks);
    }
}
