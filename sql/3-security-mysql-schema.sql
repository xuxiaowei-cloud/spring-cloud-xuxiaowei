/*
spring-security-core-*.*.*.jar!/org/springframework/security/core/userdetails/jdbc/users.ddl

https://github.com/spring-projects/spring-security/blob/main/core/src/main/resources/org/springframework/security/core/userdetails/jdbc/users.ddl
*/

use `spring_cloud_xuxiaowei`;

SET NAMES utf8mb4;

create table users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);
create table authorities (username varchar(50) not null,authority varchar(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);
