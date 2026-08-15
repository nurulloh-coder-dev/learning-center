package org.example.crm.repository;

import org.example.crm.entity.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
        where u.organization.id =:organizationId and t.deleted = false""")
    Long countTeachersByDeletedAndOrg(@Param("organizationId") String organizationId);

    @Query("""
        select t from Teacher t
        join User u on t.user.id = u.id and u.organization.id =:organizationId
        where t.id =:id
""")
    Optional<Teacher> findTeacherByIdAndOrg(String id, String organizationId);
}
