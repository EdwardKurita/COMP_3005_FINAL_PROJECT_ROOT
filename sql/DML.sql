-- ===== member =====
INSERT INTO member (email, member_id, first_name, last_name) VALUES
('alice@example.com', 1, 'Alice', 'Tanaka'),
('bob@example.com',   2, 'Bob',   'McArthur'),
('carol@example.com', 3, 'Carol', 'Singh');

-- ===== member_fitness =====
INSERT INTO member_fitness (member_id, entry_num, heart_rate, gender, weight_val, body_fat, goal) VALUES
(1, 1, 72, 'Female', 135, 22, 'Improve endurance'),
(2, 1, 80, 'Male',   180, 18, 'Lose weight'),
(3, 1, 68, 'Female', 150, 20, 'Gain muscle');

-- ===== trainer =====
INSERT INTO trainer (trainer_id, first_name, last_name) VALUES
(1, 'Daniel', 'Rousseau'),
(2, 'Emily',  'Park'),
(3, 'Frank',  'Hernandez');

-- ===== availability_window =====
INSERT INTO availability_window (trainer_id, win_date, start_time, end_time) VALUES
(1, '2025-12-01', '09:00', '11:00'),
(2, '2025-12-02', '10:00', '12:00'),
(3, '2025-12-03', '13:00', '15:00');

-- ===== admin =====
INSERT INTO admin (admin_id) VALUES
(1),
(2),
(3);

-- ===== room =====
INSERT INTO room (room_id) VALUES
(101),
(102),
(103);

-- ===== session =====
-- (These match trainer availability windows before the trigger removes them)
INSERT INTO session (session_id, trainer_id, member_id, room_id, sess_date, start_time, end_time) VALUES
(1, 1, 1, 101, '2025-12-01', '09:00', '10:00'),
(2, 2, 2, 102, '2025-12-02', '10:30', '11:30'),
(3, 3, 3, 103, '2025-12-03', '13:30', '14:30');

-- ===== equipment_logs =====
INSERT INTO equipment_logs (issue_id, descript, stat, equipment) VALUES
(1, 'Treadmill belt slipping', 'Open', 'Treadmill'),
(2, 'Loose handle on machine', 'Closed', 'Rowing'),
(3, 'Elliptical squeaking', 'Open', 'Elliptical');
