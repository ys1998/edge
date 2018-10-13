drop table if exists prereq cascade;
drop table if exists time_slot cascade;
drop table if exists advisor cascade;
drop table if exists takes cascade;
drop table if exists student cascade;
drop table if exists teaches cascade;
drop table if exists section cascade;
drop table if exists instructor cascade;
drop table if exists course cascade;
drop table if exists department cascade;
drop table if exists classroom cascade;

drop table if exists users cascade;
drop table if exists department cascade;
drop table if exists instructor cascade;
drop table if exists student cascade;
drop table if exists course cascade;
drop table if exists section cascade;
drop table if exists teaches cascade;
drop table if exists TA cascade;
drop table if exists takes cascade;
drop table if exists test cascade;
drop table if exists appears cascade;
drop table if exists question cascade;
drop table if exists ans cascade;


create table users
	(uid varchar(20),
	 hash varchar(256),
	 salt varchar(20),
	 primary key(uid)
	);

create table department
	(dept_name	varchar(20),
	 primary key (dept_name)
	);


create table instructor
	(uid 		varchar(20),
	 name 		varchar(20) not null,
	 dept_name 	varchar(20),
	 primary key(uid),
	 foreign key(uid) references users
		on delete cascade,
	 foreign key(dept_name) references department
		on delete set null
	);

create table student
	(rollnumber	varchar(20),
	 name		varchar(20) not null,
	 uid		varchar(20),
	 dept_name	varchar(20),
	 primary key (rollnumber),
	 foreign key (uid) references users
	 	on delete cascade,
	 foreign key (dept_name) references department
	 	on delete set null
	);

create table course
	(course_id 	varchar(20),
	 title     	varchar(20),
	 dept_name 	varchar(20),
	 primary key(course_id),
	 foreign key(dept_name) references department
		on delete set null
	);

create table section
	(course_id	varchar(20),
	 semester	varchar(20),
	 year		varchar(20),
	 primary key (course_id,semester,year),
	 foreign key (course_id) references course
	 	on delete cascade
	);

create table teaches
	(uid 		varchar(20),
	 course_id 	varchar(20),
	 semester 	varchar(20),
	 year 		varchar(20),
	 primary key(uid,course_id,semester,year)
	 foreign key(uid) references users
		on delete cascade,
	 foreign key(course_id,semester,year) references section
		on delete cascade
	 );

create table TA
	(rollnumber varchar(20),
	 course_id 	varchar(20),
	 semester 	varchar(20),
	 year 		varchar(20),
	 primary key(rollnumber,course_id,semester,year)
	 foreign key(course_id,semester,year) references section
		on delete cascade,
	 foreign key(rollnumber) references student
		on delete cascade
	);


create table takes
	(rollnumber	varchar(20),
	 course_id	varchar(20),
	 semester	varchar(20),
	 year		varchar(20),
	 primary key (rollnumber,course_id,semester,year),
	 foreign key (rollnumber) references student
	 	on delete cascade,
	 foreign key (course_id,semester,year) references section
	 	on delete cascade
	);

create table test
	(course_id	varchar(20),
	 semester	varchar(20),
	 year		varchar(20),
	 test_id	varchar(20),
	 name		varchar(20),
	 num_ques	int,
	 test_date	varchar(20),
	 primary key (course_id,semester,year,test_id),
	 foreign key (course_id,semester,year) references section
	 	on delete cascade
	);

create table appears
	(course_id	varchar(20),
	 semester	varchar(20),
	 year		varchar(20),
	 test_id	varchar(20),
	 rollnumber	varchar(20),
	 primary key (course_id,semester,year,test_id,rollnumber),
	 foreign key (course_id,semester,year,test_id) references test
	 	on delete cascade,
	 foreign key (rollnumber) references student
	 	on delete cascade
	);

create table question
	(course_id	varchar(20),
	 semester	varchar(20),
	 year		varchar(20),
	 test_id	varchar(20),
	 index		int,
	 m_marks	numeric(5,2),
	 q_text		varchar(5000),
	 space		int,
	 model_ans	varchar(10000),
	 primary key (course_id,semester,year,test_id,index),
	 foreign key (course_id,semester,year,test_id) references test
	 	on delete cascade
	);

create table ans
	(course_id	varchar(20),
	 semester	varchar(20),
	 year		varchar(20),
	 test_id	varchar(20),
	 index		int, 
	 rollnumber	varchar(20), 
	 stud_ans	bytea,
	 marks_obt	numeric(5,2), 
	 grader		varchar(20),
	 comments	varchar(1000),
	 primary key (course_id,semester,year,test_id,index,rollnumber),
	 foreign key (course_id,semester,year,test_id,index) references question
	 	on delete cascade,
	 foreign key (rollnumber) references appears
	 	on delete cascade
	);