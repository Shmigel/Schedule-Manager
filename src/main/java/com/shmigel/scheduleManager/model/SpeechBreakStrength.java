package com.shmigel.scheduleManager.model;

public enum SpeechBreakStrength {
    NONE("none"), X_WEAK("x-weak"), WEAK("WEAK"),
    MEDIUM("medium"), STRONG("strong"), X_STRONG("x-strong");

    String value;

    SpeechBreakStrength(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
