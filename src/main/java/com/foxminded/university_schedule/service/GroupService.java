package com.foxminded.university_schedule.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university_schedule.model.Group;
import com.foxminded.university_schedule.repository.GroupRepository;

import jakarta.transaction.Transactional;

@Service
public class GroupService extends BaseService {

	private GroupRepository groupRepository;

	private Logger logger = LoggerFactory.getLogger(GroupService.class);

	public GroupService(GroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}

	@Transactional
	public void save(Group group) {
		groupRepository.save(group);
		logger.info("Saved group with name: {}", group.getName());
	}

	public Optional<Group> getById(Integer departmentId) {
		return groupRepository.findById(departmentId);
	}
	
	public Optional<Group> getByName(String groupName) {
		return groupRepository.findByName(groupName);
	}

	public List<Group> getAll() {
		return groupRepository.findAll();
	}
	
	@Transactional
	public void update(Group group, Integer groupId) {
		group.setId(groupId);
		groupRepository.save(group);
		logger.info("Updated group with id: {}", group.getId());
	}

	@Transactional
	public void deleteById(Integer groupId) {
		groupRepository.deleteById(groupId);
		logger.info("Deleted group with id: {}", groupId);
	}
	
	public Boolean isExistById(Integer groupId) {
		if (getById(groupId).isEmpty()) {
			logger.error("Error! No group with id: {}", groupId);
			return false;
		}
		return true;
	}
}
