package com.sbiitju.smartremainder.ClassSave;

public class Value {
    String code;
    String hour;
    String min;
    String description;

    public Value(String code, String hour, String min, String description) {
        this.code = code;
        this.hour = hour;
        this.min = min;
        this.description = description;
    }

    public Value() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
