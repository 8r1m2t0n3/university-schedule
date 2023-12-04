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

import com.foxminded.university_schedule.model.Student;
import com.foxminded.university_schedule.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

	@Mock
	StudentRepository studentRepository;
	
	@InjectMocks
	StudentService studentService;
	
	@Test
	void saveTest() {
		Student student = new Student();
		when(studentRepository.save(student)).thenReturn(student);

		studentService.save(student);

		verify(studentRepository).save(student);
	}

	@Test
	void getByIdTest() {
		Student student = new Student();
		student.setId(1);
		when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

		assertEquals(1, studentService.getById(student.getId()).get().getId());

		verify(studentRepository).findById(1);
	}

	@Test
	void getByFirstNameAndLastNameTest() {
		Student student = new Student();
		student.setFirstName("A");
		student.setLastName("Aa");
		when(studentRepository.findByFirstNameAndLastName(student.getFirstName(), student.getLastName())).thenReturn(Optional.of(student));

		assertEquals("A", studentService.getByFirstNameAndLastName(student.getFirstName(), student.getLastName()).get().getFirstName());

		verify(studentRepository).findByFirstNameAndLastName("A", "Aa");
	}
	
	@Test
	void getAllTest() {
		when(studentRepository.findAll()).thenReturn(List.of(new Student(), new Student()));
		
		assertEquals(2, studentService.getAll().size());
		
		verify(studentRepository).findAll();
	}

	@Test
	void updateTest() {
		Student student = new Student();
		when(studentRepository.save(student)).thenReturn(student);

		studentService.update(student, 1);
		assertEquals(1, student.getId());

		verify(studentRepository).save(student);
	}

	@Test
	void deleteByIdTest() {
		doNothing().when(studentRepository).deleteById(1);

		studentService.deleteById(1);

		verify(studentRepository).deleteById(1);
	}
}
