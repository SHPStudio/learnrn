package com.learnrn1.calendar;

import lombok.Data;

/**
 * 日历事件
 */
@Data
public class CalendarEvent {
    /**
     * 日历事件Id
     */
    private long calEventId;
    /**
     * 事件标题
     */
    private String title;
    /**
     * 事件备注
    */
    private String description;
    /**
     * 事件开始时间
     */
    private long startTime;
    /**
     * 事件结束时间
     */
    private long endTime;
    private boolean isAllDay;
    private boolean isAlarm;
}
