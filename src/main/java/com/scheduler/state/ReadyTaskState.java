package com.scheduler.state;

import com.scheduler.core.TaskExecutionContext;

public class ReadyTaskState<I,O> extends AbstractTaskState<I,O> {

    @Override
    protected void doProcess(TaskExecutionContext<I,O> context) {
        context.setState(new RunningTaskState<I,O>());
        context.getState().process(context);
    }

}
