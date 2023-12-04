package com.foxminded.university_schedule.model;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "buildings")
public class Building implements BaseModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "address")
	private String address;

	@OneToMany(mappedBy = "building", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Department> departments = new ArrayList<>();

	@Column(name = "university_name")
	private String universityName;

	@OneToMany(mappedBy = "building", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Room> rooms = new ArrayList<>();

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void addDepartment(Department department) {
		departments.add(department);
		department.setBuilding(this);
	}

	public void removeDepartment(Department department) {
		departments.remove(department);
		department.setBuilding(null);
	}

	public String getUniversityName() {
		return universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void addRoom(Room room) {
		rooms.add(room);
		room.setBuilding(this);
	}

	public void removeRoom(Room room) {
		rooms.remove(room);
		room.setBuilding(null);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Building building = (Building) o;
		return Objects.equals(id, building.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
