package org.example.learningcenter.entity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.learningcenter.entity.base.BaseEntity;
import org.example.learningcenter.entity.enums.GroupLevel;
import org.example.learningcenter.entity.enums.GroupStatus;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Group extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private TimeTable timeTable;

    @Enumerated(EnumType.STRING)
    private GroupStatus status;

    @Enumerated(EnumType.STRING)
    private GroupLevel level;

    @ManyToOne(fetch = FetchType.LAZY)
    private Branch branch;

    @Column(name = "current_month", nullable = false)
    private Integer currentMonth = 1;

    public void registerCompletedLesson(Integer lessonsInCurrLevel) {
        if (lessonsInCurrLevel == 0 || lessonsInCurrLevel % 12 != 0) {
            return;
        }
        if (this.currentMonth >= this.level.getDurationInMonths()) {
            GroupLevel nextLevel = this.level.getNextLevel();
            if (nextLevel == null) {
                this.status = GroupStatus.COMPLETED;
            } else {
                this.level = nextLevel;
                this.currentMonth = 1;
            }
        } else {
            this.currentMonth++;
        }
    }
}