CREATE TABLE App_User(
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    -- https://stackoverflow.com/a/5882472
    name VARCHAR(70) NOT NULL -- https://stackoverflow.com/a/30509
);
INSERT INTO App_User(username, password, name)
VALUES (
        'jdoe',
        -- pw: user1
        '$2a$12$/wnuwqCoou1NwfDGzAPTFOsDgbyIblbOyGp.8WRvPMYt/GWSn8XYy',
        'John Doe'
    );
INSERT INTO App_User(username, password, name)
VALUES (
        'donna',
        -- pw: user2
        '$2a$12$OhrUMXgjSnPXpGkiKkzwSeYkXPDI6IqhpM6/h6blyKE6lDhPPjLNa',
        'Donna Da Maria'
    );
INSERT INTO App_User(username, password, name)
VALUES (
        'Alex',
        -- pw: user3
        '$2a$12$y1ZToAjRAP.yor.J5lY8/uHwB2w1wFJ/CxhbU1V.9cyRezcC2sS2i',
        'Alex Scrowatz'
    );