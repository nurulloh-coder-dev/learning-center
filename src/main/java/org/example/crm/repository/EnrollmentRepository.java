package org.example.crm.repository;

import jakarta.transaction.Transactional;
import org.example.crm.entity.model.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment,String> {
    @Query("select exists (select e.id from Enrollment e where e.id=:id)")
    Optional<Boolean> checkId(@Param("id") String id);

    @Transactional
    @Modifying
    @Query("update Enrollment e set e.leavingReason =:reason, e.deleted = true where e.id=:id")
    void softDelete(@Param("reason") String reason, @Param("id") String id);

    @Query("select e from Enrollment e where (:groupId is null or e.group.id =:groupId) and e.student.user.fullName ilike concat('%',:fullName,'%')")
    Page<Enrollment> findAllBySearch(@Param("fullName") String search,@Param("groupId")String groupId, Pageable pageable);

    @Query("select e from Enrollment e where e.student.user.fullName ilike concat('%',:fullName,'%')")
    Page<Enrollment> findAllBySearch(@Param("fullName") String search, Pageable pageable);

}
