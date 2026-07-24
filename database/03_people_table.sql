use lasyankali_erp;

create table parents (
	parent_id Bigint auto_increment primary key, 
    user_id bigint not null,
    occupation varchar(255),
    address text,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,
    
    constraint fk_parent_user
		foreign key (user_id) 
        references users(user_id)
	);
    

create table students (
	student_id bigint auto_increment primary key,
    user_id bigint not null unique,
    parent_id bigint not null,
    addmission_number varchar(50) not null unique,
    gender ENUM('Female','Male','Other') not null,
    date_of_birth date not null, 
    joining_date date not null, 
    photo_url varchar(50), 
    status enum('Active','Inactive','Alumni') default 'active',
    create_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,
    
    constraint fk_student_user 
    foreign key (user_id) 
    references users(user_id),
    
    constraint fk_student_parent 
    foreign key (parent_id) 
    references parents(parent_id)
    );
    
CREATE TABLE teachers (
    teacher_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    employee_code VARCHAR(50) NOT NULL UNIQUE,
    specialization VARCHAR(100) NOT NULL,
    qualification VARCHAR(200),
    years_of_experience INT DEFAULT 0,
    joining_date DATE NOT NULL,
    status ENUM('ACTIVE','INACTIVE')
        DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_teacher_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);

show create table students;

select * from students; 

alter table students 
rename column addmission_number to admission_number;

ALTER TABLE students
MODIFY parent_id BIGINT NULL;