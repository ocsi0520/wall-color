INSERT INTO App_User(username, password, name)
VALUES (
        'jdoe',
        -- pw: user1
        '$2a$12$/wnuwqCoou1NwfDGzAPTFOsDgbyIblbOyGp.8WRvPMYt/GWSn8XYy',
        'John Doe'
    ),
    (
        'donna',
        -- pw: user2
        '$2a$12$OhrUMXgjSnPXpGkiKkzwSeYkXPDI6IqhpM6/h6blyKE6lDhPPjLNa',
        'Donna Da Maria'
    ),
    (
        'Alex',
        -- pw: user3
        '$2a$12$y1ZToAjRAP.yor.J5lY8/uHwB2w1wFJ/CxhbU1V.9cyRezcC2sS2i',
        'Alex Scrowatz'
    );

INSERT INTO Color (id, red, green, blue, name, recorded_by)
VALUES (
    1,
    136,
    147,
    152,
    'Sulyom',
    (
        SELECT id
        from App_User
        where username = 'jdoe'
    )
),
(
    2,
    107,
    192,
    179,
    'Brazil menta',
    (
        SELECT id
        from App_User
        where username = 'jdoe'
    )
),
(
    3,
    216,
    217,
    216,
    'Havasi eukaliptusz',
    (
        SELECT id
        from App_User
        where username = 'jdoe'
    )
),
(
    4,
    10,
    104,
    174,
    'Szarkaláb',
    (
        SELECT id
        from App_User
        where username = 'donna'
    )
),
(
    5,
    237,
    233,
    227,
    'Havasi gyopár',
    (
        SELECT id
        from App_User
        where username = 'donna'
    )
),
(
    6,
    142,
    205,
    233,
    'Kék szellő rózsa',
    (
        SELECT id
        from App_User
        where username = 'jdoe'
    )
),
(
    7,
    166,
    198,
    63,
    'Palástfű',
    (
        SELECT id
        from App_User
        where username = 'jdoe'
    )
);

INSERT INTO User_join_Color(app_user_id, color_id) VALUES
    ((SELECT id from App_User where username='jdoe'), (SELECT id from Color where name='Sulyom')),
    ((SELECT id from App_User where username='jdoe'), (SELECT id from Color where name='Brazil menta')),
    ((SELECT id from App_User where username='jdoe'), (SELECT id from Color where name='Havasi eukaliptusz')),
    ((SELECT id from App_User where username='jdoe'), (SELECT id from Color where name='Szarkaláb')),
    ((SELECT id from App_User where username='Alex'), (SELECT id from Color where name='Sulyom')),
    ((SELECT id from App_User where username='Alex'), (SELECT id from Color where name='Havasi eukaliptusz')),
    ((SELECT id from App_User where username='Alex'), (SELECT id from Color where name='Palástfű'));