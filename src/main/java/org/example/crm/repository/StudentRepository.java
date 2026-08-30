package org.example.crm.repository;

import org.example.crm.entity.model.Student;
import org.example.crm.projection.AnalyticStudentProjection;
import org.example.crm.projection.StudentProjection;
import org.example.crm.projection.StudentShowProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
    @Query("""
                SELECT s FROM Student s
                JOIN s.user u
                WHERE s.deleted = false
                  AND u.deleted = false
                  AND (:search IS NULL OR :search = '' OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')))
            """)
    Page<StudentProjection> searchStudents(@Param("search") String search, Pageable pageable);

    @Query("""
                    select distinct s
                    from Student s
                    join Enrollment e on s.id = e.student.id
                    join Group g on e.group.id = g.id and g.status = 'ONGOING'
                     where s.deleted = false and
                     s.balance < 0
            """)
    List<Student> findAllStudentsForInvoice();

    @Query("SELECT s FROM Student s WHERE s.id IN " +
            "(SELECT e.student.id FROM Enrollment e WHERE e.group.id = :groupId)")
    List<StudentShowProjection> getStudentShowByGroupId(String groupId);

    @Query("select s from Student s where s.user.phone like :phone and s.deleted = false")
    List<Student> getStudentByPhone(@Param("phone") String phone);

    @Query("select exists (select s.id from Student s where s.id =:id)")
    Optional<Boolean> checkId(@Param("id") String id);


    @Query("""
                select s from Student s
                join User u on s.user.id = u.id
                where s.organizationId= :orgId and s.deleted = false
                and u.deleted = false
                and (:search IS NULL OR :search = '' OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')))
            """)
    Page<StudentProjection> searchStudentsByOrganization(@Param("search") String search, @Param("orgId") String organizationId, Pageable pageable);

    @Query("""
                select
                    count(e.id) as studentCount,
                    count(
                        case
                            when e.createdAt >= :month and e.createdAt <= :nextMonth then 1
                        end
                    ) as studentsAddedInMonth,
                    count(
                        case
                            when e.createdAt >= :prev and e.createdAt <= :month then 1
                        end
                    ) as studentsAddedInPrevMonth
                from Student e
                where e.organizationId = :organizationId
                  and e.deleted = false
            """)
    AnalyticStudentProjection getAnalyticStudent(String organizationId,
                                                 LocalDateTime prev,
                                                 LocalDateTime month,
                                                 LocalDateTime nextMonth);

    @Query("select count(s.id) from Student s where s.organizationId=:orgId and s.deleted = false")
    Long countStudentsByOrganizationId(@Param("orgId") String organizationId);
}
