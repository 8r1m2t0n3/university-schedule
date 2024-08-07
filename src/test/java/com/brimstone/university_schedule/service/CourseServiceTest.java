package com.brimstone.university_schedule.service;

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

import com.brimstone.university_schedule.model.entity.Course;
import com.brimstone.university_schedule.repository.CourseRepository;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

	@Mock
	CourseRepository courseRepository;
	
	@InjectMocks
	CourseService courseService;
	
	@Test
	void saveTest() {
		Course course = new Course();
		when(courseRepository.save(course)).thenReturn(course);
		
		courseService.save(course);
		
		verify(courseRepository).save(course);
	}
	
	@Test
	void getByIdTest() {
		Course course = new Course();
		course.setId(1);
		when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
		
		assertEquals(1, courseService.getById(course.getId()).get().getId());
		
		verify(courseRepository).findById(1);
	}
	
	@Test
	void getByNameTest() {
		Course course = new Course();
		course.setName("math");
		when(courseRepository.findByName(course.getName())).thenReturn(Optional.of(course));
		
		assertEquals("math", courseService.getByName(course.getName()).get().getName());
		
		verify(courseRepository).findByName("math");
	}

	@Test
	void getAllTest() {
		when(courseRepository.findAll()).thenReturn(List.of(new Course(), new Course()));
		
		assertEquals(2, courseService.getAll().size());
		
		verify(courseRepository).findAll();
	}
	
	@Test
	void updateTest() {
		Course course = new Course();
		when(courseRepository.save(course)).thenReturn(course);
		
		courseService.update(course, 1);
		assertEquals(1, course.getId());
		
		verify(courseRepository).save(course);
	}
	
	@Test
	void deleteByIdTest() {
		doNothing().when(courseRepository).deleteById(1);
		
		courseService.deleteById(1);
		
		verify(courseRepository).deleteById(1);
	}
	
	@Test
	void isExistById_shouldReturnFalse_whenNoEntityWithSpecifiedIdExists() {
		when(courseService.getById(1)).thenReturn(Optional.empty());
		assertFalse(courseService.isExistById(1));
	}
	
	@Test
	void isExistById_shouldReturnTrue_whenEntityWithSpecifiedIdExists() {
		when(courseService.getById(1)).thenReturn(Optional.of(new Course()));
		assertTrue(courseService.isExistById(1));
	}
}
