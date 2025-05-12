package com.scheduler.state;

import com.scheduler.core.TaskExecutionContext;

import java.time.LocalDateTime;

public abstract class AbstractTaskState<I,O> implements TaskState<I,O> {

    // marked final because it should not be overridden to honour template method pattern
    @Override
    public final void process(TaskExecutionContext<I,O> context) {
       if(canProcess(context)) {
            preProcess(context);
            doProcess(context);
            postProcess(context);
        }
    }
    
    protected void preProcess(TaskExecutionContext<I,O> context) {
        // Default implementation - can be overridden by subclasses
        context.setStartTime(LocalDateTime.now());
    }
    
    protected void postProcess(TaskExecutionContext<I,O> context) {
        // Record task execution
        context.setEndTime(LocalDateTime.now());
        
        // If we need to record in repository
        if (context.getTaskRepository() != null) {
            context.getTaskRepository().recordTaskExecution(context);
        }
        
        context.setState(this);
    }

    protected abstract void doProcess(TaskExecutionContext<I,O> context);

}
