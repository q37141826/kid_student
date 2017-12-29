package com.fxtx.framework.weekCalender;

public class BeWeekReck {
    private String objectId;
    private String startTime, endTime;
    private int week;
    private Object data;

    public BeWeekReck(String objectId, String startTime, String endTime, int week, Object data) {
        super();
        this.objectId = objectId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.week = week;
        this.data = data;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getWeek() {
        return week;
    }

    public String getObjectId() {
        return objectId;
    }

    public Object getData() {
        return data;
    }
}
