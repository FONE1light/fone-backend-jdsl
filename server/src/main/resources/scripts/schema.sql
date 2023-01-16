DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id                                     bigint NOT NULL AUTO_INCREMENT,
    job                                    varchar(100),
    interests                              varchar(50),
    nickname                               varchar(100),
    birthday                               varchar(100),
    gender                                 varchar(100),
    profile_url                            varchar(300),
    phone_number                           varchar(50),
    email                                  varchar(20),
    social_login_type                      varchar(10),
    agree_to_terms_of_service_terms_of_use tinyint(1),
    agree_to_personal_information          tinyint(1),
    is_receive_marketing                   tinyint(1),
    roles                                  varchar(20),
    enabled                                tinyint(1),
    primary key (id)
);

DROP TABLE IF EXISTS questions;
CREATE TABLE questions
(
    id                            bigint NOT NULL AUTO_INCREMENT,
    email                         varchar(20),
    type                          varchar(10),
    title                         varchar(100),
    description                   varchar(500),
    agree_to_personal_information tinyint(1),
    primary key (id)
);

DROP TABLE IF EXISTS job_openings;
CREATE TABLE job_openings
(
    id                 bigint NOT NULL AUTO_INCREMENT,
    title              varchar(30),
    interests          varchar(50),
    deadline           varchar(50),
    casting            varchar(30),
    number_of_recruits int(11),
    gender             varchar(10),
    age_max            int(11),
    age_min            int(11),
    career             varchar(20),
    type               varchar(10),
    domains            varchar(200),
    user_id            bigint,
    primary key (id)
);

DROP TABLE IF EXISTS works;
CREATE TABLE works
(
    id             bigint NOT NULL AUTO_INCREMENT,
    produce        varchar(50),
    title          varchar(50),
    director       varchar(50),
    genre          varchar(20),
    logline        varchar(50),
    location       varchar(200),
    period varchar (100),
    pay            varchar(50),
    details        varchar(500),
    manager        varchar(50),
    email          varchar(20),
    job_opening_id bigint,
    primary key (id)
);