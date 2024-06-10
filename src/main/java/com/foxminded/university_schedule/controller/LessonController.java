package com.foxminded.university_schedule.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university_schedule.model.entity.Course;
import com.foxminded.university_schedule.model.entity.Group;
import com.foxminded.university_schedule.model.entity.Lesson;
import com.foxminded.university_schedule.model.entity.Room;
import com.foxminded.university_schedule.model.entity.Teacher;
import com.foxminded.university_schedule.service.CourseService;
import com.foxminded.university_schedule.service.GroupService;
import com.foxminded.university_schedule.service.LessonService;
import com.foxminded.university_schedule.service.RoomService;
import com.foxminded.university_schedule.service.TeacherService;

@Controller
@RequestMapping("/lesson")
public class LessonController {
	
	private LessonService lessonService;
	private CourseService courseService;
	private TeacherService teacherService;
	private GroupService groupService;
	private RoomService roomService;
	
	public LessonController(LessonService lessonService, CourseService courseService, TeacherService teacherService,
			GroupService groupService, RoomService roomService) {
		this.lessonService = lessonService;
		this.courseService = courseService;
		this.teacherService = teacherService;
		this.groupService = groupService;
		this.roomService = roomService;
	}
	
	@GetMapping
	public String getLessonPage(Model model) {
		model.addAttribute("courses", courseService.sortListByIdFromLessToMore(courseService.getAll()));
		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));
		model.addAttribute("rooms", roomService.sortListByIdFromLessToMore(roomService.getAll()));
		model.addAttribute("lessons", lessonService.sortListByIdFromMoreToLess(lessonService.getAll()));
		
		return "admin/model/lesson";
	}
	
	@PostMapping("/create")
	public String createLesson(Model model) {
		lessonService.save(new Lesson());
		
		model.addAttribute("courses", courseService.sortListByIdFromLessToMore(courseService.getAll()));
		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));
		model.addAttribute("rooms", roomService.sortListByIdFromLessToMore(roomService.getAll()));
		model.addAttribute("lessons", lessonService.sortListByIdFromMoreToLess(lessonService.getAll()));
		
		return "admin/model/lesson"; 
	}
	
	@PostMapping("/update")
	public String updateLesson(Model model, @ModelAttribute("lesson") Lesson lessonNewInfo) {
		List<Lesson> lessons = lessonService.getAll();
		
		Optional<Lesson> lesson = lessonService.findInListById(lessons, lessonNewInfo.getId());
		
		lesson.ifPresent(l -> {
			l.setDate(lessonNewInfo.getDate());
			l.setStartTime(lessonNewInfo.getStartTime());
			l.setEndTime(lessonNewInfo.getEndTime());
			lessonService.update(l, lessonNewInfo.getId());
		});
		
		model.addAttribute("courses", courseService.sortListByIdFromLessToMore(courseService.getAll()));
		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));
		model.addAttribute("rooms", roomService.sortListByIdFromLessToMore(roomService.getAll()));
		model.addAttribute("lessons",lessonService.sortListByIdFromMoreToLess(lessons));
		
		return "admin/model/lesson"; 
	}
	
	@PostMapping("/delete")
	public String deleteLesson(Model model, @ModelAttribute("lessonId") int lessonId) {
		lessonService.deleteById(lessonId);
		
		model.addAttribute("courses", courseService.sortListByIdFromLessToMore(courseService.getAll()));
		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));
		model.addAttribute("rooms", roomService.sortListByIdFromLessToMore(roomService.getAll()));
		model.addAttribute("lessons", lessonService.sortListByIdFromMoreToLess(lessonService.getAll()));
		
		return "admin/model/lesson"; 
	}
	
	@PostMapping("/set-course")
	public String setCourse(Model model,
			@ModelAttribute("lessonId") int lessonId,
			@ModelAttribute("courseId") int courseId) {
		List<Lesson> lessons = lessonService.getAll();
		List<Course> courses = courseService.getAll();
		
		Optional<Lesson> lesson = lessonService.findInListById(lessons, lessonId);
		Optional<Course> course = courseService.findInListById(courses, courseId);
		
		lesson.ifPresent(l -> course.ifPresent(c -> {
			if (l.getCourse() == null || !l.getCourse().getId().equals(c.getId())) {
				l.setCourse(c);
				lessonService.update(l, lessonId);
			}
		}));
		
		model.addAttribute("courses", courseService.sortListByIdFromLessToMore(courses));
		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));
		model.addAttribute("rooms", roomService.sortListByIdFromLessToMore(roomService.getAll()));
		model.addAttribute("lessons", lessonService.sortListByIdFromMoreToLess(lessons));
		
		return "admin/model/lesson"; 
	}
	
	@PostMapping("/remove-course")
	public String removeCourse(Model model,
			@ModelAttribute("lessonId") int lessonId,
			@ModelAttribute("courseId") int courseId) {
		List<Lesson> lessons = lessonService.getAll();
		
		Optional<Lesson> lesson = lessonService.findInListById(lessons, lessonId);
		
		lesson.ifPresent(l -> {
			if (l.getCourse() != null && l.getCourse().getId().equals(courseId)) {
				l.setCourse(null);
				lessonService.update(l, lessonId);
			}
		});
		
		model.addAttribute("courses", courseService.sortListByIdFromLessToMore(courseService.getAll()));
		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));
		model.addAttribute("rooms", roomService.sortListByIdFromLessToMore(roomService.getAll()));
		model.addAttribute("lessons", lessonService.sortListByIdFromMoreToLess(lessons));
		
		return "admin/model/lesson"; 
	}
	
	@PostMapping("/set-teacher")
	public String setTeacher(Model model,
			@ModelAttribute("lessonId") int lessonId,
			@ModelAttribute("teacherId") int teacherId) {
		List<Lesson> lessons = lessonService.getAll();
		List<Teacher> teachers = teacherService.getAll();
		
		Optional<Lesson> lesson = lessonService.findInListById(lessons, lessonId);
		Optional<Teacher> teacher = teacherService.findInListById(teachers, teacherId);
		
		lesson.ifPresent(l -> teacher.ifPresent(t -> {
			if (l.getTeacher() == null || !l.getTeacher().getId().equals(t.getId())) {
				l.setTeacher(t);
				lessonService.update(l, lessonId);
			}
		}));
		
		model.addAttribute("courses", courseService.sortListByIdFromLessToMore(courseService.getAll()));
		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teachers));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));
		model.addAttribute("rooms", roomService.sortListByIdFromLessToMore(roomService.getAll()));
		model.addAttribute("lessons", lessonService.sortListByIdFromMoreToLess(lessons));
		
		return "admin/model/lesson"; 
	}
	
	@PostMapping("/remove-teacher")
	public String removeTeacher(Model model,
			@ModelAttribute("lessonId") int lessonId,
			@ModelAttribute("teacherId") int teacherId) {
		List<Lesson> lessons = lessonService.getAll();
		
		Optional<Lesson> lesson = lessonService.findInListById(lessons, lessonId);
		
		lesson.ifPresent(l -> {
			if (l.getTeacher() != null && l.getTeacher().getId().equals(teacherId)) {
				l.setTeacher(null);
				lessonService.update(l, lessonId);
			}
		});
		
		model.addAttribute("courses", courseService.sortListByIdFromLessToMore(courseService.getAll()));
		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));
		model.addAttribute("rooms", roomService.sortListByIdFromLessToMore(roomService.getAll()));
		model.addAttribute("lessons", lessonService.sortListByIdFromMoreToLess(lessons));
		
		return "admin/model/lesson"; 
	}
	
	@PostMapping("/set-group")
	public String setGroup(Model model,
			@ModelAttribute("lessonId") int lessonId,
			@ModelAttribute("groupId") int groupId) {
		List<Lesson> lessons = lessonService.getAll();
		List<Group> groups = groupService.getAll();
		
		Optional<Lesson> lesson = lessonService.findInListById(lessons, lessonId);
		Optional<Group> group = groupService.findInListById(groups, groupId);
		
		lesson.ifPresent(l -> group.ifPresent(g -> {
			if (l.getGroup() == null || !l.getGroup().getId().equals(g.getId())) {
				l.setGroup(g);
				lessonService.update(l, lessonId);
			}
		}));
		
		model.addAttribute("courses", courseService.sortListByIdFromLessToMore(courseService.getAll()));
		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groups));
		model.addAttribute("rooms", roomService.sortListByIdFromLessToMore(roomService.getAll()));
		model.addAttribute("lessons", lessonService.sortListByIdFromMoreToLess(lessons));
		
		return "admin/model/lesson"; 
	}
	
	@PostMapping("/remove-group")
	public String removeGroup(Model model,
			@ModelAttribute("lessonId") int lessonId,
			@ModelAttribute("groupId") int groupId) {
		List<Lesson> lessons = lessonService.getAll();
		
		Optional<Lesson> lesson = lessonService.findInListById(lessons, lessonId);
		
		lesson.ifPresent(l -> {
			if (l.getGroup() != null && l.getGroup().getId().equals(groupId)) {
				l.setGroup(null);
				lessonService.update(l, lessonId);
			}
		});
		
		model.addAttribute("courses", courseService.sortListByIdFromLessToMore(courseService.getAll()));
		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));
		model.addAttribute("rooms", roomService.sortListByIdFromLessToMore(roomService.getAll()));
		model.addAttribute("lessons", lessonService.sortListByIdFromMoreToLess(lessons));
		
		return "admin/model/lesson"; 
	}
	
	@PostMapping("/set-room")
	public String setRoom(Model model,
			@ModelAttribute("lessonId") int lessonId,
			@ModelAttribute("roomId") int roomId) {
		List<Lesson> lessons = lessonService.getAll();
		List<Room> rooms = roomService.getAll();
		
		Optional<Lesson> lesson = lessonService.findInListById(lessons, lessonId);
		Optional<Room> room = roomService.findInListById(rooms, roomId);
		
		lesson.ifPresent(l -> room.ifPresent(r -> {
			if (l.getRoom() == null || !l.getRoom().getId().equals(r.getId())) {
				l.setRoom(r);
				lessonService.update(l, lessonId);
			}
		}));
		
		model.addAttribute("courses", courseService.sortListByIdFromLessToMore(courseService.getAll()));
		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));
		model.addAttribute("rooms", roomService.sortListByIdFromLessToMore(rooms));
		model.addAttribute("lessons", lessonService.sortListByIdFromMoreToLess(lessons));
		
		return "admin/model/lesson"; 
	}
	
	@PostMapping("/remove-room")
	public String removeRoom(Model model,
			@ModelAttribute("lessonId") int lessonId,
			@ModelAttribute("roomId") int roomId) {
		List<Lesson> lessons = lessonService.getAll();
		
		Optional<Lesson> lesson = lessonService.findInListById(lessons, lessonId);
		
		lesson.ifPresent(l -> {
			if (l.getRoom() != null && l.getRoom().getId().equals(roomId)) {
				l.setRoom(null);
				lessonService.update(l, lessonId);
			}
		});
		
		model.addAttribute("courses", courseService.sortListByIdFromLessToMore(courseService.getAll()));
		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));
		model.addAttribute("rooms", roomService.sortListByIdFromLessToMore(roomService.getAll()));
		model.addAttribute("lessons", lessonService.sortListByIdFromMoreToLess(lessons));
		
		return "admin/model/lesson"; 
	}
}
