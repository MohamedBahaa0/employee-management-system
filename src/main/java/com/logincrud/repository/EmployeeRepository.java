package com.logincrud.repository;

import com.logincrud.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    Boolean existsByEmail(String email);
    @Override
    @EntityGraph(attributePaths = "tasks")
    Page<Employee> findAll(Pageable pageable);
    List<Employee> id(Long id);
}
