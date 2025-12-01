CREATE TABLE member (
    email VARCHAR(50),
    member_id serial,
    first_name VARCHAR(25) NOT NULL,
    last_name VARCHAR(25) NOT NULL,
    PRIMARY KEY (member_id)
);

CREATE TABLE member_fitness (
    member_id INT NOT NULL,
    entry_num INT NOT NULL,
    heart_rate INT,
    gender VARCHAR(15),
    weight_val INT,
    body_fat INT,
    goal VARCHAR(255),
    PRIMARY KEY (member_id, entry_num),
    FOREIGN KEY (member_id) REFERENCES member (member_id)
);

CREATE TABLE trainer (
    trainer_id serial,
    first_name VARCHAR(25),
    last_name VARCHAR(25),
    PRIMARY KEY (trainer_id)
);

CREATE TABLE availability_window (
    trainer_id INT,
    win_date DATE,
    start_time TIME,
    end_time TIME NOT NULL,
    PRIMARY KEY (trainer_id, win_date, start_time),
    FOREIGN KEY (trainer_id) REFERENCES trainer (trainer_id)
);

CREATE TABLE admin (
    admin_id serial,
    PRIMARY KEY (admin_id)
);

CREATE TABLE room (
    room_id INT,
    PRIMARY KEY (room_id)
);

CREATE TABLE session (
    session_id serial,
    trainer_id INT NOT NULL,
    member_id INT NOT NULL,
    room_id INT,
    sess_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    PRIMARY KEY (session_id),
    FOREIGN KEY (trainer_id) REFERENCES trainer (trainer_id),
    FOREIGN KEY (member_id) REFERENCES member (member_id),
    FOREIGN KEY (room_id) REFERENCES room (room_id)
);

CREATE TABLE equipment_logs (
    issue_id serial,
    descript VARCHAR(255) NOT NULL,
    stat VARCHAR(15) NOT NULL,
    equipment VARCHAR(15) NOT NULL,
    PRIMARY KEY (issue_id)
);

-- This view is useful for accessing the member dashboard quickly
CREATE VIEW member_fitness_dashboard AS
    SELECT 
        m.member_id,
        m.first_name,
        m.last_name,
        mf.entry_num,
        mf.heart_rate,
        mf.gender,
        mf.weight_val,
        mf.body_fat,
        mf.goal
    FROM member m
    JOIN member_fitness mf 
    ON m.member_id = mf.member_id;

-- Useful for scheduling PT sessions for members
CREATE VIEW trainer_availability AS
    SELECT 
        t.trainer_id,
        t.first_name,
        t.last_name,
        a.win_date,
        a.start_time,
        a.end_time
    FROM trainer t
    JOIN availability_window a 
    ON t.trainer_id = a.trainer_id;

-- these two indexes are useful for viewing the member and trainer info quckly
CREATE INDEX idx_member_name 
    ON member (last_name, first_name);

CREATE INDEX idx_trainer_name 
    ON trainer (last_name, first_name);

-- This trigger is use when a new session gets scheduled for a trainer, that time slot in the trainers availability gets removed.
CREATE OR REPLACE FUNCTION remove_used_availability()
RETURNS TRIGGER 
LANGUAGE plpgsql
AS 
$$
BEGIN
    DELETE FROM availability_window
    WHERE trainer_id = NEW.trainer_id
      AND win_date = NEW.sess_date
      AND start_time <= NEW.start_time
      AND end_time >= NEW.end_time;

    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_remove_availability
    AFTER INSERT ON session
    FOR EACH ROW
    EXECUTE PROCEDURE remove_used_availability();
