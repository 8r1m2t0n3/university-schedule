package com.foxminded.university_schedule.model.entity;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;

@Entity
@Table(name = "groups")
public class Group implements BaseModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "grade")
	private Integer grade;
	
	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Student> students = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "specialization_id")
	private Specialization specialization;
	
	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Lesson> lessons = new ArrayList<>();

	@PreRemove
	public void setGroupForStudentsToNull() {
		students.forEach(o->o.setGroup(null));
		students.clear();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	
	public List<Student> getStudents() {
		return students;
	}

	public void addStudent(Student student) {
		students.add(student);
		student.setGroup(this);
	}
	
	public void removeStudent(Student student) {
		students.remove(student);
		student.setGroup(null);
	}

	public Specialization getSpecialization() {
		return specialization;
	}

	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}
	
	public void removeSpecialization() {
		specialization.removeGroup(this);
		setSpecialization(null);
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void addLesson(Lesson lesson) {
		lessons.add(lesson);
		lesson.setGroup(this);
	}
	
	public void removeLesson(Lesson lesson) {
		lessons.remove(lesson);
		lesson.setGroup(null);
	}
	
	public boolean isContainStudentId(Integer studentId) {
		return getStudents().stream().map(Student::getId).toList().contains(studentId);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Group group = (Group) o;
		return Objects.equals(id, group.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
