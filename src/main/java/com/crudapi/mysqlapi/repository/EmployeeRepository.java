package com.crudapi.mysqlapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crudapi.mysqlapi.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Page<Employee> findByNameContainingIgnoreCase(String name, Pageable pageable);
	
	Page<Employee> findByDepartmentContainingIgnoreCase(String department, Pageable pageable);
	
	@Query("SELECT e FROM Employee e WHERE " + 
			"(:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
			"(:department IS NULL OR LOWER(e.department) LIKE LOWER(CONCAT('%', :department, '%')))")
	Page<Employee> searchEmployees(@Param("name") String name, @Param("department") String department
																	, Pageable pageable);
}
