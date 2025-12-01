INSERT INTO member (email, first_name, last_name) VALUES
    ('alice@example.com', 'Alice', 'Tanaka'),
    ('bob@example.com', 'Bob',   'McArthur'),
    ('carol@example.com', 'Carol', 'Singh');

INSERT INTO member_fitness (member_id, entry_num, heart_rate, gender, weight_val, body_fat, goal) VALUES
    (1, 1, 72, 'Female', 135, 22, 'Improve endurance'),
    (2, 1, 80, 'Male',   180, 18, 'Lose weight'),
    (3, 1, 68, 'Female', 150, 20, 'Gain muscle');

INSERT INTO trainer (first_name, last_name) VALUES
    ('Daniel', 'Rousseau'),
    ('Emily',  'Park'),
    ('Frank',  'Hernandez');

INSERT INTO availability_window (trainer_id, win_date, start_time, end_time) VALUES
    (1, '2025-12-01', '09:00', '11:00'),
    (2, '2025-12-02', '10:00', '12:00'),
    (3, '2025-12-03', '13:00', '15:00');

INSERT INTO admin DEFAULT VALUES;
INSERT INTO admin DEFAULT VALUES;
INSERT INTO admin DEFAULT VALUES;

INSERT INTO room (room_id) VALUES
    (101),
    (102),
    (103);

INSERT INTO session (trainer_id, member_id, room_id, sess_date, start_time, end_time) VALUES
    (1, 1, 101, '2025-12-01', '09:00', '10:00'),
    (2, 2, 102, '2025-12-02', '10:30', '11:30'),
    (3, 3, 103, '2025-12-03', '13:30', '14:30');

INSERT INTO equipment_logs (descript, stat, equipment) VALUES
    ('Treadmill belt slipping', 'Open', 'Treadmill'),
    ('Loose handle on machine', 'Closed', 'Rowing'),
    ('Elliptical squeaking', 'Open', 'Elliptical');
