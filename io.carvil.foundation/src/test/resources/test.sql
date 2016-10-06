create table users(
	id int(10) not null auto_increment,
	username varchar(24) not null,
	primary key(id)
);

insert into users (id, username) values (1, 'carvill');