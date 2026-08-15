package org.example.crm.entity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.crm.entity.base.BaseEntity;
import org.example.crm.entity.enums.AttendanceStatus;

@Entity
@Table(
        name = "attendance_students",
        uniqueConstraints = @UniqueConstraint(columnNames = {"attendance_id", "student_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceStudent extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;
}