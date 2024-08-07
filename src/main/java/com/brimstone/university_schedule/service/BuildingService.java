package com.brimstone.university_schedule.service;

import com.brimstone.university_schedule.repository.BuildingRepository;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.brimstone.university_schedule.model.entity.Building;

import jakarta.transaction.Transactional;

@Service
public class BuildingService extends BaseService {

	private final BuildingRepository buildingRepository;

	private final static Logger logger = LoggerFactory.getLogger(BuildingService.class);
	
	public BuildingService(BuildingRepository buildingRepository) {
		this.buildingRepository = buildingRepository;
	}

	@Transactional
	public void save(Building building) {
		buildingRepository.save(building);
		logger.info("Saved building with address: {}", building.getAddress());
	}

	public Optional<Building> getById(Integer buildingId) {
		return buildingRepository.findById(buildingId);
	}

	public List<Building> getAll() {
		return buildingRepository.findAll();
	}
	
	@Transactional
	public void update(Building building, Integer buildingId) {
		building.setId(buildingId);
		buildingRepository.save(building);
		logger.info("Updated building with id: {}", building.getId());
	}

	@Transactional
	public void deleteById(Integer buildingId) {
		buildingRepository.deleteById(buildingId);
		logger.info("Deleted building with id: {}", buildingId);
	}
}
