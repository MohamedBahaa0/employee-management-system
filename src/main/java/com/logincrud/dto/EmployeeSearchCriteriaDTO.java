package com.logincrud.dto;

public record EmployeeSearchCriteriaDTO(
        String name,
        String email,
        Integer minAge,
        Integer maxAge
) {
}
