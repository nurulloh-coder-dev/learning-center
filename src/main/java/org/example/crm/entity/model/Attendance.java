package org.example.crm.entity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.crm.entity.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attendances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attendance extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", unique = true)
    private Lesson lesson;

    @OneToMany(
            mappedBy = "attendance",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AttendanceStudent> attendanceStudents = new ArrayList<>();

    // Helper method to keep both sides of the relationship linked
    public void addStudentAttendance(AttendanceStudent studentAttendance) {
        attendanceStudents.add(studentAttendance);
        studentAttendance.setAttendance(this);
    }
}