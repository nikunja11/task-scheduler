package com.scheduler.core;

import java.util.function.Function;

public class Task<I, O> {
    private final String id;
    private final Function<I, O> fn;
    private final TaskType type;
    private final String cronExpression;
    private final int retries;
    private final int retryDelay;
    private final InputProvider<I> inputProvider;

    private Task(Builder<I, O> builder) {
        this.id = builder.id;
        this.fn = builder.fn;
        this.type = builder.type;
        this.cronExpression = builder.cronExpression;
        this.retries = builder.retries;
        this.retryDelay = builder.retryDelay;
        this.inputProvider = builder.inputProvider;
    }

    public String getId() {
        return id;
    }

    public TaskType getType() {
        return type;
    }

    public String getCronExpression() {
        return cronExpression;
    }
    
    public int getRetries() {
        return retries;
    }
    
    public long getRetryDelayMs() {
        return retryDelay;
    }
    
    public InputProvider<I> getInputProvider() {
        return inputProvider;
    }
    
    public boolean hasInputProvider() {
        return inputProvider != null;
    }

    public O execute(I input) {
        return fn.apply(input);
    }

    public static enum TaskType {
        SIMPLE,
        CRON
    }

    public static class Builder<I,O> {
        private final String id;
        private Function<I, O> fn;
        private TaskType type = TaskType.SIMPLE;
        private String cronExpression = "";
        private int retries = 0;
        private int retryDelay = 1000;
        private InputProvider<I> inputProvider;

        public Builder(String id) {
            this.id = id;
        }

        public Builder<I, O> fn(Function<I, O> fn) {
            this.fn = fn;
            return this;
        }

        public Builder<I, O> type(TaskType type) {
            this.type = type;
            return this;
        }

        public Builder<I, O> cronExpression(String cronExpression) {
            this.cronExpression = cronExpression;
            return this;
        }
        
        public Builder<I, O> retries(int retries) {
            this.retries = retries;
            return this;
        }
        
        public Builder<I, O> retryDelay(int retryDelay) {
            this.retryDelay = retryDelay;
            return this;
        }
        
        public Builder<I, O> inputProvider(InputProvider<I> inputProvider) {
            this.inputProvider = inputProvider;
            return this;
        }
        
        public Task<I, O> build() {
            if (fn == null) {
                throw new IllegalStateException("Task function cannot be null");
            }
            return new Task<>(this);
        }
    }
    
}
