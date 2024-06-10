package com.foxminded.university_schedule.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.foxminded.university_schedule.model.entity.Course;

@ExtendWith(MockitoExtension.class)
class BaseServiceTest {

	@InjectMocks
	BaseService baseService;
	
	@Test
	void sortListByIdFromMoreToLess_shouldSortListFromMoreToLess_whenListIsNotEmpty() {
		List<Course> list = new ArrayList<>();
		Course course1 = new Course();
		course1.setId(1);
		Course course2 = new Course();
		course2.setId(2);
		Course course3 = new Course();
		course3.setId(3);
		
		list.add(course3);
		list.add(course1);
		list.add(course2);
		
		assertEquals(3, list.get(0).getId());
		assertEquals(1, list.get(1).getId());
		assertEquals(2, list.get(2).getId());
		
		baseService.sortListByIdFromMoreToLess(list);
		
		assertEquals(3, list.get(0).getId());
		assertEquals(2, list.get(1).getId());
		assertEquals(1, list.get(2).getId());
	}
	
	@Test
	void sortListByIdFromMoreToLess_shouldReturnEmptyList_whenListIsEmpty() {
		List<Course> list = new ArrayList<>();
		baseService.sortListByIdFromMoreToLess(list);
		assertTrue(list.isEmpty());
	}
	
	@Test
	void sortListByIdFromLessToMore_shouldSortListFromLessToMore_whenListIsNotEmpty() {
		List<Course> list = new ArrayList<>();
		Course course1 = new Course();
		course1.setId(1);
		Course course2 = new Course();
		course2.setId(2);
		Course course3 = new Course();
		course3.setId(3);
		
		list.add(course3);
		list.add(course1);
		list.add(course2);
		
		assertEquals(3, list.get(0).getId());
		assertEquals(1, list.get(1).getId());
		assertEquals(2, list.get(2).getId());
		
		baseService.sortListByIdFromLessToMore(list);
		
		assertEquals(1, list.get(0).getId());
		assertEquals(2, list.get(1).getId());
		assertEquals(3, list.get(2).getId());
	}
	
	@Test
	void sortListByIdFromLessToMore_shouldReturnEmptyList_whenListIsEmpty() {
		List<Course> list = new ArrayList<>();
		baseService.sortListByIdFromLessToMore(list);
		assertTrue(list.isEmpty());
	}
	
	@Test
	void findInListById_shouldFindCourseById_whenListIsNotEmpty() {
		List<Course> list = new ArrayList<>();
		Course course1 = new Course();
		course1.setId(1);
		Course course2 = new Course();
		course2.setId(2);
		Course course3 = new Course();
		course3.setId(3);
		
		list.add(course1);
		list.add(course2);
		list.add(course3);
		
		Optional<Course> course = baseService.findInListById(list, 2);
		
		assertTrue(course.isPresent());
		assertEquals(2, course.get().getId());
	}
	
	@Test
	void findInListById_shouldReturnOptionalEmpty_whenListIsEmpty() {
		List<Course> list = new ArrayList<>();
		
		Optional<Course> course = baseService.findInListById(list, 2);
		
		assertTrue(course.isEmpty());
	}
}
