package com.brimstone.university_schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brimstone.university_schedule.model.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {}
