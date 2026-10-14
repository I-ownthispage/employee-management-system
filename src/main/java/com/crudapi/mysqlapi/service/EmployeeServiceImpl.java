package com.crudapi.mysqlapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import java.util.ArrayList;
//import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crudapi.mysqlapi.dto.EmployeeRequestdto;
import com.crudapi.mysqlapi.dto.EmployeeResponsedto;
import com.crudapi.mysqlapi.entity.Employee;
import com.crudapi.mysqlapi.exception.EmployeeNotFoundException;
import com.crudapi.mysqlapi.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	private final EmployeeRepository employeeRepository;
	
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}
	
	@Override
	public EmployeeResponsedto createEmployee(EmployeeRequestdto request) {
		
		logger.info("Creating a new employee");
		
		logger.debug("Saving employee to database");
		
		Employee employee = new Employee();
		
		employee.setName(request.getName());
		employee.setDepartment(request.getDepartment());
		employee.setEmail(request.getEmail().trim().toLowerCase());
		employee.setSalary(request.getSalary());
		
		Employee savedEmployee = employeeRepository.save(employee);
		
		logger.debug("Employee saved successfully - id={}", savedEmployee.getId());
		
		logger.info("Employee created successfully - id: {}", savedEmployee.getId());
		
		return new EmployeeResponsedto(
				savedEmployee.getId(),
				savedEmployee.getName(),
				savedEmployee.getDepartment(),
				savedEmployee.getEmail(),
				savedEmployee.getSalary()
		);
	}
	
	@Override
	public Page<EmployeeResponsedto> getAllEmployees(Pageable pageable){
		
		logger.info("Fetching all employees - page: {}, size: {}", 
						pageable.getPageNumber(), 
						pageable.getPageSize());
		
		logger.debug("Employee pagination request - sort={}",
				pageable.getSort());
		
		Page<Employee> employees = employeeRepository.findAll(pageable);
		
		logger.debug("Employee fetched completed - returned={}, total={}",
				employees.getNumberOfElements(),
				employees.getTotalElements());
		
		return employees.map(employee ->
					new EmployeeResponsedto(
							employee.getId(),
							employee.getName(),
							employee.getDepartment(),
							employee.getEmail(),
							employee.getSalary()
					)
		);
	}
	
	@Override
	public EmployeeResponsedto getEmployeeById(Long id) {
		
		logger.info("Fetching employee - id: {}", id);
		
		logger.debug("Searching database for employee - id: {}", id);
		
		Employee employee = employeeRepository.findById(id)
											.orElseThrow(() ->
													new EmployeeNotFoundException(
															"Employee not found with id: " + id
														));
		
		logger.debug("Employee found: id={}", employee.getId());
		
		return new EmployeeResponsedto(
				employee.getId(),
				employee.getName(),
				employee.getDepartment(),
				employee.getEmail(),
				employee.getSalary()
		);
	}
	
	@Override
	public Page<EmployeeResponsedto> searchEmployees(String name, String department, Pageable pageable){
		
		logger.info("Searching employees - name: {}, page: {}, size: {}",
						name,
						pageable.getPageNumber(),
						pageable.getPageSize());
		
		logger.debug("Search filters applied - nameProvided={}, departmentProvided={}, sort={}",
	            name != null && !name.isBlank(),
	            department != null && !department.isBlank(),
	            pageable.getSort());
		
		Page<Employee> employees = employeeRepository.searchEmployees(name, department, pageable);
		
		logger.debug("Employee search completed - returned={}, total={}",
	            employees.getNumberOfElements(),
	            employees.getTotalElements());
		
		return employees.map(employee -> new EmployeeResponsedto(
												employee.getId(),
												employee.getName(),
												employee.getDepartment(),
												employee.getEmail(),
												employee.getSalary()
										)
		);
	}
	
	@Override
	public Page<EmployeeResponsedto> searchEmployeesByName(String name, Pageable pageable){
		
		logger.info("Searching employees by name: {}", name);
		
		logger.debug("Name search filter received - filterProvided={}",
	            name != null && !name.isBlank());
		
		Page<Employee> employees = employeeRepository.findByNameContainingIgnoreCase(name,  pageable);
		
		logger.debug("Name search completed - returned={}, total={}",
				employees.getNumberOfElements(),
				employees.getTotalElements());
		
		return employees.map(employee -> new EmployeeResponsedto(
												employee.getId(),
												employee.getName(),
												employee.getDepartment(),
												employee.getEmail(),
												employee.getSalary()
										 )
		);
	}
	
	@Override
	public Page<EmployeeResponsedto> searchEmployeesByDepartment(String department, Pageable pageable){
		
		logger.info("Searching employee by department: {}", department);
		
		logger.debug("Department search filter received - filterProvided={}",
	            department != null && !department.isBlank());
		
		Page<Employee> employees = employeeRepository.findByDepartmentContainingIgnoreCase(department, pageable);
		
		logger.debug("Department search completed - returned={}, total={}",
	            employees.getNumberOfElements(),
	            employees.getTotalElements());
		
		return employees.map(employee -> new EmployeeResponsedto(
												employee.getId(),
												employee.getName(),
												employee.getDepartment(),
												employee.getEmail(),
												employee.getSalary()
										 )
		);
	}
	
	@Override
	public EmployeeResponsedto updateEmployee(Long id, EmployeeRequestdto request) {
		
		logger.info("Updating employee - id: {}", id);
		
		logger.debug("Searching database for employee to update - id: {}", id);
		
		Employee existingEmployee = employeeRepository.findById(id).orElseThrow(() -> 
																			new EmployeeNotFoundException(
																					"Employee not found with id " + id
																					));
		
		
		
		logger.debug("Employee found for update - id={}",
	            existingEmployee.getId());
		
		existingEmployee.setName(request.getName());
		existingEmployee.setDepartment(request.getDepartment());
		existingEmployee.setEmail(
	            request.getEmail().trim().toLowerCase()
	    );
		existingEmployee.setSalary(request.getSalary());
		
		logger.debug("Employee fields updated in memory - id={}", id);
		
		Employee updatedEmployee = employeeRepository.save(existingEmployee);
		
		logger.info("Employee updated successfully - id: {}", id);
		
		return new EmployeeResponsedto(
				updatedEmployee.getId(),
				updatedEmployee.getName(),
				updatedEmployee.getDepartment(),
				updatedEmployee.getEmail(),
				updatedEmployee.getSalary()
		);
	}
	
	@Override
	public void deleteEmployee(Long id) {
		
		logger.info("Deleting employee - id: {}", id);
		
		logger.debug("Checking employee existence before deletion - id={}", id);
		
		if(!employeeRepository.existsById(id)) {
			
			logger.warn("Cannot delete employee - employee not found - id: {}", id);
			
			throw new EmployeeNotFoundException(
					"Employee not found with id: " + id
					);
		}
		
		logger.debug("Employee exists. Proceeding with deletion - id: {}", id);
		
		employeeRepository.deleteById(id);
		
		logger.info("Employee deleted successfully - id: {}", id);
	}
}
