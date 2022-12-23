DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id          bigint NOT NULL AUTO_INCREMENT,
    job         varchar(100),
    interests   varchar(50),
    nickname    varchar(100),
    date        varchar(100),
    gender      varchar(100),
    profileUrl  varchar(100),
    primary key (id)
);
