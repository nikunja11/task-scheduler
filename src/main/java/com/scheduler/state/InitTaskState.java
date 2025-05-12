package com.scheduler.state;

import com.scheduler.core.TaskExecutionContext;

public class InitTaskState<I,O> extends AbstractTaskState<I,O> {
    
    @Override
    protected void doProcess(TaskExecutionContext<I,O> context) {
        // Initialize the task
        // Check dependencies
        // Check resources
        // Check parameters
        // etc.
        // Transition to READY state
        context.setState(new ReadyTaskState<I,O>());
    }
}

