package com.scheduler.event;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.scheduler.listener.WorkflowEventListener;

public class WorkflowEventBus {
    private static final Logger logger = Logger.getLogger(WorkflowEventBus.class.getName());
    private final Map<WorkflowEventType, List<WorkflowEventListener>> listeners;
    private final ExecutorService executorService;
    
    // Singleton instance
    private static WorkflowEventBus instance;
    
    public static synchronized WorkflowEventBus getInstance() {
        if (instance == null) {
            instance = new WorkflowEventBus();
        }
        return instance;
    }
    
    private WorkflowEventBus() {
        listeners = new EnumMap<>(WorkflowEventType.class);
        for (WorkflowEventType type : WorkflowEventType.values()) {
            listeners.put(type, new ArrayList<>());
        }
        executorService = Executors.newFixedThreadPool(2);
    }
    
    public void publish(WorkflowEvent event) {
        executorService.submit(() -> {
            List<WorkflowEventListener> typeListeners = listeners.get(event.getType());
            logger.info("Publishing event: " + event + " to " + typeListeners.size() + " listeners");
            
            for (WorkflowEventListener listener : typeListeners) {
                try {
                    listener.onEvent(event);
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error notifying listener", e);
                }
            }
        });
    }
    
    public void subscribe(WorkflowEventType eventType, WorkflowEventListener listener) {
        if (listener.isInterestedIn(eventType)) {
            listeners.get(eventType).add(listener);
            logger.info("Listener subscribed to " + eventType);
        }
    }
    
    public void subscribeToAll(WorkflowEventListener listener) {
        for (WorkflowEventType type : WorkflowEventType.values()) {
            if (listener.isInterestedIn(type)) {
                listeners.get(type).add(listener);
            }
        }
        logger.info("Listener subscribed to all applicable events");
    }
    
    public void unsubscribe(WorkflowEventType eventType, WorkflowEventListener listener) {
        listeners.get(eventType).remove(listener);
    }
    
    public void unsubscribeFromAll(WorkflowEventListener listener) {
        for (List<WorkflowEventListener> listenerList : listeners.values()) {
            listenerList.remove(listener);
        }
    }
    
    public void shutdown() {
        executorService.shutdown();
    }
}