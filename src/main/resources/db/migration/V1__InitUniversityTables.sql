drop table if exists teachers_departments;
drop table if exists rooms;
drop table if exists persons;
drop table if exists students;
drop table if exists teachers;
drop table if exists lessons;
drop table if exists departments;
drop table if exists courses_teachers;
drop table if exists courses_departments;
drop table if exists courses;
drop table if exists buildings;
drop table if exists groups;
drop table if exists specializations;

create table buildings (
   id serial not null,
   address varchar (127),
   university_name varchar (255),
   primary key (id)
);

create table departments (
   id serial not null,
   name varchar (63),
   university_name varchar (255),
   building_id integer,
   primary key (id),
   foreign key (building_id) references buildings on delete cascade
);

create table specializations (
   id serial not null,
   name varchar(63),
   department_id integer,
   primary key (id),
   foreign key (department_id) references departments on delete cascade
);

create table groups (
   id serial not null,
   name varchar(63),
   grade integer,
   specialization_id integer,
   primary key (id),
   foreign key (specialization_id) references specializations on delete cascade
);

create table students (
   id serial not null,
   date_of_birth date,
   first_name varchar(63),
   last_name varchar(63),
   group_id integer,
   primary key (id),
   foreign key (group_id) references groups on delete cascade
);

create table teachers (
   id serial not null,
   date_of_birth date,
   first_name varchar (63),
   last_name varchar (63),
   primary key (id)
);

create table courses (
   id serial not null,
   name varchar (31),
   description varchar (511),
   primary key (id)
);

create table rooms (
   id serial not null,
   number integer,
   building_id integer,
   primary key (id),
   foreign key (building_id) references buildings on delete cascade
);

create table lessons (
   id serial not null,
   "date" date,
   start_time time,
   end_time time,
   teacher_id integer,
   course_id integer,
   group_id integer,
   room_id integer,
   primary key (id),
   foreign key (teacher_id) references teachers on delete cascade,
   foreign key (course_id) references courses on delete cascade,
   foreign key (group_id) references groups on delete cascade,
   foreign key (room_id) references rooms on delete cascade
);

create table courses_specializations (
   id serial not null,
   course_id integer not null,
   specialization_id integer not null,
   primary key (id),
   foreign key (specialization_id) references specializations on delete cascade,
   foreign key (course_id) references courses on delete cascade
);

create table courses_teachers (
   id serial not null,
   course_id integer not null,
   teacher_id integer not null,
   primary key (id),
   foreign key (teacher_id) references teachers on delete cascade,
   foreign key (course_id) references courses on delete cascade
);

create table teachers_departments (
   id serial not null,
   teacher_id integer not null,
   department_id integer not null,
   primary key (id),
   foreign key (department_id) references departments on delete cascade,
   foreign key (teacher_id) references teachers on delete cascade
);