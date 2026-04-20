package com.logincrud.service;
import com.logincrud.dto.EmployeeRequestDTO;
import com.logincrud.dto.EmployeeResponseDTO;
import com.logincrud.exception.EmployeeNotFoundException;
import com.logincrud.mapper.EmployeeMapper;
import com.logincrud.model.Employee;
import com.logincrud.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl (EmployeeRepository employeeRepository, EmployeeMapper employeeMapper){
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }
    @Override
    public EmployeeResponseDTO create(EmployeeRequestDTO dto) {
        if (employeeRepository.existsByEmail(dto.email()))
            throw new RuntimeException("Email already exists");
        return employeeMapper.toResponse(employeeRepository.save(employeeMapper.toEntity(dto)));
    }

    @Override
    public EmployeeResponseDTO getById(Long id){
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(()-> new EmployeeNotFoundException(id));
        return employeeMapper.toResponse(emp);
    }

    @Override
    public Page<EmployeeResponseDTO> getAll(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(employeeMapper::toResponse);
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


