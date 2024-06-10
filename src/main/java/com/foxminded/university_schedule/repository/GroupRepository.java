package com.foxminded.university_schedule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foxminded.university_schedule.model.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
	
	Optional<Group> findByName(String groupName);
}
