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

import com.foxminded.university_schedule.model.entity.Teacher;
import com.foxminded.university_schedule.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

	@Mock
	TeacherRepository teacherRepository;

	@InjectMocks
	TeacherService teacherService;

	@Test
	void saveTest() {
		Teacher teacher = new Teacher();
		when(teacherRepository.save(teacher)).thenReturn(teacher);

		teacherService.save(teacher);

		verify(teacherRepository).save(teacher);
	}

	@Test
	void getByIdTest() {
		Teacher teacher = new Teacher();
		teacher.setId(1);
		when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.of(teacher));

		assertEquals(1, teacherService.getById(teacher.getId()).get().getId());

		verify(teacherRepository).findById(1);
	}

	@Test
	void getByFirstNameAndLastNameTest() {
		Teacher teacher = new Teacher();
		teacher.setFirstName("A");
		teacher.setLastName("Aa");
		when(teacherRepository.findByFirstNameAndLastName(teacher.getFirstName(), teacher.getLastName()))
				.thenReturn(Optional.of(teacher));

		assertEquals("A", teacherService.getByFirstNameAndLastName(teacher.getFirstName(), teacher.getLastName()).get()
				.getFirstName());

		verify(teacherRepository).findByFirstNameAndLastName("A", "Aa");
	}

	@Test
	void getAllTest() {
		when(teacherRepository.findAll()).thenReturn(List.of(new Teacher(), new Teacher()));
		
		assertEquals(2, teacherService.getAll().size());
		
		verify(teacherRepository).findAll();
	}

	@Test
	void updateTest() {
		Teacher teacher = new Teacher();
		when(teacherRepository.save(teacher)).thenReturn(teacher);

		teacherService.update(teacher, 1);
		assertEquals(1, teacher.getId());

		verify(teacherRepository).save(teacher);
	}

	@Test
	void deleteByIdTest() {
		doNothing().when(teacherRepository).deleteById(1);

		teacherService.deleteById(1);

		verify(teacherRepository).deleteById(1);
	}

	@Test
	void isExistById_shouldReturnFalse_whenNoEntityWithSpecifiedIdExists() {
		when(teacherService.getById(1)).thenReturn(Optional.empty());
		assertFalse(teacherService.isExistById(1));
	}

	@Test
	void isExistById_shouldReturnTrue_whenEntityWithSpecifiedIdExists() {
		when(teacherService.getById(1)).thenReturn(Optional.of(new Teacher()));
		assertTrue(teacherService.isExistById(1));
	}
}
