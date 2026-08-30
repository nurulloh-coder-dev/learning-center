package org.example.crm.repository;

import org.example.crm.entity.model.Teacher;
import org.example.crm.projection.AnalyticTeacherProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, String> {
    @Query(value = "select t.*,u.phone,u.full_name from teachers t join users u on u.id=t.user_id and u.deleted = false and u.full_name ilike concat('%',:search,'%')",
            countQuery = "select count(t.id) from teachers t join users u on u.id=t.user_id and u.deleted = false and u.full_name ilike concat('%',:search,'%')",
            nativeQuery = true)
    Page<Teacher> findAll(Pageable pageable, String search);

    Long countTeachersByDeleted(Boolean deleted);

    Teacher findTeacherByUser_Id(String userId);


    @Query("""
        SELECT COUNT(t.id)
        from Teacher t
        join t.user u
        where u.organizationId =:organizationId and t.deleted = false""")
    Long countTeachersByDeletedAndOrg(@Param("organizationId") String organizationId);

    @Query("""
        select t from Teacher t
        join User u on t.user.id = u.id and u.organizationId =:organizationId
        where t.id =:id
""")
    Optional<Teacher> findTeacherByIdAndOrg(String id, String organizationId);

    @Query("SELECT t from Teacher t where (:search is null or t.user.fullName ilike :search) and t.organizationId= :orgId")
    Page<Teacher> findAllBySearch(@Param("orgId") String organizationId, @Param("search") String search, Pageable pageable);

    @Query("""
    select
        count(e.id) as teacherCount,
        count(
            case
                when e.createdAt >= :month and e.createdAt <= :nextMonth then 1
            end
        ) as teachersAddedInMonth,
        count(
            case
                when e.createdAt >= :prev and e.createdAt <= :month then 1
            end
        ) as teachersAddedInPrevMonth
    from Teacher e
    where e.organizationId = :organizationId
      and e.deleted = false
""")
    AnalyticTeacherProjection getAnalyticTeacher(String organizationId,
                                                 LocalDateTime prev,
                                                 LocalDateTime month,
                                                 LocalDateTime nextMonth);
}
