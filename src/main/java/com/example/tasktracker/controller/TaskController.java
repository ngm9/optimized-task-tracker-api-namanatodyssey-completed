package com.example.tasktracker.controller;
import com.example.tasktracker.service.TaskService;
import com.example.tasktracker.model.Task;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("")
    public CompletableFuture<List<Task>> list() {
        return taskService.getAll();
    }
    @PostMapping("")
    public Task add(@RequestBody Task t) {
        return taskService.add(t);
    }
    @GetMapping("/stats")
    public CompletableFuture<Map<String, Object>> stats() {
        // Async: request thread is released while stats are being computed
        return taskService.calculateStats();
    }
}
