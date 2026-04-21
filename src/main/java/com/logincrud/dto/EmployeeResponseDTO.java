package com.logincrud.dto;



import java.util.List;

public record EmployeeResponseDTO(
     long id,
     String name,
     String email,
     int age,
     List<TaskResponseDTO> tasks
)
{}
