create table departments (
   id serial not null,
   name varchar (63),
   university_name varchar (255),
   building_id integer,
   primary key (id)
);