CREATE TABLE programs
(
  id          UUID PRIMARY KEY,
  name        varchar(200) UNIQUE  NOT NULL,
  description varchar(500)  NOT NULL,
  responsible varchar(2000) NOT NULL
);
