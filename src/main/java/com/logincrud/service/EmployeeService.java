package com.logincrud.service;

import com.logincrud.dto.EmployeeRequestDTO;
import com.logincrud.dto.EmployeeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


public interface EmployeeService {
    EmployeeResponseDTO create(EmployeeRequestDTO dto);
    EmployeeResponseDTO getById(Long id);
    Page<EmployeeResponseDTO> getAll(Pageable pageable);
    EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto);
    void delete(Long id);
    void importFromCsv(MultipartFile file) throws Exception;


}