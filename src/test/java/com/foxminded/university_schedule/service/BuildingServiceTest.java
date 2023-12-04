package com.foxminded.university_schedule.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university_schedule.model.Building;
import com.foxminded.university_schedule.repository.BuildingRepository;

@ExtendWith(MockitoExtension.class)
class BuildingServiceTest {

	@Mock
	BuildingRepository buildingRepository;
	
	@InjectMocks
	BuildingService buildingService;
	
	@Test
	void saveTest() {
		Building building = new Building();
		when(buildingRepository.save(building)).thenReturn(building);
		
		buildingService.save(building);
		
		verify(buildingRepository).save(building);
	}
	
	@Test
	void getByIdTest() {
		Building building = new Building();
		building.setId(1);
		when(buildingRepository.findById(building.getId())).thenReturn(Optional.of(building));
		
		assertEquals(1, buildingService.getById(building.getId()).get().getId());
		
		verify(buildingRepository).findById(1);
	}

	@Test
	void getAllTest() {
		when(buildingRepository.findAll()).thenReturn(List.of(new Building(), new Building()));
		
		assertEquals(2, buildingService.getAll().size());
		
		verify(buildingRepository).findAll();
	}
	
	@Test
	void updateTest() {
		Building building = new Building();
		when(buildingRepository.save(building)).thenReturn(building);
		
		buildingService.update(building, 1);
		assertEquals(1, building.getId());
		
		verify(buildingRepository).save(building);
	}
	
	@Test
	void deleteByIdTest() {
		doNothing().when(buildingRepository).deleteById(1);
		
		buildingService.deleteById(1);
		
		verify(buildingRepository).deleteById(1);
	}
}
