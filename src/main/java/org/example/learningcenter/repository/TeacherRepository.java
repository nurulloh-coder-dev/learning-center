package org.example.learningcenter.repository;

import org.example.learningcenter.entity.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeacherRepository extends JpaRepository<Teacher, String> {
    @Query(value = "select t.*,u.phone,u.full_name from teachers t join users u on u.id=t.user_id and u.deleted = false and u.full_name ilike concat('%',:search,'%')",
            countQuery = "select count(t.id) from teachers t join users u on u.id=t.user_id and u.deleted = false and u.full_name ilike concat('%',:search,'%')",
            nativeQuery = true)
    Page<Teacher> findAll(Pageable pageable, String search);

    Long countTeachersByDeleted(Boolean deleted);

    Teacher findTeacherByUser_Id(String userId);
}
