package com.logincrud.repository;

import com.logincrud.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    Boolean existsByEmail(String email);

    List<Employee> id(Long id);
}
