create sequence hibernate_sequence start 1 increment 1;
create sequence sequenceGenerator start 1 increment 50;

    create table attendance (
       id int8 not null,
        authenticated boolean,
        created_at timestamp,
        embedding jsonb,
        face_detected boolean,
        identifier varchar(255),
        mac_address varchar(255),
        temperature float8,
        primary key (id)
    );

    create table embeddings (
       id int8 not null,
        embedding jsonb,
        employee_id int8,
        primary key (id)
    );

    create table employee (
       id int8 not null,
        encryption_key varchar(255),
        identifier varchar(255),
        name varchar(255),
        primary key (id)
    );

    create table file (
       id int8 not null,
        encryption boolean,
        encryption_algorithm varchar(255),
        identifier varchar(255),
        location varchar(255),
        uri varchar(255),
        employee_id int8,
        primary key (id)
    );

    create table Person (
       id int8 not null,
        birth timestamp,
        city varchar(255),
        country varchar(255),
        name varchar(255),
        status int4,
        primary key (id)
    );

    alter table if exists employee 
       add constraint UK_5rh0wqupnwpehb050wgokpc4n unique (identifier);

    alter table if exists embeddings 
       add constraint FK1q9f5dh50n84cwdjb8vin1ioy 
       foreign key (employee_id) 
       references employee;

    alter table if exists file 
       add constraint FKoli2rgge1fomxfk7vwr9cyy96 
       foreign key (employee_id) 
       references employee;
