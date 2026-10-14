package com.crudapi.mysqlapi.dto;

public class EmployeeResponsedto {

	private Long id;
	private String name;
	private String department;
	private String email;
	private double salary;
	
	public EmployeeResponsedto() {
		
	}
	
	public EmployeeResponsedto(Long id, String name, String department, String email, double salary) {
		
		this.id = id;
		this.name = name;
		this.department = department;
		this.email =email;
		this.salary = salary;
	}
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDepartment() {
		return department;
	}
	
	public String getEmail() {
		return email;
	}
	
	public double getSalary() {
		return salary;
	}
	
}
