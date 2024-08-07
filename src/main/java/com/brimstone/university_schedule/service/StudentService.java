package com.brimstone.university_schedule.service;

import com.brimstone.university_schedule.repository.StudentRepository;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.brimstone.university_schedule.model.entity.Student;

import jakarta.transaction.Transactional;

@Service
public class StudentService extends BaseService {

	private final StudentRepository studentRepository;

	private final Logger logger = LoggerFactory.getLogger(StudentService.class);

	public StudentService(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	@Transactional
	public void save(Student student) {
		studentRepository.save(student);
		logger.info("Saved student with name: {}", student.getFirstName() + " " + student.getLastName());
	}

	public Optional<Student> getById(Integer studentId) {
		return studentRepository.findById(studentId);
	}

	public Optional<Student> getByFirstNameAndLastName(String firstName, String lastName) {
		return studentRepository.findByFirstNameAndLastName(firstName, lastName);
	}
	
	public List<Student> getAll() {
		return studentRepository.findAll();
	}
	
	@Transactional
	public void update(Student student, Integer studentId) {
		student.setId(studentId);
		studentRepository.save(student);
		logger.info("Updated student with id: {}", student.getId());
	}

	@Transactional
	public void deleteById(Integer studentId) {
		studentRepository.deleteById(studentId);
		logger.info("Deleted student with id: {}", studentId);
	}
}
