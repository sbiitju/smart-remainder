package com.sbiitju.smartremainder.ClassSave;

public class ExamSchedule {
    private String date;
    private String time;
    private String examdescription;

    public ExamSchedule(String date, String time, String examdescription) {
        this.date = date;
        this.time = time;
        this.examdescription = examdescription;
    }

    public ExamSchedule() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExamdescription() {
        return examdescription;
    }

    public void setExamdescription(String examdescription) {
        this.examdescription = examdescription;
    }
}
