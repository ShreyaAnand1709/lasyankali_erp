Use lasyankali_erp;

create table roles (
	role_id BigInt auto_increment primary key,
    role_name varchar(25) not null unique,
    description varchar(25),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp
    );
    
    insert into roles(role_name , description) values
    ('ADMIN','System Administration'),
    ('TEACHER','Dance Teacher'),
    ('STUDENT','Student User'),
    ('PARENT','Parent User');
    
    select * from roles;
    
    #user table 
    
    create table users (
		user_id bigint auto_increment primary key,
        user_name varchar(100) not null unique, 
        email varchar(150) not null unique, 
        password_hash varchar(255) not null,
        mobile_number varchar(20),
        role_id bigint not null,
        is_active boolean default true,
        last_login timestamp null,
		created_at timestamp default current_timestamp,
        updated_at timestamp default current_timestamp on update current_timestamp, 
        
        constraint fk_user_role foreign key (role_id) references roles(role_id)
        );
        
	insert into users(user_name, email, password_hash, mobile_number, role_id) values (
    'yaadvi', 'yaadvi@gmail.com','yaadvi@123','9900216065',3); 
    
    select * from users;
    
    alter table users
    add column first_name varchar(25) 
    after mobile_number;
    
    alter table users
    add column last_name varchar(25)
    after first_name;
    
    desc users
    
    