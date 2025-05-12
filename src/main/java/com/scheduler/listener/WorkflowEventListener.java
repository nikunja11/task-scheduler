package com.scheduler.listener;

import com.scheduler.event.WorkflowEvent;
import com.scheduler.event.WorkflowEventType;

public interface WorkflowEventListener {
    /**
     * Called when an event this listener is interested in occurs
     */
    void onEvent(WorkflowEvent event);
    
    /**
     * Determines if this listener is interested in the given event type
     */
    default boolean isInterestedIn(WorkflowEventType eventType) {
        return true; // By default, listen to all events
    }
}