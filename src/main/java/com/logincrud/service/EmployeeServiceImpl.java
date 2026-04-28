package com.logincrud.service;
import com.logincrud.dto.EmployeeRequestDTO;
import com.logincrud.dto.EmployeeResponseDTO;
import com.logincrud.dto.EmployeeSearchCriteriaDTO;
import com.logincrud.exception.EmployeeNotFoundException;
import com.logincrud.mapper.EmployeeMapper;
import com.logincrud.model.Employee;
import com.logincrud.repository.EmployeeRepository;
import com.logincrud.repository.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final KeycloakService keycloakService;

    @Override
    public EmployeeResponseDTO create(EmployeeRequestDTO dto) {
        if (employeeRepository.existsByEmail(dto.email()))
            throw new RuntimeException("Email already exists");
        return employeeMapper.toResponse(employeeRepository.save(employeeMapper.toEntity(dto)));
    }


    @Override
    @Transactional
    public EmployeeResponseDTO register(EmployeeRequestDTO dto) {
        if (employeeRepository.existsByEmail(dto.email()))
            throw new RuntimeException("Email already exists");
        Employee employeeSaved = employeeRepository.save(employeeMapper.toEntity(dto));
        // Keycloak creation
        try {
            keycloakService.createUser(
                    dto.email(),
                    dto.password()
            );
        } catch (Exception e) {
            throw new RuntimeException("Keycloak sync failed, rolling back: " + e.getMessage());
        }
        return employeeMapper.toResponse(employeeSaved);
    }

    @Override
    public EmployeeResponseDTO getById(Long id){
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(()-> new EmployeeNotFoundException(id));
        return employeeMapper.toResponse(emp);
    }

    @Override
    public Page<EmployeeResponseDTO> getAll(Pageable pageable) {
        Page<Long> idPage = employeeRepository.findAllIds(pageable);

        List<Employee> employees = employeeRepository.findAllWithTasksByIds(idPage.getContent());

        List<EmployeeResponseDTO> dtos = employees.stream()
                .map(employeeMapper::toResponse)
                .toList();

        return new PageImpl<>(dtos, pageable, idPage.getTotalElements());
    }

    @Override
    public Page<EmployeeResponseDTO> search(EmployeeSearchCriteriaDTO criteria, Pageable pageable) {
        Page<Employee> page = employeeRepository.findAll(
                EmployeeSpecification.byCriteria(
                        criteria.name(),
                        criteria.email(),
                        criteria.minAge(),
                        criteria.maxAge()
                ),
                pageable
        );

        return page.map(employeeMapper::toResponse);
    }

    @Override
    public void delete(Long id) {
        if (!employeeRepository.existsById(id))
            throw new EmployeeNotFoundException(id);
        employeeRepository.deleteById(id);
    }

    @Override
    public EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        emp.setName(dto.name());
        emp.setEmail(dto.email());
        emp.setAge(dto.age());
        return employeeMapper.toResponse(employeeRepository.save(emp));
    }

    @Override
    public void importFromCsv(MultipartFile file) throws Exception {
        List<Employee> employees = new ArrayList<>();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream())
        );
        String line;
        boolean firstLine = true;
        while ((line = reader.readLine()) != null) {
            if (firstLine) { firstLine = false; continue; }
            String[] data = line.split(",");
            Employee emp = new Employee();
            emp.setName(data[0].trim());
            emp.setEmail(data[1].trim());
            emp.setAge(Integer.parseInt(data[2].trim()));
            employees.add(emp);
        }
        employeeRepository.saveAll(employees);
    }
}


