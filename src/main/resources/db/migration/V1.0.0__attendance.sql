create sequence hibernate_sequence start 1 increment 1;
create sequence sequenceGenerator start 1 increment 50;

    create table attendance (
       id int8 not null,
        authenticated boolean,
        created_at timestamp,
        embedding jsonb,
        face_identified boolean,
        identifier int8,
        device_identifier varchar(255),
        score float8,
        temperature float8,
        primary key (id)
    );

    create table configuration (
       id int8 not null,
        created_at timestamp,
        device_identifier varchar(255),
        device_name varchar(255),
        device_type varchar(255),
        enabled boolean,
        location varchar(255),
        status boolean,
        updated_at timestamp,
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
        enabled boolean,
        deletion boolean,
        created_at timestamp,
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

    alter table if exists configuration 
       add constraint UK_maesvpohv6qh8dh6kygxun3c7 unique (device_identifier);

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
