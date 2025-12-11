package com.example.tasktracker.service;
import org.springframework.stereotype.Service;
import com.example.tasktracker.model.Task;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TaskService {
    private List<Task> tasks = new ArrayList<>();
    private List<Task> completedTasks = new ArrayList<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public CompletableFuture<List<Task>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            return new ArrayList<>(tasks);
        }, executorService);
    }
    public Task add(Task t) {
        // Check if task with this ID already exists
        if (t.getId() != null) {
            for (int i = 0; i < tasks.size(); i++) {
                if (t.getId().equals(tasks.get(i).getId())) {
                    // Update existing task
                    tasks.set(i, t);
                    return t;
                }
            }
        }
        
        // New task - generate ID and add
        t.setId(UUID.randomUUID().toString());
        tasks.add(t);
        if (t.isCompleted()) {
            // Memory inefficient: completedTasks grows endlessly
            completedTasks.add(t);
        }
        return t;
    }
    
    public CompletableFuture<Map<String, Object>> calculateStats() {
        return CompletableFuture.supplyAsync(() -> {
            // Runs on ExecutorService thread pool, freeing up the request thread
            int total = tasks.size();
            int completed = 0;
            for (Task t : tasks) {
                if (t.isCompleted()) completed++;
            }
            
            Map<String, Object> res = new HashMap<>();
            res.put("total", total);
            res.put("completed", completed);
            return res;
        }, executorService);
    }
}
