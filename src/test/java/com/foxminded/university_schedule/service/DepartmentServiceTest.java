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

import com.foxminded.university_schedule.model.Department;
import com.foxminded.university_schedule.repository.DepartmentRepository;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

	@Mock
	DepartmentRepository departmentRepository;
	
	@InjectMocks
	DepartmentService departmentService;
	
	@Test
	void saveTest() {
		Department department = new Department();
		when(departmentRepository.save(department)).thenReturn(department);
		
		departmentService.save(department);
		
		verify(departmentRepository).save(department);
	}
	
	@Test
	void getByIdTest() {
		Department department = new Department();
		department.setId(1);
		when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
		
		assertEquals(1, departmentService.getById(department.getId()).get().getId());
		
		verify(departmentRepository).findById(1);
	}
	
	@Test
	void getByNameTest() {
		Department department = new Department();
		department.setName("department");
		when(departmentRepository.findByName(department.getName())).thenReturn(Optional.of(department));
		
		assertEquals("department", departmentService.getByName(department.getName()).get().getName());
		
		verify(departmentRepository).findByName("department");
	}

	@Test
	void getAllTest() {
		when(departmentRepository.findAll()).thenReturn(List.of(new Department(), new Department()));
		
		assertEquals(2, departmentService.getAll().size());
		
		verify(departmentRepository).findAll();
	}
	
	@Test
	void updateTest() {
		Department department = new Department();
		when(departmentRepository.save(department)).thenReturn(department);
		
		departmentService.update(department, 1);
		assertEquals(1, department.getId());
		
		verify(departmentRepository).save(department);
	}
	
	@Test
	void deleteByIdTest() {
		doNothing().when(departmentRepository).deleteById(1);
		
		departmentService.deleteById(1);
		
		verify(departmentRepository).deleteById(1);
	}
}
