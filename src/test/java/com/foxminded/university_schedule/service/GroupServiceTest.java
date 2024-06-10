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

import com.foxminded.university_schedule.model.entity.Group;
import com.foxminded.university_schedule.repository.GroupRepository;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

	@Mock
	GroupRepository groupRepository;
	
	@InjectMocks
	GroupService groupService;
	
	@Test
	void saveTest() {
		Group group = new Group();
		when(groupRepository.save(group)).thenReturn(group);
		
		groupService.save(group);
		
		verify(groupRepository).save(group);
	}
	
	@Test
	void getByIdTest() {
		Group group = new Group();
		group.setId(1);
		when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
		
		assertEquals(1, groupService.getById(group.getId()).get().getId());
		
		verify(groupRepository).findById(1);
	}

	
	@Test
	void getByNameTest() {
		Group group = new Group();
		group.setName("Aa");
		when(groupRepository.findByName(group.getName())).thenReturn(Optional.of(group));
		
		assertEquals("Aa", groupService.getByName(group.getName()).get().getName());
		
		verify(groupRepository).findByName("Aa");
	}
	
	@Test
	void getAllTest() {
		when(groupRepository.findAll()).thenReturn(List.of(new Group(), new Group()));
		
		assertEquals(2, groupService.getAll().size());
		
		verify(groupRepository).findAll();
	}
	
	@Test
	void updateTest() {
		Group group = new Group();
		when(groupRepository.save(group)).thenReturn(group);
		
		groupService.update(group, 1);
		assertEquals(1, group.getId());
		
		verify(groupRepository).save(group);
	}
	
	@Test
	void deleteByIdTest() {
		doNothing().when(groupRepository).deleteById(1);
		
		groupService.deleteById(1);
		
		verify(groupRepository).deleteById(1);
	}
	
	@Test
	void isExistById_shouldReturnFalse_whenNoEntityWithSpecifiedIdExists() {
		when(groupService.getById(1)).thenReturn(Optional.empty());
		assertFalse(groupService.isExistById(1));
	}
	
	@Test
	void isExistById_shouldReturnTrue_whenEntityWithSpecifiedIdExists() {
		when(groupService.getById(1)).thenReturn(Optional.of(new Group()));
		assertTrue(groupService.isExistById(1));
	}
}
