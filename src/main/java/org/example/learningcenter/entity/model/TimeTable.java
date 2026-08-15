package org.example.learningcenter.entity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.learningcenter.entity.base.BaseEntity;
import org.example.learningcenter.entity.enums.DayType;

import java.time.LocalTime;

@Entity
@Table(name = "timetables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeTable extends BaseEntity {

    @Column(name = "day_type")
    @Enumerated(EnumType.STRING)
    private DayType dayType;

    private LocalTime startTime;
    private LocalTime endTime;
}