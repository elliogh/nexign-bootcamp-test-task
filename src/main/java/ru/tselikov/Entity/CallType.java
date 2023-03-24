package ru.tselikov.Entity;

public enum CallType {
    OUTGOING("01"),
    INCOMING("02");

    private final String code;

    CallType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
