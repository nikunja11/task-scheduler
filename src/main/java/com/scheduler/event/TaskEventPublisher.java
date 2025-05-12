package com.scheduler.event;

import com.scheduler.core.Task;
import com.scheduler.core.TaskExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class TaskEventPublisher {
    private final WorkflowEventBus eventBus;
    
    public TaskEventPublisher() {
        this.eventBus = WorkflowEventBus.getInstance();
    }
    
    public <I, O> void publishTaskCreated(Task<I, O> task) {
        Map<String, Object> data = new HashMap<>();
        data.put("taskId", task.getId());
        data.put("taskType", task.getType());
        
        WorkflowEvent event = new WorkflowEvent(WorkflowEventType.TASK_CREATED, data);
        eventBus.publish(event);
    }
    
    public <I, O> void publishTaskStarted(TaskExecutionContext<I, O> context) {
        Map<String, Object> data = new HashMap<>();
        data.put("taskId", context.getTask().getId());
        data.put("input", context.getInput());
        
        WorkflowEvent event = new WorkflowEvent(WorkflowEventType.TASK_STARTED, data);
        eventBus.publish(event);
    }
    
    public <I, O> void publishTaskCompleted(TaskExecutionContext<I, O> context) {
        Map<String, Object> data = new HashMap<>();
        data.put("taskId", context.getTask().getId());
        data.put("input", context.getInput());
        data.put("output", context.getOutput());
        data.put("startTime", context.getStartTime());
        data.put("endTime", context.getEndTime());
        
        WorkflowEvent event = new WorkflowEvent(WorkflowEventType.TASK_COMPLETED, data);
        eventBus.publish(event);
    }
    
    public <I, O> void publishTaskFailed(TaskExecutionContext<I, O> context, Exception error) {
        Map<String, Object> data = new HashMap<>();
        data.put("taskId", context.getTask().getId());
        data.put("input", context.getInput());
        data.put("error", error.getMessage());
        
        WorkflowEvent event = new WorkflowEvent(WorkflowEventType.TASK_FAILED, data);
        eventBus.publish(event);
    }
}