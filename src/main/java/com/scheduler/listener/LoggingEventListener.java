package com.scheduler.listener;

import java.util.logging.Logger;

import com.scheduler.event.WorkflowEvent;
import com.scheduler.event.WorkflowEventType;

public class LoggingEventListener implements WorkflowEventListener {
    private static final Logger logger = Logger.getLogger(LoggingEventListener.class.getName());
    
    @Override
    public void onEvent(WorkflowEvent event) {
        logger.info("Event received: " + event);
    }
    
    @Override
    public boolean isInterestedIn(WorkflowEventType eventType) {
        // This listener is interested in all events
        return true;
    }
}