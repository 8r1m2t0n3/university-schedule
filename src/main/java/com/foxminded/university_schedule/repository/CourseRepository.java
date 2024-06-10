package com.foxminded.university_schedule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foxminded.university_schedule.model.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
	
	Optional<Course> findByName(String courseName);
}
