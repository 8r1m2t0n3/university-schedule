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

import com.foxminded.university_schedule.model.Room;
import com.foxminded.university_schedule.repository.RoomRepository;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

	@Mock
	RoomRepository roomRepository;

	@InjectMocks
	RoomService roomService;

	@Test
	void saveTest() {
		Room room = new Room();
		when(roomRepository.save(room)).thenReturn(room);

		roomService.save(room);

		verify(roomRepository).save(room);
	}

	@Test
	void getByIdTest() {
		Room room = new Room();
		room.setId(1);
		when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

		assertEquals(1, roomService.getById(room.getId()).get().getId());

		verify(roomRepository).findById(1);
	}

	@Test
	void getAllTest() {
		when(roomRepository.findAll()).thenReturn(List.of(new Room(), new Room()));
		
		assertEquals(2, roomService.getAll().size());
		
		verify(roomRepository).findAll();
	}

	@Test
	void updateTest() {
		Room room = new Room();
		when(roomRepository.save(room)).thenReturn(room);

		roomService.update(room, 1);
		assertEquals(1, room.getId());

		verify(roomRepository).save(room);
	}

	@Test
	void deleteByIdTest() {
		doNothing().when(roomRepository).deleteById(1);

		roomService.deleteById(1);

		verify(roomRepository).deleteById(1);
	}

	@Test
	void isExistById_shouldReturnFalse_whenNoEntityWithSpecifiedIdExists() {
		when(roomService.getById(1)).thenReturn(Optional.empty());
		assertFalse(roomService.isExistById(1));
	}

	@Test
	void isExistById_shouldReturnTrue_whenEntityWithSpecifiedIdExists() {
		when(roomService.getById(1)).thenReturn(Optional.of(new Room()));
		assertTrue(roomService.isExistById(1));
	}
}
