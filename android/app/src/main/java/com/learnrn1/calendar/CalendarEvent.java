package com.learnrn1.calendar;

import lombok.Data;

/**
 * 日历事件
 */
@Data
public class CalendarEvent {
    private long calEventId;
    private String title;
    private String description;
    private long startTime;
    private long endTime;
    private boolean isAllDay;
    private boolean isAlarm;
}
