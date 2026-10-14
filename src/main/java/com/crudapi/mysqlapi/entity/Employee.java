package com.crudapi.mysqlapi.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "employees")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Name cannot be empty")
	private String name;
	
	@NotBlank(message = "Department cannot be empty")
	private String department;
	
	@NotBlank(message = "Email cannot be empty")
	private String email;
	
	@Min(value = 0, message = "Salary cannot be negative")
	private double salary;
	
	
	public Employee() {
		
	}
	
	public Employee(Long id, String name, String department, String email, double salary) {
		this.id = id;
		this.name = name;
		this.department = department;
		this.email = email;
		this.salary = salary;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
