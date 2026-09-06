package org.example.crm.repository;

import jakarta.transaction.Transactional;
import org.example.crm.entity.model.Student;
import org.example.crm.projection.AnalyticStudentProjection;
import org.example.crm.projection.StudentProjection;
import org.example.crm.projection.StudentShowProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
    @Query("""
                SELECT s FROM Student s
                JOIN s.user u
                WHERE u.deleted = false
                  AND (:search IS NULL OR :search = '' OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')))
            """)
    Page<StudentProjection> searchStudents(@Param("search") String search, Pageable pageable);


    @Query("SELECT s FROM Student s WHERE s.id IN " +
            "(SELECT e.student.id FROM Enrollment e WHERE e.group.id = :groupId)")
    List<StudentShowProjection> getStudentShowByGroupId(String groupId);

    @Query("select s from Student s join s.user u where u.phone like :phone and u.deleted = false")
    List<Student> getStudentByPhone(@Param("phone") String phone);

    @Query("select exists (select s.id from Student s where s.id =:id)")
    Optional<Boolean> checkId(@Param("id") String id);


    @Query("""
                select s from Student s
                join s.user u
                where u.organizationId= :orgId and u.deleted = false
                and u.deleted = false
                and (:search IS NULL OR :search = '' OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')))
            """)
    Page<StudentProjection> searchStudentsByOrganization(@Param("search") String search, @Param("orgId") String organizationId, Pageable pageable);

    @Query("""
                select
                    count(e.id) as studentCount,
                    count(
                        case
                            when u.createdAt >= :month and u.createdAt <= :nextMonth then 1
                        end
                    ) as studentsAddedInMonth,
                    count(
                        case
                            when u.createdAt >= :prev and u.createdAt <= :month then 1
                        end
                    ) as studentsAddedInPrevMonth
                from Student e
                join e.user u
                where u.organizationId = :organizationId
                  and u.deleted = false
            """)
    AnalyticStudentProjection getAnalyticStudent(String organizationId,
                                                 LocalDateTime prev,
                                                 LocalDateTime month,
                                                 LocalDateTime nextMonth);

    @Query("select count(s.id) from Student s where s.user.organizationId=:orgId and s.user.deleted = false")
    Long countStudentsByOrganizationId(@Param("orgId") String organizationId);

    @Modifying
    @Transactional
    @Query("update Student s set s.user.deleted = true where s.id=:id")
    void softDelete(String id);


    @Query("""
        select s
        from Student s
        join fetch User u on u.id = :id and s.user.id = u.id
        
""")
    Optional<Student> findByUserId(String id);
}
