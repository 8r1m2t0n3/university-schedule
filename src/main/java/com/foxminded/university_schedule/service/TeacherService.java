package com.foxminded.university_schedule.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university_schedule.model.entity.Teacher;
import com.foxminded.university_schedule.repository.TeacherRepository;

import jakarta.transaction.Transactional;

@Service
public class TeacherService extends BaseService {

	private TeacherRepository teacherRepository;

	private Logger logger = LoggerFactory.getLogger(TeacherService.class);

	public TeacherService(TeacherRepository teacherRepository) {
		this.teacherRepository = teacherRepository;
	}

	@Transactional
	public void save(Teacher teacher) {
		teacherRepository.save(teacher);
		logger.info("Saved teacher with name: {}", teacher.getFirstName() + " " + teacher.getLastName());
	}

	public Optional<Teacher> getById(Integer teacherId) {
		return teacherRepository.findById(teacherId);
	}

	public Optional<Teacher> getByFirstNameAndLastName(String firstName, String lastName) {
		return teacherRepository.findByFirstNameAndLastName(firstName, lastName);
	}
	
	public List<Teacher> getAll() {
		return teacherRepository.findAll();
	}

	@Transactional
	public void update(Teacher teacher, Integer teacherId) {
		teacher.setId(teacherId);
		teacherRepository.save(teacher);
		logger.info("Updated teacher with id: {}", teacher.getId());
	}

	@Transactional
	public void deleteById(Integer teacherId) {
		teacherRepository.deleteById(teacherId);
		logger.info("Deleted teacher with id: {}", teacherId);
	}

	public Boolean isExistById(Integer teacherId) {
		if (getById(teacherId).isEmpty()) {
			logger.error("Error! No teacher with id: {}", teacherId);
			return false;
		}
		return true;
	}
}
