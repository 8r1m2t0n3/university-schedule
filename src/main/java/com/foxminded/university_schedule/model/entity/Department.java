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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "departments")
public class Department implements BaseModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "university_name")
	private String universityName;

	@OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Specialization> specializations = new ArrayList<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "building_id")
	private Building building;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "departments")
	private List<Teacher> teachers = new ArrayList<>();

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUniversityName() {
		return universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}

	public List<Specialization> getSpecializations() {
		return specializations;
	}

	public void addSpecialization(Specialization specialization) {
		specializations.add(specialization);
		specialization.setDepartment(this);
	}

	public void removeSpecialization(Specialization specialization) {
		specializations.remove(specialization);
		specialization.setDepartment(null);
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void addTeacher(Teacher teacher) {
		teachers.add(teacher);
		teacher.getDepartments().add(this);
	}

	public void removeTeacher(Teacher teacher) {
		teachers.remove(teacher);
		teacher.getDepartments().remove(this);
	}
	
	public Boolean isContainTeacherId(Integer teacherId) {
		return getTeachers().stream().map(Teacher::getId).toList().contains(teacherId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Department department = (Department) o;
		return Objects.equals(id, department.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
