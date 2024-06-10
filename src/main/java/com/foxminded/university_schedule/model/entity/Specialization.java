package com.foxminded.university_schedule.model.entity;

import java.util.ArrayList;
import java.util.List;

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
@Table(name = "specializations")
public class Specialization implements BaseModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "department_id")
	private Department department;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "specializations")
	private List<Course> courses = new ArrayList<>();

	@OneToMany(mappedBy = "specialization" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Group> groups = new ArrayList<>();

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

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void addCourse(Course course) {
		courses.add(course);
		course.getSpecializations().add(this);
	}
	
	public void removeCourse(Course course) {
		courses.remove(course);
		course.getSpecializations().remove(this);
	}
	
	public List<Group> getGroups() {
		return groups;
	}

	public void addGroup(Group group) {
		groups.add(group);
		group.setSpecialization(this);
	}

	public void removeGroup(Group group) {
		groups.remove(group);
		group.setSpecialization(null);
	}
	
	public boolean isContainCourseId(Integer courseId) {
		return getCourses().stream().map(Course::getId).toList().contains(courseId);
	}
	
	public boolean isContainGroupId(Integer groupId) {
		return getGroups().stream().map(Group::getId).toList().contains(groupId);
	}
}
