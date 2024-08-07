package com.brimstone.university_schedule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brimstone.university_schedule.model.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
	
	Optional<Department> findByName(String departmentName);
}
