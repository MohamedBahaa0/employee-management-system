package com.logincrud.dto;

import com.logincrud.model.Task;
import jakarta.validation.constraints.*;

import java.util.List;


public record EmployeeRequestDTO (
        @NotBlank String name ,
        @Email String email,
        @Min(18) int age ,
         List<Task> tasks,
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one number")
         String password
        )

{}