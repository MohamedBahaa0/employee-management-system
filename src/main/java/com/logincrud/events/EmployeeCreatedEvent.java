package com.logincrud.events;
import com.logincrud.model.Employee;

public record EmployeeCreatedEvent(Employee employee) {}
