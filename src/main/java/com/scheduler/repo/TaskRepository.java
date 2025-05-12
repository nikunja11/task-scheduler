package com.scheduler.repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.scheduler.core.Task;
import com.scheduler.core.TaskExecutionContext;

/**
 * In-memory repository for tasks and their execution contexts
 */
public class TaskRepository<I,O> {

    /*
     * Java does not support generic singletons. A singleton must be 
     * shared globally, but generics like <I, O> exist only at compile-time — 
     * they’re erased at runtime. 
     * 
     *private static final TaskRepository<I,O> INSTANCE = new TaskRepository<I,O>();

     */
    
    private final Map<String, Task<?, ?>> tasks;
    private final Map<String, List<TaskExecutionContext<?, ?>>> taskExecutions;
    
    private TaskRepository() {
        this.tasks = new ConcurrentHashMap<>();
        this.taskExecutions = new ConcurrentHashMap<>();
    }
    
    
    /**
     * Register a task
     */
    public void registerTask(Task<I, O> task) {
        tasks.put(task.getId(), task);
        taskExecutions.putIfAbsent(task.getId(), Collections.synchronizedList(new ArrayList<>()));
    }
    
    /**
     * Get a task by ID
     */
    @SuppressWarnings("unchecked")
    public Task<I, O> getTask(String taskId) {
        return (Task<I, O>) tasks.get(taskId);
    }
    
    /**
     * Record a task execution
     */
    public void recordTaskExecution(TaskExecutionContext<I, O> context) {
        String taskId = context.getTask().getId();
        taskExecutions.computeIfAbsent(taskId, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(context);
    }
    
    /**
     * Get all executions for a task
     */
    @SuppressWarnings("unchecked")
    public List<TaskExecutionContext<I, O>> getTaskExecutions(String taskId) {
        List<TaskExecutionContext<?, ?>> executions = taskExecutions.getOrDefault(
                taskId, Collections.emptyList());
        
        return executions.stream()
                .map(ctx -> (TaskExecutionContext<I, O>) ctx)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Get the latest execution for a task
     */
    @SuppressWarnings("unchecked")
    public TaskExecutionContext<I, O> getLatestExecution(String taskId) {
        List<TaskExecutionContext<?, ?>> executions = taskExecutions.getOrDefault(
                taskId, Collections.emptyList());
        
        if (executions.isEmpty()) {
            return null;
        }
        
        return (TaskExecutionContext<I, O>) executions.get(executions.size() - 1);
    }
    
    /**
     * Remove a task by ID
     */
    public void removeTask(String taskId) {
        tasks.remove(taskId);
        // Optionally keep the execution history
        // taskExecutions.remove(taskId);
    }
}
