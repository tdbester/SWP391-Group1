package org.example.talentcenter.utilities;

public enum Level {
    BEGINNER("Cơ bản"),
    INTERMEDIATE("Trung cấp"),
    ADVANCED("Nâng cao");

    private final String displayName;

    Level(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}