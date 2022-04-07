DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, id, dateTime, description, calories)
VALUES (100000, 200000, '30.01.2020 10:00', 'Завтрак', 500),
       (100000, 200001, '30.01.2020 13:00', 'Обед', 1000),
       (100000, 200002, '30.01.2020 20:00', 'Ужин', 500),
       (100000, 200003, '31.01.2020 0:00', 'Еда на граничное значение', 100),
       (100000, 200004, '31.01.2020 10:00', 'Завтрак', 1000),
       (100000, 200005, '31.01.2020 13:00', 'Обед', 500),
       (100000, 200006, '31.01.2020 20:00', 'Ужин', 410);
