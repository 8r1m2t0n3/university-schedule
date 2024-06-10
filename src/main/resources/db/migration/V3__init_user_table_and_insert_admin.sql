create table users (
   id serial not null,
   username varchar(63),
   first_name varchar(63),
   last_name varchar(63),
   password varchar(127),
   role_note varchar(15),
   role varchar(15),
   primary key (id)
);

insert into users(username, password, role) values('admin', '$2a$12$vflVCNn.AQZjm5U5CRSkwuCHkMio3.DITNeJREYNtIKkxkN27vFMK', 'ADMIN'); -- password: 1
