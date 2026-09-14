package org.example.crm.specs;

import jakarta.persistence.criteria.*;
import org.example.crm.entity.enums.GroupStatus;
import org.example.crm.entity.model.*;
import org.springframework.data.jpa.domain.Specification;

public class GroupSpec {

    public static Specification<Group> isNotDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("deleted"));
    }

    public static Specification<Group> belongsToOrg(String orgId) {
        return (root, query, cb) -> {
            if (orgId == null || orgId.isBlank()) return cb.conjunction();
            return cb.equal(root.get("organizationId"), orgId);
        };
    }

    public static Specification<Group> hasStatus(GroupStatus status) {
        return (root, query, cb) -> {
            if (status == null) return cb.conjunction();
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Group> hasLevel(String levelName) {
        return (root, query, cb) -> {
            if (levelName == null || levelName.isBlank()) return cb.conjunction();
            Join<Group, Level> levelJoin = root.join("level", JoinType.INNER);
            return cb.equal(levelJoin.get("name"), levelName);
        };
    }

    public static Specification<Group> searchKeyword(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return cb.conjunction();

            String pattern = "%" + search.trim().toLowerCase() + "%";

            // Left join teacher -> user for searching teacher name
            Join<Group, Teacher> teacherJoin = root.join("teacher", JoinType.LEFT);
            Join<Teacher, User> userJoin = teacherJoin.join("user", JoinType.LEFT);

            Predicate nameLike = cb.like(cb.lower(root.get("name")), pattern);
            Predicate roomLike = cb.like(cb.lower(root.get("room")), pattern);
            Predicate teacherLike = cb.and(
                    cb.isNotNull(userJoin.get("id")),
                    cb.like(cb.lower(userJoin.get("fullName")), pattern)
            );

            return cb.or(nameLike, roomLike, teacherLike);
        };
    }
}