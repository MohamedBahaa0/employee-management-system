package com.logincrud.dto;


import com.logincrud.model.Task;

import java.util.List;

public record EmployeeResponseDTO(
     long id,
     String name,
     String email,
     int age,
     List<Task> tasks
)
{}
