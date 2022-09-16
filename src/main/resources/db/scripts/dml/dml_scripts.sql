--liquibase formatted sql

--changset nikishin:drop_column_name_at_messages
alter table messages drop column name;