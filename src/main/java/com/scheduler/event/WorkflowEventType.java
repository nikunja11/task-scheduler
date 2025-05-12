package com.scheduler.event;

public enum WorkflowEventType {
    // Task lifecycle events
    TASK_CREATED,
    TASK_SCHEDULED,
    TASK_STARTED,
    TASK_COMPLETED,
    TASK_FAILED,
    
    // Workflow lifecycle events
    WORKFLOW_CREATED,
    WORKFLOW_STARTED,
    WORKFLOW_COMPLETED,
    WORKFLOW_FAILED,
    
    // System events
    SCHEDULER_STARTED,
    SCHEDULER_STOPPED,
    EXECUTOR_STARTED,
    EXECUTOR_STOPPED,
    
    // Custom events
    CUSTOM_EVENT
}