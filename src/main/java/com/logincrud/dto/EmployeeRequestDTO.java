package com.logincrud.dto;

import com.logincrud.model.Task;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;


public record EmployeeRequestDTO (
        @NotBlank String name ,
        @Email String email,
        @Min(18) int age ,
         List<Task> tasks)

{}