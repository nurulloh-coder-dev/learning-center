package org.example.crm.entity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.crm.entity.base.BaseEntity;
import org.example.crm.entity.enums.GroupStatus;
import org.example.crm.repository.GroupLevelRepository;
import org.example.crm.repository.GroupRepository;

import java.time.LocalDate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private Level level;

    @ManyToOne(fetch = FetchType.LAZY)
    private Branch branch;

    @Column(name = "current_month", nullable = false)
    private Integer currentMonth = 1;


    public void registerCompletedLesson(Integer lessonsInCurrLevel,
                                        GroupLevelRepository groupLevelRepository) {
        if (lessonsInCurrLevel == 0 || lessonsInCurrLevel % this.getLevel().getDurationInMonths() != 0) {
            return;
        }
        if (this.currentMonth >= this.getLevel().getLessonCount()/this.getLevel().getDurationInMonths()) {
            Level nextLevel = groupLevelRepository.getNextLevelForGroup(
                    this.getLevel().getOrderNumber(),this.getOrganizationId()).orElse(null);
            if (nextLevel == null) {
                this.setStatus(GroupStatus.COMPLETED);
            } else {
                this.setLevel(nextLevel);
                this.setCurrentMonth(1);

            }

        } else {
            this.currentMonth++;
        }
    }
}