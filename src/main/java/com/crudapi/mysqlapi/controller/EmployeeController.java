package com.crudapi.mysqlapi.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.crudapi.mysqlapi.dto.EmployeeRequestdto;
import com.crudapi.mysqlapi.dto.EmployeeResponsedto;
import com.crudapi.mysqlapi.dto.PageResponse;
import com.crudapi.mysqlapi.exception.ApiResponse;
import com.crudapi.mysqlapi.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	private final EmployeeService employeeService;
	
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EmployeeResponsedto createEmployee(@Valid @RequestBody EmployeeRequestdto request) {
		return employeeService.createEmployee(request);
	}
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
	@GetMapping
	public PageResponse<EmployeeResponsedto> getAllEmployees(@RequestParam(required = false) String name, 
															@RequestParam(required = false) String department, Pageable pageable){
		
		Page<EmployeeResponsedto> employees = employeeService.searchEmployees(name, department, pageable);
		
		return new PageResponse<EmployeeResponsedto>(
				employees.getContent(),
				employees.getNumber(),
				employees.getSize(),
				employees.getTotalElements(),
				employees.getTotalPages(),
				employees.isFirst(),
				employees.isLast()
		);
	}
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
	@GetMapping("/{id}")
	public EmployeeResponsedto getEmployeeById(@PathVariable Long id) {
		return employeeService.getEmployeeById(id);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
	@PutMapping("/{id}")
	public EmployeeResponsedto updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequestdto request) {
		return employeeService.updateEmployee(id, request);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
	@DeleteMapping("/{id}")
	public ApiResponse deleteEmployee(@PathVariable Long id) {
		
		employeeService.deleteEmployee(id);
		
		return new ApiResponse("Employee deleted successfully");
	}
}
