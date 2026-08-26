package org.example.crm.repository;

import org.example.crm.entity.model.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson,String> {
    @Query(value = "select l from Lesson l where l.deleted = false and (:search is null or l.teacher.user.fullName ilike concat('%',cast(:search as string),'%'))")
    Page<Lesson> findAll(Pageable pageable, @Param("search") String search);

    Long countLessonsByDeleted(Boolean deleted);

    @Query("""
    SELECT COUNT(l.id)
     from Lesson l
     join Group g on g.id = :groupId
     join g.level gl on gl.name = :name
      where l.group.id =:groupId
""")
    Optional<Integer> findLessonCountByGroupId(@Param("groupId") String id, @Param("name") String name);


    @Query("SELECT COUNT(l.id) from Lesson l where l.organizationId =:orgId and l.deleted = false")
    Long countLessonsByOrgIdAndDeleted(@Param("orgId") String organizationId);

}
