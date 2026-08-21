package org.example.crm.repository;

import org.example.crm.entity.enums.GroupLevel;
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

    @Query("""
                SELECT
                    g.id AS id,
                    g.name AS name,
                    g.room AS room,
                    g.teacher AS teacher,
                    g.timeTable AS timeTable,
                    g.status AS status,
                    g.level AS level,
                    g.currentMonth AS currentMonth,
                    COUNT(l.id) AS lessonsCount
                FROM Group g
                LEFT JOIN Lesson l ON l.group = g AND (:level IS NULL OR l.level = :level)
                JOIN g.branch b
                WHERE b.organizationId = :organizationId
                  AND (:status IS NULL OR g.status = :status)
                  AND (:level IS NULL OR g.level = :level)
                  AND (
                       :search IS NULL 
                       OR LOWER(g.name) LIKE :search
                       OR LOWER(g.room) LIKE :search
                       OR (g.teacher IS NOT NULL AND LOWER(g.teacher.user.fullName) LIKE :search)
                  )
                GROUP BY g.id, g.name, g.room, g.teacher, g.timeTable, g.status, g.level, g.currentMonth
            """)
    Page<GroupProjection> getAllByFilter(
            @Param("organizationId") String organizationId,
            @Param("status") GroupStatus status,
            @Param("level") GroupLevel level,
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
            SELECT g.id as id, g.name as name, g.timeTable.dayType as dayType
            from Group g
            left join g.teacher t
            left join t.user u
            where u.id = :userId and g.deleted = false
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
}
