CREATE TABLE confirm_attendance
(
    session_id  UUID REFERENCES sessions,
    user_id     UUID REFERENCES users,
    PRIMARY KEY (session_id, user_id)
);