package dev.api.controller;

import dev.core.dto.TaskDTO;
import dev.core.service.TaskService;
import dev.security.MyUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private TaskService taskService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<TaskDTO>> findAll(Authentication authentication,
                                                 Pageable pageable) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        Page<TaskDTO> tasks = taskService.findAll(pageable, userDetails.getId());
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO, Authentication authentication) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        TaskDTO task = taskService.createTask(taskDTO, userDetails.getId());
        return ResponseEntity.ok(task);
    }
}
