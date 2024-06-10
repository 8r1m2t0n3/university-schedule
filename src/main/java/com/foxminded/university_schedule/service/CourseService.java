package com.foxminded.university_schedule.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university_schedule.model.entity.Course;
import com.foxminded.university_schedule.repository.CourseRepository;

import jakarta.transaction.Transactional;

@Service
public class CourseService extends BaseService {

	private CourseRepository courseRepository;

	private Logger logger = LoggerFactory.getLogger(CourseService.class);

	public CourseService(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	@Transactional
	public void save(Course course) {
		courseRepository.save(course);
		logger.info("Saved course with name: {}", course.getName());
	}

	public Optional<Course> getById(Integer courseId) {
		return courseRepository.findById(courseId);
	}
	
	public Optional<Course> getByName(String courseName) {
		return courseRepository.findByName(courseName);
	}
	
	public List<Course> getAll() {
		return courseRepository.findAll();
	}

	@Transactional
	public void update(Course course, Integer courseId) {
		course.setId(courseId);
		courseRepository.save(course);
		logger.info("Updated course with id: {}", course.getId());
	}

	@Transactional
	public void deleteById(Integer courseId) {
		courseRepository.deleteById(courseId);
		logger.info("Deleted course with id: {}", courseId);
	}

	public Boolean isExistById(Integer courseId) {
		if (getById(courseId).isEmpty()) {
			logger.error("No course with id: {}", courseId);
			return false;
		}
		return true;
	}
}
