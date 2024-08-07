package com.brimstone.university_schedule.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.brimstone.university_schedule.model.entity.Lesson;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ExtendWith(SpringExtension.class)
@DataJpaTest
class LessonRepositoryTest {

	@Autowired
	LessonRepository lessonRepository;
	
	@Sql({ "/test_tables/drop/drop_lessons.sql", "/test_tables/create/create_lessons.sql" })
	@Sql(statements = "insert into lessons(date) values ('2023-01-31')")
	@Sql(statements = "insert into lessons(date) values ('2023-01-30')")
	@Sql(statements = "insert into lessons(date) values ('2023-01-29')")
	@Test
	void findByPeriod_shouldReturnListLengthTwo_whenOneLessonOutOfDiapason() {
		List<Lesson> lessons = lessonRepository.findByPeriod(LocalDate.of(2023, 1, 29), LocalDate.of(2023, 1, 30));
		assertEquals(2, lessons.size());
	}
	
	@Sql({ "/test_tables/drop/drop_lessons.sql", "/test_tables/create/create_lessons.sql" })
	@Sql(statements = "insert into lessons(date) values ('2023-01-31')")
	@Sql(statements = "insert into lessons(date) values ('2023-01-30')")
	@Sql(statements = "insert into lessons(date) values ('2023-01-29')")
	@Test
	void findByPeriod_shouldReturnEmptyList_whenNoLessonsInDiapason() {
		List<Lesson> lessons = lessonRepository.findByPeriod(LocalDate.of(2022, 1, 29), LocalDate.of(2022, 1, 30));
		assertEquals(0, lessons.size());
	}
}
