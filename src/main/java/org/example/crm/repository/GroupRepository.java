package org.example.crm.repository;

import org.example.crm.entity.enums.GroupStatus;
import org.example.crm.projection.GroupNameProjection;
import org.example.crm.projection.GroupProjection;
import org.example.crm.entity.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
                            g.teacher AS teacher,
                            g.timeTable AS timeTable,
                            g.status AS status,
                            lev.id AS levelId,
                            g.currentMonth AS currentMonth,
                            COUNT(l.id) AS lessonsCount
                        FROM Group g
                        JOIN g.level lev
                        LEFT JOIN Lesson l ON l.group = g
                             AND (:level IS NULL OR l.level.name = :level)
                        LEFT JOIN g.teacher t
                        LEFT JOIN t.user tu
                        LEFT JOIN g.timeTable tt
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
//            countQuery = """
//                        SELECT COUNT(DISTINCT g.id)
//                        FROM Group g
//                        JOIN g.level lev
//                        JOIN g.branch b
//                        LEFT JOIN g.teacher t
//                        LEFT JOIN t.user tu
//                        WHERE g.organizationId = :organizationId
//                          AND (:status IS NULL OR g.status = :status)
//                          AND (:level IS NULL OR lev.name = :level)
//                          AND (
//                               CAST(:search AS string) IS NULL
//                               OR LOWER(g.name) LIKE LOWER(CAST(:search AS string))
//                               OR LOWER(g.room) LIKE LOWER(CAST(:search AS string))
//                               OR (tu.id IS NOT NULL AND LOWER(tu.fullName) LIKE LOWER(CAST(:search AS string)))
//                          )
//                    """
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
                WHERE g.teacher.id = :teacherId
                  AND g.deleted = false
            """)
    List<GroupNameProjection> findAllGroupNames(@Param("teacherId") String teacherId);

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
}
