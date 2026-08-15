package org.example.crm.entity.enums;

import lombok.Getter;

@Getter
public enum GroupLevel {
    A1(2),
    A2(3),
    B1(4),
    B2(4),
    C1(5),
    C2(6);

    private final int durationInMonths;

    GroupLevel(int durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    public GroupLevel getNextLevel() {
        GroupLevel[] levels = GroupLevel.values();
        int nextIndex = this.ordinal() + 1;

        if (nextIndex < levels.length) {
            return levels[nextIndex];
        }
        return null;
    }
}