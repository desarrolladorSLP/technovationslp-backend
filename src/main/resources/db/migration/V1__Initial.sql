create table roles
(
    name        varchar(100) PRIMARY KEY,
    description varchar(2000)
);

create table users
(
    id              uuid primary key,
    name            varchar(100)          NOT NULL,
    preferred_email varchar(200)          not null,
    phone_number    varchar(100),
    enabled         boolean default false not null,
    picture_url     varchar(1024)         not null,
    validated       boolean default false not null
);

create table users_roles
(
    user_id   uuid references users,
    role_name varchar(100) references roles,
    primary key (user_id, role_name)
);

create table firebase_users
(
    uid     varchar(200) primary key,
    user_id uuid references users not null,
    email   varchar(200)
);

create table oauth_client_details
(
    client_id               varchar(256) primary key,
    resource_ids            varchar(256),
    client_secret           varchar(256),
    scope                   varchar(256),
    authorized_grant_types  varchar(256),
    web_server_redirect_uri varchar(256),
    authorities             varchar(256),
    access_token_validity   integer,
    refresh_token_validity  integer,
    additional_information  varchar(4096),
    autoapprove             varchar(256)
);

create table programs
(
    id          uuid primary key,
    name        varchar(200) UNIQUE NOT NULL,
    description varchar(500)        not null,
    responsible varchar(2000)       not null
);

create table batches
(
    id         uuid primary key,
    program_id uuid references programs,
    name       varchar(200) NOT NULL,
    start_date date         not null,
    end_date   date         not null,
    notes      varchar(2000),

    constraint batch_name_by_program unique (program_id, name)
);

create table sessions
(
    id         uuid primary key,
    batch_id   uuid references batches,
    title      varchar(200) not null,
    notes      text         not null,
    location   varchar(500),
    date       DATE         NOT NULL,
    start_time time with time zone,
    end_time   time with time zone,

    constraint session_title unique (batch_id, title)
);

create table confirm_attendance
(
    session_id uuid references sessions,
    user_id    uuid references users,
    primary key (session_id, user_id)
);

insert into roles (name, description)
values ('ROLE_TECKER',
        'Main consumers of the programs provided by the organization, for many of the programs they are supposed to be kids'),
       ('ROLE_PARENT', 'Parents/tutors of the teckers'),
       ('ROLE_MENTOR', 'People in charge of some team of teckers'),
       ('ROLE_STAFF', 'People helping to achieve success in any of the programs'),
       ('ROLE_ADMINISTRATOR',
        'People guiding the programs. An administrator belongs to staff as well');

insert into oauth_client_details
(client_id, client_secret, scope, authorized_grant_types,
 web_server_redirect_uri, authorities, access_token_validity,
 refresh_token_validity, additional_information, autoapprove)
values ('iOSApp', '$2a$10$7rdl7jOe.LW1Db05XkPhneoAFXryHC0qGhmDdsmSbLYjMpZPTrBZ2', 'read,write',
        'firebase', null, null, 5184000, 0, null, true);

insert into oauth_client_details
(client_id, client_secret, scope, authorized_grant_types,
 web_server_redirect_uri, authorities, access_token_validity,
 refresh_token_validity, additional_information, autoapprove)
values ('AndroidApp', '$2a$10$SK3CdCpS2ui543vb4dMS4ev1F5NanWS.wxzlSFRa/huWBZkKIWwe6', 'read,write',
        'firebase', null, null, 5184000, 0, null, true);

insert into oauth_client_details
(client_id, client_secret, scope, authorized_grant_types,
 web_server_redirect_uri, authorities, access_token_validity,
 refresh_token_validity, additional_information, autoapprove)
values ('ManagementApp', '$2a$10$QyEdcDTyzndP6/3p7IrtWOF.Bg.AgzejotqLgYOjwzU0Ua5szaRDC',
        'read,write', 'firebase', null, null, 5184000, 0, null, true);

create table messages
(
    id              uuid primary key,
    user_sender_id  uuid references users,
    title           varchar(200)             not null,
    body            text                     not null,
    date_time       timestamp with time zone not null,
    high_priority   boolean                  not null,
    read            boolean                  not null,
    received        boolean                  not null,
    confirm_reading boolean                  not null
);

create table messages_by_users
(
    message_id       uuid references messages,
    user_receiver_id uuid references users,
    primary key (message_id, user_receiver_id)
);

create table resources
(
    id         uuid primary key,
    url        varchar(200) not null,
    mime_type  varchar(200) not null
);

create table users_by_batch
(
    batch_id uuid references batches,
    user_id  uuid references users,
    primary key (batch_id, user_id)
);

create table messages_resources
(
    message_id  uuid references messages,
    resource_id uuid references resources,
    primary key (message_id, resource_id)
);

create table deliverables
(
    id              uuid primary key,
    batch_id        uuid references batches,
    due_date        timestamp with time zone not null,
    title           varchar(200)             not null,
    description     text                     not null
);

create table deliverables_by_session
(
    deliverable_id uuid references deliverables,
    session_id     uuid references sessions,
    type           varchar (100)    not null,
    primary key (deliverable_id, session_id),
    constraint relation_type check (
        type = 'STARTS_ON_SESSION' OR
        type = 'COMPLETED_BEFORE_SESSION' OR
        type = 'BUILT_ON_SESSION'
    )
);

