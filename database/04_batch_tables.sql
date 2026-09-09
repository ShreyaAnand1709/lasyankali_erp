use lasyankali_erp;

create table batches (
	batch_id bigint auto_increment primary key,
    batch_code varchar(50) not null unique,
    batch_name varchar(100) not null,
    discipline enum('Bharatanatyam','Vocal','Tabla','Yoga') not null,
    level enum('Beginner','Intermediate','ADVANCED') NOT NULL,
    start_time time not null,
    end_time time not null,
    capacity int default 10,
    status enum('Active','Inactive') default 'Active',
    create_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp
);

create table batch_students (
	batch_student_id bigint primary key auto_increment, 
    batch_id bigint not null,
    student_id bigint not null,
    enrollment_date date not null,
    status enum('Active','Completed','Dropped'),
    created_at timestamp default current_timestamp,
    
    constraint fk_batch_student_batch
    foreign key (batch_id)
    references batches(batch_id),
    
    constraint fk_batch_student_student
    foreign key (student_id)
    references students(student_id),
    
    constraint uk_batch_student
    unique(batch_id, student_id)
    
);

create table batch_teacher (
	batch_teacher_id bigint auto_increment primary key, 
    batch_id bigint not null,
    teacher_id bigint not null,
    assignemd_date date not null,
    created_at timestamp default current_timestamp,
    
    constraint fk_batch_teacher_batch 
    foreign key (batch_id) 
    references batches(batch_id), 
    
    constraint fk_batch_teacher_teacher 
    foreign key (teacher_id) 
    references teachers(teacher_id),
    
    constraint uk_batch_teacher 
    unique(batch_id, teacher_id)
    
);

ALTER TABLE Batches
MODIFY discipline enum('BHARATANATYAM','VOCAL','TABALA','YOGA');

ALTER TABLE Batches
modify level enum('BEGINNER','INTERMEDIATE','ADVANCED') NOT NULL;

ALTER TABLE Batches
MODIFY status enum('ACTIVE','INACTIVE') default 'ACTIVE';

ALTER TABLE batch_students 
MODIFY status enum('ACTIVE','COMPLETED','DROPPED');

select * from batches;
select * from batch_students;
select * from batch_teacher;