package org.example.crm.repository;

import org.example.crm.entity.enums.GroupStatus;
import org.example.crm.entity.model.Enrollment;
import org.example.crm.projection.GroupNameProjection;
import org.example.crm.projection.GroupProjection;
import org.example.crm.entity.model.Group;
import org.example.crm.projection.GroupStatsProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {
    boolean existsGroupByName(String name);

    @Query(
            value = """
                        SELECT
                            g.id AS id,
                            g.name AS name,
                            g.room AS room,
                            g.startDate as startDate,
                            g.teacher AS teacher,
                            g.timeTable AS timeTable,
                            g.status AS status,
                            lev.name AS levelName,
                            g.currentMonth AS currentMonth,
                            COUNT(distinct l.id) AS lessonsCount,
                            COUNT(distinct e.id) as studentCount
                        FROM Group g
                        JOIN g.level lev
                        LEFT JOIN Lesson l ON l.group = g
                             AND (:level IS NULL OR l.level.name = :level)
                        LEFT JOIN g.teacher t
                        LEFT JOIN t.user tu
                        LEFT JOIN g.timeTable tt
                        LEFT JOIN Enrollment e on e.group.id=g.id and e.deleted=false
                        WHERE g.organizationId = :organizationId
                          AND (:status IS NULL OR g.status = :status)
                          AND (:level IS NULL OR lev.name = :level)
                          AND (
                               CAST(:search AS string) IS NULL
                               OR LOWER(g.name) LIKE LOWER(CAST(:search AS string))
                               OR LOWER(g.room) LIKE LOWER(CAST(:search AS string))
                               OR (tu.id IS NOT NULL AND LOWER(tu.fullName) LIKE LOWER(CAST(:search AS string)))
                          )
                        GROUP BY g.id, g.name, g.room, g.status, lev.id, g.currentMonth, t.id, tt.id
                    """
    )
    Page<GroupProjection> getAllByFilter(
            @Param("organizationId") String organizationId,
            @Param("status") GroupStatus status,
            @Param("level") String level,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("""
                    update Group g
                    set g.deleted = true
                    where g.id = :id
            """)
    @Modifying
    void updateDeleted(String id);

    @Query("""
                        SELECT COUNT(g.id)
                        FROM Group g
                        JOIN g.branch b
                        JOIN Organization o ON o.id = :organizationId
                        WHERE g.deleted = false
            """)
    Optional<Integer> getCount(String organizationId);

    @Query("""
                SELECT
                    g.id AS id,
                    g.name AS name,
                    tt.dayType AS dayType
                FROM Group g
                LEFT JOIN g.timeTable tt
                WHERE g.teacher.user.id = :userId
                  AND g.deleted = false
            """)
    List<GroupNameProjection> findAllGroupNames(@Param("userId") String teacherId);

    @Query("SELECT g FROM Group g WHERE g.teacher.user.id = :userId AND g.status = 'ONGOING' and g.deleted = false")
    List<Group> findAllByTeacherUserId(@Param("userId") String userId);


    @Query("""
                        SELECT g FROM Group g
                        join g.branch b
                        join Organization o on o.id = :organizationId
                        WHERE g.id = :id
                        AND g.deleted = false
            """)
    Optional<Group> findByIdAndOrganizationId(String id, String organizationId);

    @Query("select g.branch.id from Group g where g.id=:id and g.deleted=false")
    Optional<String> checkAndGetBranchId(@Param("id") String groupId);

    @Query("""
                SELECT e.group
                FROM Enrollment e
                JOIN e.student s
                JOIN s.user u
                WHERE u.id = :userId
                  AND u.deleted = false
                  AND e.deleted = false
                  AND e.group.deleted = false
                  AND e.group.status = 'ACTIVE'
            """)
    List<Group> getMyGroups(@Param("userId") String userId);

    @Query("select g.currentMonth from Group g where g.id=:id and g.deleted=false")
    Optional<Integer> checkAndGetCurrentMonth(String groupId);


    @Query("""
                SELECT e
                FROM Enrollment e
                JOIN fetch e.student s
                WHERE e.group.id = :groupId
                  AND e.deleted = false
                  AND s.user.deleted = false
""")
    List<Enrollment> findAllEnrollmentsByGroupId(String groupDd);

    @Query("""
                SELECT g
                FROM Group g
                JOIN g.teacher t
                JOIN t.user u
                join UserOrganization uo on uo.user.id = u.id and uo.organization.id = :organizationId
                WHERE u.id = :id
                  AND g.deleted = false
                  and uo.deleted = false
            """)
    List<Group> findAllGroupsByTeacherId(String id, String organizationId);


    @Query(value = """
    SELECT
        COUNT(DISTINCT s.id) AS totalStudents,
        COUNT(DISTINCT CASE WHEN en.deleted = false THEN s.id END) AS activeStudents,
        COUNT(DISTINCT CASE WHEN en.created_at BETWEEN :monthAgo AND :now THEN en.id END)AS newStudents,
        COUNT(DISTINCT CASE WHEN en.created_at BETWEEN :monthAgo AND :now
                                 AND en.leaving_reason IS NOT NULL AND en.deleted = true THEN s.id END) AS lostStudents,
(
                    SELECT COUNT(DISTINCT failing.student_id)
                    FROM (
                        SELECT island.student_id
                        FROM (
                            SELECT
                                ats.student_id,
                                ats.status,
                                ROW_NUMBER() OVER (PARTITION BY ats.student_id, l.group_id ORDER BY l.created_at)
                                    - ROW_NUMBER() OVER (PARTITION BY ats.student_id, l.group_id,
                                                         CASE WHEN ats.status = 'ABSENT' THEN 1 ELSE 0 END
                                                         ORDER BY l.created_at) AS grp
                            FROM attendance_students ats
                            JOIN attendances a ON a.id = ats.attendance_id AND a.deleted = false
                            JOIN lessons l ON l.id = a.lesson_id AND l.deleted = false
                            JOIN enrollments en ON en.student_id = ats.student_id
                                               AND en.group_id = l.group_id AND en.deleted = false
                            WHERE l.group_id IN (:groupIdList)
                              AND l.created_at BETWEEN :monthAgo AND :now
                        ) AS island
                        WHERE island.status = 'ABSENT'
                        GROUP BY island.student_id, island.grp
                        HAVING COUNT(*) >= 3
                    ) AS failing
                ) AS potentialFailStudents,
        CAST(0 AS BIGINT) AS redList,
        CAST(0 AS BIGINT) AS blackList
    FROM students s
    JOIN enrollments en ON en.student_id = s.id
                        AND en.group_id IN (:groupIdList)
                        AND s.deleted = false
    """, nativeQuery = true)
    GroupStatsProjection getGroupStats(List<String> groupIdList, LocalDateTime monthAgo, LocalDateTime now);
}
