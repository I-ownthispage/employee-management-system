package com.crudapi.mysqlapi.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.crudapi.mysqlapi.dto.EmployeeRequestdto;
import com.crudapi.mysqlapi.dto.EmployeeResponsedto;

public interface EmployeeService {
	
	EmployeeResponsedto createEmployee(EmployeeRequestdto request);
	
	Page<EmployeeResponsedto> getAllEmployees(Pageable pageable);
	
	Page<EmployeeResponsedto> searchEmployeesByName(String name, Pageable pageable);
	
	Page<EmployeeResponsedto> searchEmployeesByDepartment(String department, Pageable pageable);
	
	Page<EmployeeResponsedto> searchEmployees(String name, String department, Pageable pageable);
	
	EmployeeResponsedto getEmployeeById(Long id);
	
	EmployeeResponsedto updateEmployee(Long id, EmployeeRequestdto request);
	
	void deleteEmployee(Long id);
}
