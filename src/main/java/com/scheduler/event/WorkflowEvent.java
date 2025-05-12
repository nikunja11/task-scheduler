package com.scheduler.event;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WorkflowEvent {
    private final String id;
    private final WorkflowEventType type;
    private final LocalDateTime timestamp;
    private final Map<String, Object> data;
    
    public WorkflowEvent(WorkflowEventType type) {
        this(type, new HashMap<>());
    }
    
    public WorkflowEvent(WorkflowEventType type, Map<String, Object> data) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }
    
    public String getId() {
        return id;
    }
    
    public WorkflowEventType getType() {
        return type;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public Map<String, Object> getData() {
        return data;
    }
    
    public void addData(String key, Object value) {
        data.put(key, value);
    }
    
    public Object getData(String key) {
        return data.get(key);
    }
    
    @Override
    public String toString() {
        return "WorkflowEvent{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", timestamp=" + timestamp +
                ", data=" + data +
                '}';
    }
}