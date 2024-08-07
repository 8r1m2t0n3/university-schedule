package com.brimstone.university_schedule.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.brimstone.university_schedule.model.entity.Group;
import com.brimstone.university_schedule.model.entity.Lesson;
import com.brimstone.university_schedule.model.entity.Teacher;
import com.brimstone.university_schedule.repository.LessonRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

	@Mock
  LessonRepository lessonRepository;
	
	@InjectMocks
	LessonService lessonService;
	
	@Test
	void saveTest() {
		Lesson lesson = new Lesson();
		when(lessonRepository.save(lesson)).thenReturn(lesson);
		
		lessonService.save(lesson);
		
		verify(lessonRepository).save(lesson);
	}
	
	@Test
	void getByIdTest() {
		Lesson lesson = new Lesson();
		lesson.setId(1);
		when(lessonRepository.findById(lesson.getId())).thenReturn(Optional.of(lesson));
		
		Assertions.assertEquals(1, lessonService.getById(lesson.getId()).get().getId());
		
		verify(lessonRepository).findById(1);
	}

	@Test
	void getAllTest() {
		when(lessonRepository.findAll()).thenReturn(List.of(new Lesson(), new Lesson()));
		
		assertEquals(2, lessonService.getAll().size());
		
		verify(lessonRepository).findAll();
	}
	
	@Test
	void getListByPeriodTest() {
		when(lessonRepository.findByPeriod(LocalDate.now(), LocalDate.now())).thenReturn(List.of(new Lesson(), new Lesson()));
		
		assertEquals(2, lessonService.getListByPeriod(LocalDate.now(), LocalDate.now()).size());
		
		verify(lessonRepository).findByPeriod(LocalDate.now(), LocalDate.now());
	}
	
	@Test
	void updateTest() {
		Lesson lesson = new Lesson();
		when(lessonRepository.save(lesson)).thenReturn(lesson);
		
		lessonService.update(lesson, 1);
		assertEquals(1, lesson.getId());
		
		verify(lessonRepository).save(lesson);
	}
	
	@Test
	void deleteByIdTest() {
		doNothing().when(lessonRepository).deleteById(1);
		
		lessonService.deleteById(1);
		
		verify(lessonRepository).deleteById(1);
	}
	
	@Test 
	void getLessonsOfGroupForDateTest() {
		Lesson lesson1 = new Lesson(); 
		Group group1 = new Group();
		group1.setId(1);
		lesson1.setGroup(group1);
		Lesson lesson2 = new Lesson(); 
		Group group2 = new Group();
		group2.setId(2);
		lesson2.setGroup(group2);
		List<Lesson> lessons = new ArrayList<>();
		lessons.add(lesson1);
		lessons.add(lesson2);
		when(lessonRepository.findByPeriod(LocalDate.of(2023, 6, 13), LocalDate.of(2023, 6, 13))).thenReturn(lessons);
		
		assertEquals(List.of(lesson1), lessonService.getLessonsOfGroupForDate(group1, LocalDate.of(2023, 6, 13)));
	}
	
	@Test 
	void getLessonsOfTeacherForDateTest() {
		Lesson lesson1 = new Lesson(); 
		Teacher teacher1 = new Teacher();
		teacher1.setId(1);
		lesson1.setTeacher(teacher1);
		Lesson lesson2 = new Lesson(); 
		Teacher teacher2 = new Teacher();
		teacher2.setId(2);
		lesson2.setTeacher(teacher2);
		List<Lesson> lessons = new ArrayList<>();
		lessons.add(lesson1);
		lessons.add(lesson2);
		when(lessonRepository.findByPeriod(LocalDate.of(2023, 6, 13), LocalDate.of(2023, 6, 13))).thenReturn(lessons);
		
		assertEquals(List.of(lesson1), lessonService.getLessonsOfTeacherForDate(teacher1, LocalDate.of(2023, 6, 13)));
	}
}
