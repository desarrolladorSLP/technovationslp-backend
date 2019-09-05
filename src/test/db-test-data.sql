DO
$$
    DECLARE
        userId      uuid ARRAY[10];
        userRoles   varchar ARRAY[10] := ARRAY ['ROLE_ADMINISTRATOR', 'ROLE_MENTOR',
            'ROLE_STAFF', 'ROLE_TECKER', 'ROLE_TECKER', 'ROLE_TECKER',
            'ROLE_PARENT', 'ROLE_PARENT','ROLE_STAFF', 'ROLE_MENTOR'];
        programId   uuid ARRAY[2];
        batchId     uuid ARRAY[3];
        sessionId   uuid;
        sessionDate date;
    BEGIN

        CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

        -- INSERT users with roles
        FOR i IN 1 .. array_upper(userRoles, 1)
            LOOP
                userId[i] := uuid_generate_v4();
                INSERT INTO users (id, name, preferred_email, phone_number, enabled, picture_url,
                                   validated)
                VALUES (userId[i], 'user' || i, 'user' || i || '@mail.com',
                        substring(REPEAT('' || i, 10), 1, 10),
                        true,
                        '/fake-pictures/user' || i || '.jpg',
                        true);

                INSERT INTO users_roles (user_id, role_name) VALUES (userId[i], userRoles[i]);
            END LOOP;

        -- INSERT programs

        programId[1] := uuid_generate_v4();
        programId[2] := uuid_generate_v4();

        INSERT INTO programs (id, name, description, responsible)
        VALUES (programId[1], 'Technovation', 'Description for technovation',
                'Lía Morales, Mirna Botello');

        INSERT INTO programs (id, name, description, responsible)
        VALUES (programId[2], 'TecnoMakers', 'Description for TecnoMakers',
                'Lía Morales, Mariana Bautista');

        -- INSERT batches

        batchId[1] := uuid_generate_v4();
        batchId[2] := uuid_generate_v4();
        batchId[3] := uuid_generate_v4();

        INSERT INTO batches (id, program_id, name, start_date, end_date, notes)
        VALUES (batchId[1], programId[1], 'Technovation 2020', '2020-01-19', '2020-07-19',
                'notes for batch 1');

        INSERT INTO batches (id, program_id, name, start_date, end_date, notes)
        VALUES (batchId[2], programId[1], 'Technovation 2021', '2021-01-15', '2021-07-15',
                'notes for batch 2');

        INSERT INTO batches (id, program_id, name, start_date, end_date, notes)
        VALUES (batchId[3], programId[2], 'TechnoMakers 2020', '2021-02-20', '2021-07-20',
                'notes for batch 3');

        -- INSERT sessions
        sessionDate := '2020-01-19';
        FOR i IN 1 .. 20
            LOOP
                sessionId := uuid_generate_v4();

                INSERT INTO sessions (id, batch_id, title, notes, location, date, start_time,
                                      end_time)
                VALUES (sessionId, batchId[1], 'Session ' || i || ' - Technovation',
                        'Notes session ' || i,
                        'Somewhere ' || i, sessionDate, '15:00Z', '20:00Z');
                sessionDate := sessionDate + 7;
            END LOOP;

    END
$$;
