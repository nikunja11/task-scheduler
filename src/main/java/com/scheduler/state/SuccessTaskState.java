package com.scheduler.state;

import java.util.HashMap;
import java.util.Map;

import com.scheduler.core.Task.TaskType;
import com.scheduler.core.TaskExecutionContext;
import com.scheduler.event.TaskEventPublisher;

public class SuccessTaskState<I,O> extends AbstractTaskState<I,O> {

    private final TaskEventPublisher eventPublisher = new TaskEventPublisher();

    @Override
    protected void doProcess(TaskExecutionContext<I,O> context) {

        // Publish task completed event
        eventPublisher.publishTaskCompleted(context);
        
        System.out.println("Task successful for input" + context.getInput());
        System.out.println("Output: ");
        System.out.println("----------------------------------------------------");
        System.out.println(context.getOutput());
        System.out.println(context.getOutput().getClass());

        if(context.getOutput() == null) {
            throw new RuntimeException("Output is null");
        } else if(context.getOutput().getClass() == HashMap.class) {
            ((Map<?, ?>) context.getOutput()).entrySet().stream()
            .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
        
        }
        //System.out.println(context.getOutput());
        
        // do nothing. things would be taken care in postProcess method
        // may be something unique related to SUCCESS state because preProcess
        // and postProcess is common for all the states
        // We might want to notify successful execution.
        // we do not want to notify each state change may be we just want to audit
        // so audit specific implementation would be added in Abstract class.

        
        // Remove SIMPLE tasks after successful execution
        if (context.getTask().getType() == TaskType.SIMPLE && 
            context.getTaskRepository() != null) {
            
            context.getTaskRepository().removeTask(context.getTask());
            
        }

    }   

}
