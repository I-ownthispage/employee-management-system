package com.crudapi.mysqlapi.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EmployeeRequestdto {

	@NotBlank(message = "Name cannot be empty")
	@Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
	private String name;
	
	@NotBlank(message = "Department cannot be empty")
	@Size(min = 2, max = 50, message = "Deaprtment must be between 2 and 50 characters")
	private String department;
	
	@Email(message = "Invalid email format")
	@NotBlank(message = "Email cannot be empty")
	@Pattern(
			regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$",
			message = "Email must end with .com"
	)
	private String email;
	
	@Min(value = 1, message = "Salary must be greater than 0")
	private double salary;
	
	public EmployeeRequestdto(){
		
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDepartment() {
		return department;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public double getSalary() {
		return salary;
	}
	
	public void setSalary(double salary) {
		this.salary = salary;
	}
}
