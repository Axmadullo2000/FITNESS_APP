package com.gym.entity.types;

public enum TariffType {
    EVERY_DAY_WITH_TRAINER("Каждый день + тренер"),
    EVERY_DAY_NO_TRAINER("Каждый день, без тренера"),
    EVEY_OTHER_DAY_WITH_TRAINER("3 раза в неделю + тренер"),
    EVEY_OTHER_DAY_NO_TRAINER("3 раза в неделю, без тренера"),;

    private final String displayName;

    TariffType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
