package com.logincrud.mapper;

import com.logincrud.dto.EmployeeRequestDTO;
import com.logincrud.dto.EmployeeResponseDTO;
import com.logincrud.model.Employee;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeResponseDTO toResponse(Employee emp);

    Employee toEntity(EmployeeRequestDTO dto);
}