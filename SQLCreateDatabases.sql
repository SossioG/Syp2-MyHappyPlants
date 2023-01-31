CREATE TABLE [dbo].[species](
	id float NULL,
	scientific_name varchar(255) NULL,
	genus varchar(255) NULL,
	family varchar(255) NULL,
	common_name varchar(255) NULL,
	image_url nvarchar(255) NULL,
	light varchar(255) NULL,
	url_wikipedia_en varchar(255) NULL,
	water_frequency varchar(255) NULL
) ON [PRIMARY]
GO

create table User(
	id serial,
	username varchar(25) not null,
	email varchar(50) unique,
	password varchar(100) not null,
	notification_activated boolean,
  	fun_facts_activated boolean,
	
	primary key(id)
);

create table Plant (
    id serial,
    user_id int not null,
    nickname varchar(255),
    last_watered date not null,
    plant_id varchar(255) not null,
    image_url varchar(255) not null,
    
    primary key(user_id, nickname),
    foreign key(user_id) references  User(id)
);
