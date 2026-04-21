package com.logincrud.controller;

import com.logincrud.dto.EmployeeRequestDTO;
import com.logincrud.dto.EmployeeResponseDTO;
import com.logincrud.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> add(@Valid @RequestBody EmployeeRequestDTO dto) {
        return ResponseEntity.ok(employeeService.create(dto));
    }

}
