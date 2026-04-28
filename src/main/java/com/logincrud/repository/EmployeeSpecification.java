package com.logincrud.repository;

import com.logincrud.model.Employee;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class EmployeeSpecification {

    private EmployeeSpecification() {
    }

    public static Specification<Employee> byCriteria(String name, String email, Integer minAge, Integer maxAge) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                ));
            }

            if (email != null && !email.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        "%" + email.toLowerCase() + "%"
                ));
            }

            if (minAge != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("age"), minAge));
            }

            if (maxAge != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("age"), maxAge));
            }

            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
