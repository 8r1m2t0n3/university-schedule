create table lessons (
   id serial not null,
   "date" date,
   end_time time,
   start_time time,
   teacher_id integer,
   course_id integer,
   group_id integer,
   room_id integer,
   primary key (id)
);