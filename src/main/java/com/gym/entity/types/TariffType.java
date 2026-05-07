package com.gym.entity.types;

public enum TariffType {
    EVERY_DAY_WITH_TRAINER("Every Day + trainer"),
    EVERY_DAY_NO_TRAINER("Every Day, no trainer"),
    EVEY_OTHER_DAY_WITH_TRAINER("Every other day + trainer"),
    EVEY_OTHER_DAY_NO_TRAINER("Every other day, no trainer"),;

    private final String displayName;

    TariffType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
