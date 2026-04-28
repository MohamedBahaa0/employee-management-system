package com.logincrud.repository;

import com.logincrud.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee,Long>, JpaSpecificationExecutor<Employee> {
    Boolean existsByEmail(String email);
    @Query("SELECT e.id FROM Employee e")
    Page<Long> findAllIds(Pageable pageable);

    @EntityGraph(attributePaths = "tasks")
    Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.tasks WHERE e.id IN :ids")
    List<Employee> findAllWithTasksByIds(@Param("ids") List<Long> ids);
}
