# User stories

1. **`Teacher` can view own schedule flow:**
    - User can login as `Teacher` useing `login page`.
	  - User should see own `teacher schedule` according to selected date/period in `Menu`.

2. **`Student` can view own schedule flow:**
      - User can login as `Student` useing `login page`.
	    - User should see own `student schedule` according to selected date/period in `Menu`.

3. **Any authorized user can select any `Student`, `Teacher` or `Room`:**
	  - User can select any `Student`, `Teacher` or `Room` using 'Selector' in 'Menu' to remember it and not to search for it again.

4. **Any user can find any `Student`, `Teacher` or `Room` info:**
	  - User can find any `Student`, `Teacher` or `Room` info using 'Searcher' in 'Menu'.

5. **`Teacher` can cancel any `Lesson` he teach:**
	- User can login as `Teacher` using `login page`.
	  - `Teacher` can cancel any `Lesson` he teach using `lessons page`.

6. **`Teacher` can reschedule any `Lesson` he teach:**
	- User can login as `Teacher` using `login page`.
	  - `Teacher` can reschedule any `Lesson` he teach using `lessons page`.

7.	**`Teacher` can initiate new `Lesson` of `Course` he teach:**
	- User can login as `Teacher` using `login page`.
	  - `Teacher` can initiate new `Lesson` of `Course` he teach using `lessons page`.

# Features

### User administration flow

**Given User `A` logged in with Admin role**
- User `A` should be able to create/read/update/delete courses;
- User `A` should be able to assign/reassign teacher to a course;
- User `A` should be able to assign/reassign groups to a course;
- User `A` should be able to create/read/update/delete groups;
- User `A` should be able to assign/reassign student to a group;
- User `A` should be able to assign/reassign groups to a specialization;
- User `A` should be able to create/read/update/delete students;
- User `A` should be able to assign/reassign teacher to a course;
- User `A` should be able to assign/reassign teacher to a lesson;
- User `A` should be able to assign/reassign teacher to a department;
- User `A` should be able to create/read/update/delete teachers;
- User `A` should be able to create/read/update/delete lessons.

**Given User `B` logged in with Student or Teacher role**
- User `B` should be able to list all courses (read access);
- User `B` should be able to list all groups (read access);
- User `B` should be able to list all students (read access);
- User `B` should be able to list all teachers (read access);
- User `B` should be able to list all lessons of specified group/teacher (read access).

**Given User `C` logged in with Teacher role**
- User `C` should be able to create/read/update/delete lessons of courses he/she teach.
