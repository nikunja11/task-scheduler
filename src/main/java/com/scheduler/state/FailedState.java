package com.scheduler.state;

import com.scheduler.core.TaskExecutionContext;
import com.scheduler.event.TaskEventPublisher;

class FailedState<I,O> extends AbstractTaskState<I,O> {

    private final TaskEventPublisher eventPublisher = new TaskEventPublisher();

    @Override
    protected void doProcess(TaskExecutionContext<I,O> context) {

        // Get the error from the context
        
         Exception error = context.getError();
        if (error == null) {
           error = new RuntimeException("Unknown error");
        }
               
               
        // Publish task failed event
        eventPublisher.publishTaskFailed(context, error);

        System.err.println("Task failed for input" + context.getInput());
    }

}