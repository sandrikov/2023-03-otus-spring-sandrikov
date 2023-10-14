create table if not exists users
(
    user_name varchar(50)  not null primary key,
    password  varchar(500) not null,
    enabled   boolean      not null
);

create table if not exists authorities
(
    id             bigserial primary key,
    user_name      varchar(50) not null,
    authority_role varchar(50) not null,
    constraint UK_auth_username unique (user_name, authority_role),
    constraint FK_authorities_users foreign key (user_name) references users (user_name)
);

create table if not exists departments
(
    department_id   varchar(2) primary key,
    department_name varchar(50) not null,
    manager_id      bigint,
    constraint UK_department_name unique (department_name)
);

create table if not exists jobs
(
    job_id     varchar(10) primary key,
    job_title  varchar(50) not null,
    min_salary bigint,
    max_salary bigint,
    constraint UK_job_title unique (job_title)
);

create table if not exists employees
(
    employee_id   bigserial primary key,
    first_name    varchar(20) not null,
    last_name     varchar(25) not null,
    email         varchar(50) not null,
    phone_number  varchar(50),
    hire_date     date        not null,
    job_id        varchar(10) not null,
    salary        bigint,
    manager_id    bigint,
    department_id varchar(2)  not null,
    constraint emp_salary_min CHECK (salary > 0),
    constraint UK_emp_email unique (email),
    constraint FK_emp_manager foreign key (manager_id) references employees (employee_id),
    constraint FK_emp_job foreign key (job_id) references jobs (job_id),
    constraint FK_emp_dep foreign key (department_id) references departments (department_id)
);

alter table departments
    drop constraint if exists FK_dep_manager;
alter table departments
    add constraint FK_dep_manager foreign key (manager_id) references employees (employee_id);

create table if not exists job_history
(
    job_history_id bigserial primary key,
    employee_id    bigint      not null,
    start_date     date        not null,
    end_date       date        not null,
    job_id         varchar(10) not null,
    department_id  varchar(2)  /*not null*/,
    constraint UK_jhist_email unique (employee_id, start_date),
    constraint FK_jhist_empl foreign key (employee_id) references employees (employee_id),
    constraint FK_jhist_job foreign key (job_id) references jobs (job_id),
    constraint FK_jhist_dep foreign key (department_id) references departments (department_id),
    constraint jhist_date_interval check (end_date > start_date)
);
