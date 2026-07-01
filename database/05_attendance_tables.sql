use lasyankali_erp;

create table attendance_sessions (
	session_id bigint auto_increment primary key,
    batch_id bigint not null,
    attendance_date date not null,
    remark varchar(255),
    created_at timestamp default current_timestamp,
    
    constraint fk_attendance_sessions_batch
    foreign key (batch_id)
    references batches(batch_id)
);

create table attendance_records (
	record_id bigint auto_increment primary key,
    session_id bigint not null,
    student_id bigint not null,
    status enum('Present','Absent') not null,
    remarks varchar(255),
    created_at timestamp default current_timestamp,
    
    constraint fk_attendance_record_session
    foreign key (session_id) 
    references attendance_sessions(session_id),
    
    constraint fk_attendance_record_student 
    foreign key (student_id)
    references students(student_id)
);

desc attendance_records;