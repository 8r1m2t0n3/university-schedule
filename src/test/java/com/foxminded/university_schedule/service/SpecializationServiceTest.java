package com.foxminded.university_schedule.service;

import static org.junit.jupiter.api.Assertions.*;
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

import com.foxminded.university_schedule.model.entity.Specialization;
import com.foxminded.university_schedule.repository.SpecializationRepository;

@ExtendWith(MockitoExtension.class)
class SpecializationServiceTest {

	@Mock
	SpecializationRepository specializationRepository;

	@InjectMocks
	SpecializationService specializationService;

	@Test
	void saveTest() {
		Specialization specialization = new Specialization();
		when(specializationRepository.save(specialization)).thenReturn(specialization);

		specializationService.save(specialization);

		verify(specializationRepository).save(specialization);
	}

	@Test
	void getByIdTest() {
		Specialization specialization = new Specialization();
		specialization.setId(1);
		when(specializationRepository.findById(specialization.getId())).thenReturn(Optional.of(specialization));

		assertEquals(1, specializationService.getById(specialization.getId()).get().getId());

		verify(specializationRepository).findById(1);
	}

	@Test
	void getByNameTest() {
		Specialization specialization = new Specialization();
		specialization.setName("specialization");
		when(specializationRepository.findByName(specialization.getName())).thenReturn(Optional.of(specialization));

		assertEquals("specialization", specializationService.getByName(specialization.getName()).get().getName());

		verify(specializationRepository).findByName("specialization");
	}
	
	@Test
	void getAllTest() {
		when(specializationRepository.findAll()).thenReturn(List.of(new Specialization(), new Specialization()));
		
		assertEquals(2, specializationService.getAll().size());
		
		verify(specializationRepository).findAll();
	}

	@Test
	void updateTest() {
		Specialization specialization = new Specialization();
		when(specializationRepository.save(specialization)).thenReturn(specialization);

		specializationService.update(specialization, 1);
		assertEquals(1, specialization.getId());

		verify(specializationRepository).save(specialization);
	}

	@Test
	void deleteByIdTest() {
		doNothing().when(specializationRepository).deleteById(1);

		specializationService.deleteById(1);

		verify(specializationRepository).deleteById(1);
	}
}
