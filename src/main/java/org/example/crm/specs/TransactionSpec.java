package org.example.crm.specs;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.example.crm.entity.enums.TransactionType;
import org.example.crm.entity.model.Invoice;
import org.example.crm.entity.model.Student;
import org.example.crm.entity.model.Transaction;
import org.example.crm.entity.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionSpec {

    public static Specification<Transaction> isNotDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("deleted"));
    }

    public static Specification<Transaction> belongsToOrg(String orgId) {
        return (root, query, cb) -> cb.equal(root.get("organizationId"), orgId);
    }

    public static Specification<Transaction> hasType(TransactionType type) {
        return (root, query, cb) -> type == null ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<Transaction> searchKeyword(String search) {
        return (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return null;
            }

            String pattern = "%" + search.trim().toLowerCase() + "%";

            // Joins
            Join<Transaction, Invoice> invoiceJoin = root.join("invoice", JoinType.LEFT);
            Join<Transaction, Student> studentJoin = root.join("student", JoinType.INNER);
            Join<Student, User> userJoin = studentJoin.join("user", JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.like(cb.lower(invoiceJoin.get("invoiceNumber")), pattern));
            predicates.add(cb.like(cb.lower(userJoin.get("fullName")), pattern));
            predicates.add(cb.like(cb.lower(userJoin.get("phone")), pattern));
            predicates.add(cb.like(cb.lower(root.get("amount").as(String.class)), pattern));

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}