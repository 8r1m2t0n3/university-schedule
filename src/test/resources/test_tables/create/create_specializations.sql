create table specializations (
   id serial not null,
   name varchar(63),
   department_id integer,
   primary key (id)
);