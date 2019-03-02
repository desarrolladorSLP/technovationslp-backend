CREATE TABLE batches
(
  id         UUID PRIMARY KEY,
  program_id UUID REFERENCES programs,
  name       varchar(200) UNIQUE NOT NULL,
  start_date DATE                NOT NULL,
  end_date   DATE                NOT NULL,
  notes      varchar(2000)
);
