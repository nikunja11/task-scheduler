
package com.scheduler.core;

import com.scheduler.repo.InMemoryTaskRepository;
import com.scheduler.state.TaskState;
import java.time.LocalDateTime;
import java.util.UUID;

public class TaskExecutionContext<I, O> {
    private final UUID id;
    private final Task<I,O> task;
    private final InMemoryTaskRepository<I,O> taskRepository;

    private TaskState<I,O> state;

    private I input;
    private O output;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int attemptNumber = 1;
    private Exception error;

    public TaskExecutionContext(Task<I,O> task, I input, InMemoryTaskRepository<I,O> taskRepository) {
        this.id = UUID.randomUUID();
        this.taskRepository = taskRepository;
        this.task = task;
        this.input = input;
    }

    public void setState(TaskState<I,O> state) {
        this.state = state;
    }

    public void setOutput(O output) {
        this.output = output;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void incrementAttemptNumber() {
        this.attemptNumber++;
    }

    public void setError(Exception error) {
        this.error = error;
    }

    public Exception getError() {
        return this.error;
    }

    // getters

    public UUID getId() {
        return id;
    }

    public Task<I, O> getTask() {
        return task;
    }


    public int getAttemptNumber() {
        return attemptNumber;
    }

    public TaskState<I,O> getState() {
        return state;
    }

    public I getInput() {
        return input;
    }

    public O getOutput() {
        return output;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public  InMemoryTaskRepository<I,O> getTaskRepository() {
        return taskRepository;
    }

}
