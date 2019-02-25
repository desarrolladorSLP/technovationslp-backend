CREATE TABLE roles
(
  name        varchar(100) PRIMARY KEY,
  description varchar(2000)
);

CREATE TABLE users
(
  id              UUID PRIMARY KEY,
  name            varchar(100)          NOT NULL,
  preferred_email varchar(200)          NOT NULL,
  phone_number    varchar(100),
  enabled         boolean DEFAULT false NOT NULL,
  validated       boolean DEFAULT false NOT NULL
);

CREATE TABLE users_roles
(
  user_id   UUID REFERENCES users,
  role_name varchar(100) REFERENCES roles,
  PRIMARY KEY (user_id, role_name)
);

CREATE TABLE firebase_users
(
  uid     varchar(200) PRIMARY KEY,
  user_id UUID REFERENCES users NOT NULL,
  email   varchar(200) UNIQUE
);

INSERT INTO roles (name, description)
VALUES ('ROLE_TECKER',
        'Main consumers of the programs provided by the organization, for many of the programs they are supposed to be kids'),
       ('ROLE_PARENT', 'Parents/tutors of the teckers'),
       ('ROLE_MENTOR', 'People in charge of some team of teckers'),
       ('ROLE_STAFF', 'People helping to achieve success in any of the programs'),
       ('ROLE_ADMINISTRATOR',
        'People guiding the programs. An administrator belongs to staff as well');
