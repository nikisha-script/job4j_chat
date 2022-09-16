--liquibase formatted sql

--changeset nikishin:create_users
create table if not exists users (
    id serial primary key,
    login text not null unique,
    password varchar(100) not null,
    email text
);

--changeset nikishin:create_roles
create table if not exists roles (
    id serial primary key,
    name text not null unique,
    id_user int references users(id)
);

--changeset nikishin:create_message
create table if not exists messages (
    id serial primary key,
    name text not null,
    id_user int references users(id)
);

--changeset nikishin:create_rooms
create table if not exists rooms (
    id serial primary key,
    name text not null
);

--changeset nikishin:create_users_rooms
create table if not exists users_rooms (
    id serial primary key,
    id_user int references users(id),
    id_room int references rooms(id)
);