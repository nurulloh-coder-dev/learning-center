package org.example.crm.projection;

public interface GroupStatsProjection {
    Long getTotalStudents();
    Long getActiveStudents();
    Long getNewStudents();
    Long getLostStudents();
    Long getPotentialFailStudents(); // students did not attend more than 3 classes in a row
    Long getRedList(); // homework not done more than 2 times
    Long getBlackList();
}
