-- TODO: Delete
create table hello_message
(
    id      bigint generated always as identity primary key,
    message varchar(255) not null
);

insert into hello_message (message)
values ('Hello World from database');

-- placeholder table for users when we add auth
create table users (   -- rare inserts, so uuid should be fine for now. This way I can easily have a dummy user
    id           uuid primary key,
    display_name varchar(50) not null
);

insert into users (id, display_name)
values ('00000000-0000-0000-0000-000000000001', 'Test User');


-- file type table defines which file types can be used for the odd job and which can be used for output
-- the addition_x_prompt fields will be appended to the prompt in case that file type is used
create table filetype
(
    id                       bigint generated always as identity primary key,
    name                     varchar(50) not null, -- displayable name for the filetype like "JPEG Image"
    ext                      char(3)     not null, -- file extension like jpg
    mimetype                 varchar(50) not null,
    input                    boolean     not null, -- can be used for input?
    output                   boolean     not null, -- cna be used for output?

    additional_input_prompt  varchar,
    additional_output_prompt varchar
);

-- for an mvp we store this in the database, later we can replace this with a filename / url scheme
create table file
(
    id          bigint generated always as identity primary key,
    filetype_id bigint              not null references filetype (id) on delete cascade on update cascade,
    size        bigint              not null,
    content     bytea not null
);

-- odd job describe the 'job' to give to the AI
create table oddjob
(
    id           bigint generated always as identity primary key,
    user_id      uuid references users (id) on delete cascade on update cascade,

    display_name varchar(50)   not null,
    -- this is the user prompt to the AI that defines what the AI is supposed to do
    prompt       varchar(1000) not null
);

create table oddjob_input
(
    id           bigint generated always as identity primary key,
    oddjob_id    bigint references oddjob (id) on delete cascade on update cascade,
    filetype_id  bigint references filetype (id) on delete restrict on update cascade,
    display_name varchar(50),
    position     int not null, -- the input position defines the order in which the input files will be provided to the AI
    prompt       varchar(1000) not null  -- prompt fragment specifically describing this input to the AI
);


create table oddjob_output
(
    id           bigint generated always as identity primary key,
    oddjob_id    bigint references oddjob (id) on delete cascade on update cascade,
    filetype_id  bigint references filetype (id) on delete restrict on update cascade,
    display_name varchar(50),
    position     int not null, -- the output positions define the order in which we ask the AI to provide the outputs
    prompt       varchar(1000) not null  -- prompt fragment specifically describing this output to the AI
);

-- when an oddjob runs
create table oddjob_execution
(
    id          bigint generated always as identity primary key,
    oddjob_id   bigint not null references oddjob (id),
    begin       timestamp with time zone,
    "end"         timestamp with time zone,
    full_prompt varchar -- the complete prompt (input prompts + job prompt + output prompts) given to the AI
);

-- the actual input to for a given oddjob execution
create table oddjob_execution_input
(
    id                  bigint generated always as identity primary key,
    oddjob_execution_id bigint not null references oddjob_execution (id),
    position            int    not null, -- this is the order in which the file type was given to the AI.
    file_id             bigint references file (id) on delete restrict on update cascade
);

-- the actual output for the giver oddjob execution
-- this might be too normalized
create table oddjob_execution_output
(
    id bigint generated always as identity primary key,
    oddjob_execution_id bigint not null references oddjob_execution (id),
    position int not null,
    file_id  bigint references file(id) on delete restrict on update cascade
);
