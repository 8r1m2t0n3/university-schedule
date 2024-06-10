insert into buildings(address, university_name) values('address 1', 'university name');
insert into buildings(address, university_name) values('address 2', 'university name');

insert into departments(name, university_name, building_id) values('department name 1', 'university name', 1);
insert into departments(name, university_name, building_id) values('department name 2', 'university name', 1);
insert into departments(name, university_name, building_id) values('department name 3', 'university name', 2);

insert into specializations(name, department_id) values('specialization name 1', 1);
insert into specializations(name, department_id) values('specialization name 2', 2);
insert into specializations(name, department_id) values('specialization name 3', 2);
insert into specializations(name, department_id) values('specialization name 4', 3);

insert into groups(name, grade, specialization_id) values('group name 1', 2, 1);
insert into groups(name, grade, specialization_id) values('group name 2', 2, 1);
insert into groups(name, grade, specialization_id) values('group name 3', 1, 2);
insert into groups(name, grade, specialization_id) values('group name 4', 4, 3);
insert into groups(name, grade, specialization_id) values('group name 5', 3, 4);
insert into groups(name, grade, specialization_id) values('group name 6', 3, 4);

insert into students(group_id, date_of_birth, first_name, last_name) values(1, '2001-04-15', 'A', 'Aa');
insert into students(group_id, date_of_birth, first_name, last_name) values(1, '2001-04-15', 'B', 'Bb');
insert into students(group_id, date_of_birth, first_name, last_name) values(1, '2001-04-15', 'C', 'Cc');
insert into students(group_id, date_of_birth, first_name, last_name) values(1, '2001-04-15', 'D', 'Dd');
insert into students(group_id, date_of_birth, first_name, last_name) values(1, '2001-04-15', 'E', 'Ee');
insert into students(group_id, date_of_birth, first_name, last_name) values(2, '2001-04-15', 'F', 'Ff');
insert into students(group_id, date_of_birth, first_name, last_name) values(2, '2001-04-15', 'G', 'Gg');
insert into students(group_id, date_of_birth, first_name, last_name) values(2, '2001-04-15', 'H', 'Hh');
insert into students(group_id, date_of_birth, first_name, last_name) values(2, '2001-04-15', 'I', 'Ii');
insert into students(group_id, date_of_birth, first_name, last_name) values(2, '2001-04-15', 'J', 'Jj');
insert into students(group_id, date_of_birth, first_name, last_name) values(3, '2001-04-15', 'K', 'Kk');
insert into students(group_id, date_of_birth, first_name, last_name) values(3, '2001-04-15', 'L', 'Ll');
insert into students(group_id, date_of_birth, first_name, last_name) values(3, '2001-04-15', 'M', 'Mm');
insert into students(group_id, date_of_birth, first_name, last_name) values(6, '2001-04-15', 'N', 'Nn');
insert into students(group_id, date_of_birth, first_name, last_name) values(3, '2001-04-15', 'O', 'Oo');
insert into students(group_id, date_of_birth, first_name, last_name) values(4, '2001-04-15', 'P', 'Pp');
insert into students(group_id, date_of_birth, first_name, last_name) values(4, '2001-04-15', 'Q', 'Qq');
insert into students(group_id, date_of_birth, first_name, last_name) values(4, '2001-04-15', 'R', 'Rr');
insert into students(group_id, date_of_birth, first_name, last_name) values(4, '2001-04-15', 'S', 'Ss');
insert into students(group_id, date_of_birth, first_name, last_name) values(5, '2001-04-15', 'T', 'Tt');
insert into students(group_id, date_of_birth, first_name, last_name) values(5, '2001-04-15', 'U', 'Uu');
insert into students(group_id, date_of_birth, first_name, last_name) values(5, '2001-04-15', 'V', 'Vv');
insert into students(group_id, date_of_birth, first_name, last_name) values(5, '2001-04-15', 'W', 'Ww');
insert into students(group_id, date_of_birth, first_name, last_name) values(6, '2001-04-15', 'X', 'Xx');
insert into students(group_id, date_of_birth, first_name, last_name) values(6, '2001-04-15', 'Y', 'Yy');
insert into students(group_id, date_of_birth, first_name, last_name) values(6, '2001-04-15', 'Z', 'Zz');

insert into teachers(date_of_birth, first_name, last_name) values('2001-04-15', 'AA', 'AAa');
insert into teachers(date_of_birth, first_name, last_name) values('2001-04-15', 'BB', 'BBb');
insert into teachers(date_of_birth, first_name, last_name) values('2001-04-15', 'CC', 'CCc');
insert into teachers(date_of_birth, first_name, last_name) values('2001-04-15', 'DD', 'DDd');

insert into courses(name, description) values('math', 'some text');
insert into courses(name, description) values('bio', 'some text');
insert into courses(name, description) values('pe', 'some text');
insert into courses(name, description) values('music', 'some text');

insert into rooms(number, building_id) values(1, 1);
insert into rooms(number, building_id) values(2, 1);
insert into rooms(number, building_id) values(3, 1);
insert into rooms(number, building_id) values(4, 1);
insert into rooms(number, building_id) values(1, 2);
insert into rooms(number, building_id) values(2, 2);
insert into rooms(number, building_id) values(3, 2);
insert into rooms(number, building_id) values(4, 2);
insert into rooms(number, building_id) values(5, 2);

insert into lessons("date", start_time, end_time, teacher_id, course_id, group_id, room_id) values('2023-10-02', '8:00:00', '9:00:00', 1, 1, 1, 1);
insert into lessons("date", start_time, end_time, teacher_id, course_id, group_id, room_id) values('2023-10-02', '8:00:00', '9:00:00', 2, 2, 2, 2);
insert into lessons("date", start_time, end_time, teacher_id, course_id, group_id, room_id) values('2023-10-02', '8:00:00', '9:00:00', 3, 3, 3, 3);
insert into lessons("date", start_time, end_time, teacher_id, course_id, group_id, room_id) values('2023-10-02', '9:10:00', '9:50:00', 1, 1, 4, 4);
insert into lessons("date", start_time, end_time, teacher_id, course_id, group_id, room_id) values('2023-10-02', '9:10:00', '9:50:00', 3, 3, 5, 5);
insert into lessons("date", start_time, end_time, teacher_id, course_id, group_id, room_id) values('2023-10-02', '8:00:00', '9:00:00', 4, 4, 6, 6);

insert into courses_specializations(course_id, specialization_id) values(1, 1);
insert into courses_specializations(course_id, specialization_id) values(3, 1);
insert into courses_specializations(course_id, specialization_id) values(2, 2);
insert into courses_specializations(course_id, specialization_id) values(4, 2);
insert into courses_specializations(course_id, specialization_id) values(1, 3);
insert into courses_specializations(course_id, specialization_id) values(4, 3);
insert into courses_specializations(course_id, specialization_id) values(3, 4);
insert into courses_specializations(course_id, specialization_id) values(2, 4);

insert into courses_teachers(course_id, teacher_id) values(1, 1);
insert into courses_teachers(course_id, teacher_id) values(2, 2);
insert into courses_teachers(course_id, teacher_id) values(3, 3);
insert into courses_teachers(course_id, teacher_id) values(4, 4);

insert into teachers_departments(teacher_id, department_id) values(1, 1);
insert into teachers_departments(teacher_id, department_id) values(1, 2);
insert into teachers_departments(teacher_id, department_id) values(2, 1);
insert into teachers_departments(teacher_id, department_id) values(3, 2);
insert into teachers_departments(teacher_id, department_id) values(4, 3);
