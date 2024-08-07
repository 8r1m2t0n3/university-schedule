package com.brimstone.university_schedule.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "teachers")
public class Teacher implements BaseModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "teachers")
	private List<Course> courses = new ArrayList<>();

	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Lesson> lessons = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "teachers_departments", joinColumns = @JoinColumn(name = "teacher_id"), inverseJoinColumns = @JoinColumn(name = "department_id"))
	private List<Department> departments = new ArrayList<>();

	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void addCourse(Course course) {
		courses.add(course);
		course.getTeachers().add(this);
	}
	
	public void removeCourse(Course course) {
		courses.remove(course);
		course.getTeachers().remove(this);
	}
	
	public List<Lesson> getLessons() {
		return lessons;
	}

	public void addLesson(Lesson lesson) {
		lessons.add(lesson);
		lesson.setTeacher(this);
	}
	
	public void removeLesson(Lesson lesson) {
		lessons.remove(lesson);
		lesson.setTeacher(null);
	}
	
	public List<Department> getDepartments() {
		return departments;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Teacher teacher = (Teacher) o;
		return Objects.equals(id, teacher.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	public boolean isContainCourseId(Integer courseId) {
		return getCourses().stream().map(Course::getId).toList().contains(courseId);
	}
	
	public boolean isContainLessonId(Integer lessonId) {
		return getLessons().stream().map(Lesson::getId).toList().contains(lessonId);
	}
}
