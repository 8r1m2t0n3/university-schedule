create table groups (
   id serial not null,
   name varchar(63),
   grade integer,
   specialization_id integer,
   primary key (id)
);