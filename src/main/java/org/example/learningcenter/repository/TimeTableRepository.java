package org.example.learningcenter.repository;

import org.example.learningcenter.entity.enums.DayType;
import org.example.learningcenter.entity.model.TimeTable;
import org.example.learningcenter.projection.TimeTableProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, String> {

    @Query("""
        select distinct t.id as id,
            t.dayType as days,
                   t.startTime as startTime,
                   t.endTime as endTime
            from TimeTable t
            where (:days_1 is null or t.dayType = :days_1)
              and (:start is null or t.startTime >= :start)
              and (:end is null or t.endTime <= :end)
    """)
    List<TimeTableProjection> getAllTimeTableByFilter(DayType dayType, LocalTime start, LocalTime end);

    @Modifying
    @Query("""
        update TimeTable t
        set t.deleted = true
        where t.id = :id
    """)
    void updateDeleted(String id);
}
