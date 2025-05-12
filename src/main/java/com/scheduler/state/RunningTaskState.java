package com.scheduler.state;

import com.scheduler.core.Task;
import com.scheduler.core.TaskExecutionContext;
import com.scheduler.event.TaskEventPublisher;

public class RunningTaskState<I,O> extends AbstractTaskState<I,O> {

    private final TaskEventPublisher eventPublisher = new TaskEventPublisher();

    @Override
    protected void doProcess(TaskExecutionContext<I,O> context) {
        // Publish task started event
        eventPublisher.publishTaskStarted(context);

        try {
            Task<I, O> task = context.getTask();
            O output = task.execute(context.getInput());
            context.setOutput(output);
            context.setState(new SuccessTaskState<I, O>());
            context.getState().process(context);
        } catch (Exception e) {
            context.setState(new FailedState<I,O>());
            context.getState().process(context);
        }
    }
    
}