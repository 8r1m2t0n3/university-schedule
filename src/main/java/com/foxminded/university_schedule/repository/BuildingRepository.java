package com.foxminded.university_schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foxminded.university_schedule.model.Building;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Integer> {}
