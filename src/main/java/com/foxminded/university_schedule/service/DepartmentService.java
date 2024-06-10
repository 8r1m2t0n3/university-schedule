package com.foxminded.university_schedule.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university_schedule.model.entity.Department;
import com.foxminded.university_schedule.repository.DepartmentRepository;

import jakarta.transaction.Transactional;

@Service
public class DepartmentService extends BaseService {

	private DepartmentRepository departmentRepository;

	private final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

	public DepartmentService(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

	@Transactional
	public void save(Department department) {
		departmentRepository.save(department);
		logger.info("Saved department with name: {}", department.getName());
	}

	public Optional<Department> getById(Integer departmentId) {
		return departmentRepository.findById(departmentId);
	}

	public Optional<Department> getByName(String departmentName) {
		return departmentRepository.findByName(departmentName);
	}
	
	public List<Department> getAll() {
		return departmentRepository.findAll();
	}
	
	@Transactional
	public void update(Department department, Integer departmentId) {
		department.setId(departmentId);
		departmentRepository.save(department);
		logger.info("Updated department with id: {}", department.getId());
	}

	@Transactional
	public void deleteById(Integer departmentId) {
		departmentRepository.deleteById(departmentId);
		logger.info("Deleted department with id: {}", departmentId);
	}
}
