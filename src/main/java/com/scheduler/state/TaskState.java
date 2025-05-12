package com.scheduler.state;

import com.scheduler.core.TaskExecutionContext;

public interface TaskState<I,O> {
    void process(TaskExecutionContext<I,O> context);
    default boolean canProcess(TaskExecutionContext<I,O> context) {
        return true;
    }
}
