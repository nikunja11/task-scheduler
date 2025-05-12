package com.scheduler.core;

/**
 * Interface for providing input to scheduled tasks.
 */
public interface InputProvider<I> {
    /**
     * Generates input for a task when it's scheduled to run.
     * @return The input for the task
     */
    I generateInput();
}