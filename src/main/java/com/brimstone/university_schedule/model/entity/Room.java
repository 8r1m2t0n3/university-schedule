package com.brimstone.university_schedule.model.entity;

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
import jakarta.persistence.Table;

@Entity
@Table(name = "rooms")
public class Room implements BaseModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "number")
	private Integer number;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "building_id")
	private Building building;
	
	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Lesson> lessons = new ArrayList<>();

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void addLesson(Lesson lesson) {
		lessons.add(lesson);
		lesson.setRoom(this);
	}
	
	public void removeLesson(Lesson lesson) {
		lessons.remove(lesson);
		lesson.setRoom(null);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Room room = (Room) o;
		return Objects.equals(id, room.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
