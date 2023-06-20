package org.example.model;

import java.sql.Date;
import java.time.LocalDate;

public class Event {
    private int Id;
    private LocalDate processingTime;
    private String startTime;
    private String endTime;
    private String eventType;
    private String eventDescription;

    public Event(int id, LocalDate processingTime, String startTime, String endTime, String eventType, String eventDescription) {
        Id = id;
        this.processingTime = processingTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventType = eventType;
        this.eventDescription = eventDescription;
    }

    public Event(LocalDate processingTime, String startTime, String endTime, String eventType, String eventDescription) {
        this.processingTime = processingTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventType = eventType;
        this.eventDescription = eventDescription;
    }

    public Event(LocalDate processingTime) {
        this.processingTime = processingTime;
    }

    public Event() {

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public LocalDate getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(LocalDate processingTime) {
        this.processingTime = processingTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
