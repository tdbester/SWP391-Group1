package org.example.talentcenter.utilities;

public enum CategoryType {
    COURSE(1),
    BLOG(2);

    private final int value;

    CategoryType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CategoryType fromValue(int value) {
        for (CategoryType type : CategoryType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid CategoryType value: " + value);
    }
}