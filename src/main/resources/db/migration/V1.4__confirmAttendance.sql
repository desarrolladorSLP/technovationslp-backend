CREATE TABLE confirm_attendance
(
    session_id  UUID REFERENCES sessions,
    user_id     UUID REFERENCES users,
    PRIMARY KEY (session_id, user_id)
);

INSERT INTO confirm_attendance (session_id, user_id)
 VALUES ('51a8d9f3-8eb0-468d-9190-e783016ddbe8','c0926e7b-26fd-4789-a9ee-686109ef5c71');