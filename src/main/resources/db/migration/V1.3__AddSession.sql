CREATE TABLE sessions
(
  id         UUID PRIMARY KEY,
  batch_id   UUID REFERENCES batches,
  title      varchar(200) NOT NULL,
  notes      TEXT         NOT NULL,
  location   varchar(500),
  date       DATE         NOT NULL,
  start_time TIME,
  end_time   TIME,

  CONSTRAINT batch_title UNIQUE (batch_id, title)
);
