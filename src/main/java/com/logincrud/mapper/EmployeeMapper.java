package com.logincrud.mapper;

import com.logincrud.dto.EmployeeRequestDTO;
import com.logincrud.dto.EmployeeResponseDTO;
import com.logincrud.dto.TaskResponseDTO;
import com.logincrud.model.Employee;
import com.logincrud.model.Task;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeResponseDTO toResponse(Employee emp);

    Employee toEntity(EmployeeRequestDTO dto);
    @AfterMapping
    default void linkTasks(@MappingTarget Employee employee) {
        if (employee.getTasks() != null) {
            employee.getTasks()
                    .forEach(task -> task.setEmployee(employee));
        }
    }
    TaskResponseDTO toResponse(Task task);
}