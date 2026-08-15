package org.example.crm.repository;

import org.example.crm.entity.enums.GroupLevel;
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

    @Query("SELECT COUNT(l.id) from Lesson l where l.group.id =:groupId and l.level =:level")
    Optional<Integer> findLessonCountByGroupId(@Param("groupId") String id, @Param("level") GroupLevel level);
}
