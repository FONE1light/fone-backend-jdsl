DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id                                     bigint NOT NULL AUTO_INCREMENT,
    job                                    varchar(100),
    interests                              varchar(50),
    nickname                               varchar(100),
    birthday                               varchar(100),
    gender                                 varchar(100),
    profile_url                            varchar(100),
    phone_number                           varchar(50),
    email                                  varchar(20),
    social_login_type                      varchar(10),
    agree_to_terms_of_service_terms_of_use tinyint(1),
    agree_to_personal_information          tinyint(1),
    is_receive_marketing                   tinyint(1),
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