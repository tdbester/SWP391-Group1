package org.example.talentcenter.utilities;

public enum Type {
    COMBO("Combo"),
    LESSON("Theo buổi");

    private final String displayName;

    Type(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
