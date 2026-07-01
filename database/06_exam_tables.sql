use lasyankali_erp;

create table universities (
	university_id bigint auto_increment primary key,
	univerity_code varchar(20) not null unique,
	university_name varchar(150) not null unique,
	description varchar(255),
    status enum('Active','Inavtive') default 'Active',
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp
);

alter table universities rename column univerity_code to university_code;

insert into universities ( university_code, university_name, description ) values 
('GHU' , 'Gangubai Hangal Uniersity', 'Gangubai hangal university of music and performaing Arts'),
('GMV','Gandharva Mahavidhyalaya','Gandharva Mahavidhyalaya Pune Examination');

create table exam_levels (
	level_id bigint auto_increment primary key,
    university_id bigint not null, 
    level_code varchar(20) not null,
    level_order int not null,
    description varchar(150),
    created_at timestamp default current_timestamp,
    update_at timestamp default current_timestamp on update current_timestamp,
    
    constraint fk_exam_levels_universities 
    foreign key (university_id)
    references universities(university_id), 
    
    constraint uk_university_level
    unique (university_id, level_code)
);

alter table exam_levels 
add column level_name varchar(100) not null
after level_code;

insert into exam_levels ( university_id, level_code, level_name, level_order) values 
(1, 'JR','Junior',1),
(1,'SR','Senior',2),
(1,'PV','Pre Vidwat',3),
(1,'POV','Post Vidwat',4);

insert into exam_levels ( university_id, level_code, level_name, level_order) values 
(2, 'PRA','Prarambhik',1),
(2,'PP','Praveshik Pratham',2),
(2,'PPU','Prqaveshik Puran',3),
(2,'MP','Madhyama Pratham',4),
(2,'MPU','Madhyama Puran',5),
(2,'VP','Visharad Pratham',6),
(2,'VPU','Visharad Puran',7),
(2,'AP','Alankar Pratham',8),
(2,'APU','Alankar Puran',9);

create table exam_registrations (
	registration_id bigint auto_increment primary key,
    student_id bigint not null,
    level_id bigint not null,
    exam_year year not null,
    registration_date date not null,
    registration_status enum('REGISTERED','APPROVED','REJECTED','COMPLETED') DEFAULT 'REGISTERED',
    remarks varchar(255),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,
    
    constraint fk_exam_registrations_student 
    foreign key (student_id)
    references students(student_id),
    
    constraint fk_exam_registrations_level 
    foreign key (level_id) 
    references exam_levels(level_id)
);

create table exam_results (
	result_id bigint auto_increment primary key,
    registration_id bigint not null unique,
    marks decimal(5,2),
    grade enum('A','B','C','F'),
    result_status enum('PASSED','FAILED') NOT NULL,
    result_date date,
    remarks varchar(255),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,
    
    constraint fk_exam_results_registrations 
    foreign key (registration_id) 
    references exam_registrations(registration_id)
    
);