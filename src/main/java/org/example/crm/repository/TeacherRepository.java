package org.example.crm.repository;

import jakarta.transaction.Transactional;
import org.example.crm.entity.model.Teacher;
import org.example.crm.projection.AnalyticTeacherProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, String> {
    @Query(value = "select t.*,u.phone,u.full_name from teachers t join users u on u.id=t.user_id and u.deleted = false and u.full_name ilike concat('%',:search,'%')",
            countQuery = "select count(t.id) from teachers t join users u on u.id=t.user_id and u.deleted = false and u.full_name ilike concat('%',:search,'%')",
            nativeQuery = true)
    Page<Teacher> findAll(Pageable pageable, String search);

    @Query("""
        SELECT COUNT(t.id)
        from Teacher t
        join t.user u
        where u.organizationId =:organizationId and u.deleted = false""")
    Long countTeachersByDeletedAndOrg(@Param("organizationId") String organizationId);

    @Query("""
                    select t from Teacher t
                    join t.user u
                    where t.id =:id and u.organizationId=:organizationId
            """)
    Optional<Teacher> findTeacherByIdAndOrg(String id, String organizationId);

    @Query("SELECT t from Teacher t join t.user u where (:search is null or u.fullName ilike :search) and u.organizationId= :orgId")
    Page<Teacher> findAllBySearch(@Param("orgId") String organizationId, @Param("search") String search, Pageable pageable);

    @Query("""
    select
        count(e.id) as teacherCount,
        count(
            case
                when u.createdAt >= :monthAgo then 1
            end
        ) as teachersAddedInMonth
    from Teacher e
    join e.user u
    where u.organizationId = :organizationId
      and u.deleted = false
""")
    AnalyticTeacherProjection getAnalyticTeacher(String organizationId, LocalDateTime monthAgo);

    @Query("select exists (select t.id from Teacher t where t.id =:id and t.user.deleted = false)")
    Optional<Boolean> checkId(String id);

    @Modifying
    @Transactional
    @Query("update Teacher t set t.user.deleted = true where t.id=:id")
    void softDelete(String id);

    @Query("select exists(select t.id from Teacher t join t.user u where t.id=:id and u.deleted=false and u.organizationId=:organizationId)")
    Optional<Boolean> checkIdAndOrgId(String id, String organizationId);
}
