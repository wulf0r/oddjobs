create table hello_message (
    id bigint generated always as identity primary key,
    message varchar(255) not null
);

insert into hello_message (message)
values ('Hello World from database');
