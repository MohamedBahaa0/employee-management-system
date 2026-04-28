package com.logincrud.controller;

import com.logincrud.dto.EmployeeRequestDTO;
import com.logincrud.dto.EmployeeResponseDTO;
import com.logincrud.dto.EmployeeSearchCriteriaDTO;
import com.logincrud.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> getAll(
            @RequestParam(defaultValue = "5") int size ,
            @RequestParam(defaultValue = "0") int page
    ){
        return ResponseEntity.ok(employeeService.getAll(PageRequest.of(page,size)));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeResponseDTO>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "0") int page
    ){
        EmployeeSearchCriteriaDTO criteria = new EmployeeSearchCriteriaDTO(name, email, minAge, maxAge);
        return ResponseEntity.ok(employeeService.search(criteria, PageRequest.of(page, size)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> update(@PathVariable Long id,
                                                      @Valid @RequestBody EmployeeRequestDTO dto) {
        return ResponseEntity.ok(employeeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.ok("Successfully deleted");
    }

    @PostMapping("/import")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) throws Exception {
        employeeService.importFromCsv(file);
        return ResponseEntity.ok("Imported successfully");
    }
    @PostMapping("/register")
    public ResponseEntity<EmployeeResponseDTO> register(@Valid @RequestBody EmployeeRequestDTO dto) {
        return ResponseEntity.ok(employeeService.register(dto));
    }

    @PostMapping
    @PreAuthorize("hasRole('role_admin')")
    public ResponseEntity<EmployeeResponseDTO> add(@Valid @RequestBody EmployeeRequestDTO dto) {
        return ResponseEntity.ok(employeeService.create(dto));
    }

}
