package com.foxminded.university_schedule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foxminded.university_schedule.model.entity.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
	
	Optional<Teacher> findByFirstNameAndLastName(String firstName, String lastName);
}
