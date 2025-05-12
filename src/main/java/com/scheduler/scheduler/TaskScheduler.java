package com.scheduler.scheduler;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.scheduler.core.Task;
import com.scheduler.core.Task.TaskType;
import com.scheduler.core.TaskExecutionContext;
import com.scheduler.core.TaskExecutor;
import com.scheduler.repo.InMemoryTaskRepository;
import com.scheduler.state.ReadyTaskState;

public class TaskScheduler<I, O> {

    private final InMemoryTaskRepository<I, O> repository;
    private final TaskExecutor<I, O> executor;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public TaskScheduler(InMemoryTaskRepository<I, O> repository, TaskExecutor<I, O> executor) {
        this.repository = repository;
        this.executor = executor;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::scheduleTasks, 0, 30, TimeUnit.SECONDS);
    }

    private void scheduleTasks() {
        System.out.println("Scheduling tasks...");
        for (Task<I, O> task : repository.getAllTasks()) {
            if (task.getType() == TaskType.CRON && isDueNow(task.getCronExpression()) 
            || task.getType() == TaskType.SIMPLE) {

                System.out.println("Scheduling task: " + task.getId());
                // Generate input if an input provider is available
                I input = null;
                if (task.hasInputProvider()) {
                    input = task.getInputProvider().generateInput();
                }

                System.out.println("Input: " + input);
                
                // Create execution context with the generated input
                // We were not passing the task repo reference in TaskExecutionContext 
                // In TaskExecutionContext we were creating a new InMemoryTaskRepository
                //TaskExecutionContext<I, O> context = new TaskExecutionContext<>(task, input);

                TaskExecutionContext<I, O> context = new TaskExecutionContext<>(task, input, repository);
                context.setState(new ReadyTaskState<>());
                executor.submitTask(context);
            }
        }
    }

    

    // Stub: you should use a real cron library like cron-utils or Quartz
    private boolean isDueNow(String cronExpression) {
        // Simple demo: run every 5 seconds
        return Instant.now().getEpochSecond() % 5 == 0;
    }

    public void stop() {
        scheduler.shutdown();
    }
}

