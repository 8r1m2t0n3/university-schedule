package com.brimstone.university_schedule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brimstone.university_schedule.model.entity.Specialization;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Integer> {
	
	Optional<Specialization> findByName(String specializationName);
}
