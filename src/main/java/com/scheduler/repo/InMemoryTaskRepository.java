package com.scheduler.repo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.scheduler.core.Task;
import com.scheduler.core.TaskExecutionContext;

public class InMemoryTaskRepository<I,O> {

    private final Map<String, Task<I,O>> tasks = new ConcurrentHashMap<>();
    private final Map<String, List<TaskExecutionContext<I, O>>> taskExecutions = new ConcurrentHashMap<>();


    public void registerTask(Task<I,O> task) {
        tasks.put(task.getId(), task);
        taskExecutions.putIfAbsent(task.getId(), Collections.synchronizedList(new ArrayList<>()));
    }

    public void removeTask(Task<I, O> task) {
        System.out.println("Before removal - Repository size: " + getAllTasks().size());
        System.out.println("Repo size :" + getAllTasks().size());
        
        getAllTasks().stream()
            .forEach(t -> System.out.println("Task in repository: " + t.getId()));
        
        System.out.println("Removing task: " + task.getId());
        
        // Try removing by reference first
        boolean removed = getAllTasks().remove(task);
        
        // If that didn't work, try removing by ID
        if (!removed) {
            getAllTasks().removeIf(t -> t.getId().equals(task.getId()));
        }
        
        System.out.println("After removal - Repository size: " + getAllTasks().size());
    }

    public void recordTaskExecution(TaskExecutionContext<I, O> context) {
        String taskId = context.getTask().getId();
        taskExecutions.computeIfAbsent(taskId, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(context);
    }

    public Task<I,O> getTask(String taskId) {
        return tasks.get(taskId);
    }

    public Collection<Task<I,O>> getAllTasks() {
        return tasks.values();
    }

    public List<TaskExecutionContext<I, O>> getTaskExecutions(String taskId) {
        return taskExecutions.getOrDefault(taskId, Collections.emptyList());
    }

    public TaskExecutionContext<I, O> getLatestExecution(String taskId) {
        List<TaskExecutionContext<I, O>> executions = taskExecutions.getOrDefault(
                taskId, Collections.emptyList());
        
        if (executions.isEmpty()) {
            return null;
        }
        
        return executions.get(executions.size() - 1);
    }

    
}
