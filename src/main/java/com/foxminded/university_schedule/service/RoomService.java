package com.foxminded.university_schedule.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university_schedule.model.Room;
import com.foxminded.university_schedule.repository.RoomRepository;

import jakarta.transaction.Transactional;

@Service
public class RoomService extends BaseService {

	private RoomRepository roomRepository;

	private Logger logger = LoggerFactory.getLogger(RoomService.class);

	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	@Transactional
	public void save(Room room) {
		roomRepository.save(room);
		logger.info("Saved room with number: {}", room.getNumber());
	}

	public Optional<Room> getById(Integer roomId) {
		return roomRepository.findById(roomId);
	}

	public List<Room> getAll() {
		return roomRepository.findAll();
	}

	@Transactional
	public void update(Room room, Integer roomId) {
		room.setId(roomId);
		roomRepository.save(room);
		logger.info("Updated room with id: {}", room.getId());
	}

	@Transactional
	public void deleteById(Integer roomId) {
		roomRepository.deleteById(roomId);
		logger.info("Deleted room with id: {}", roomId);
	}

	public Boolean isExistById(Integer roomId) {
		if (getById(roomId).isEmpty()) {
			logger.error("Error! No room with id: {}", roomId);
			return false;
		}
		return true;
	}
}
