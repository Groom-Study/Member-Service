-- 테스트용 회원 데이터. 관리자 계정: admin / admin1234, 일반 사용자 계정: user1 ~ user10 / pass1234

INSERT IGNORE INTO members (username, password, name, role) VALUES
('admin',  '$2a$10$Adhii31BXgPNkFW0gn9H8.jVZ7Frc8OWKLa8/.CJcN49R334gq.eK', 'admin',  'ADMIN'),
('user1',  '$2a$10$zpn0mrCkvpeHa5WzMhZhVejmCXmJ8DV92mUchQ4KRQhlm5gdSxKNO',  'user1',  'USER'),
('user2',  '$2a$10$zpn0mrCkvpeHa5WzMhZhVejmCXmJ8DV92mUchQ4KRQhlm5gdSxKNO',  'user2',  'USER'),
('user3',  '$2a$10$zpn0mrCkvpeHa5WzMhZhVejmCXmJ8DV92mUchQ4KRQhlm5gdSxKNO',  'user3',  'USER'),
('user4',  '$2a$10$zpn0mrCkvpeHa5WzMhZhVejmCXmJ8DV92mUchQ4KRQhlm5gdSxKNO',  'user4',  'USER'),
('user5',  '$2a$10$zpn0mrCkvpeHa5WzMhZhVejmCXmJ8DV92mUchQ4KRQhlm5gdSxKNO',  'user5',  'USER'),
('user6',  '$2a$10$zpn0mrCkvpeHa5WzMhZhVejmCXmJ8DV92mUchQ4KRQhlm5gdSxKNO',  'user6',  'USER'),
('user7',  '$2a$10$zpn0mrCkvpeHa5WzMhZhVejmCXmJ8DV92mUchQ4KRQhlm5gdSxKNO',  'user7',  'USER'),
('user8',  '$2a$10$zpn0mrCkvpeHa5WzMhZhVejmCXmJ8DV92mUchQ4KRQhlm5gdSxKNO',  'user8',  'USER'),
('user9',  '$2a$10$zpn0mrCkvpeHa5WzMhZhVejmCXmJ8DV92mUchQ4KRQhlm5gdSxKNO',  'user9',  'USER'),
('user10', '$2a$10$zpn0mrCkvpeHa5WzMhZhVejmCXmJ8DV92mUchQ4KRQhlm5gdSxKNO',  'user10', 'USER');