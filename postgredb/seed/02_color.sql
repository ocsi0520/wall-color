CREATE TABLE Color(
    id SERIAL PRIMARY KEY,
    red SMALLINT NOT NULL CHECK (
        red BETWEEN 0 AND 255
    ),
    green SMALLINT NOT NULL CHECK (
        green BETWEEN 0 AND 255
    ),
    blue SMALLINT NOT NULL CHECK (
        blue BETWEEN 0 AND 255
    ),
    name VARCHAR(100),
    recorded_by INTEGER REFERENCES App_User(id) ON DELETE
    SET NULL,
        CONSTRAINT unique_rgb UNIQUE (red, green, blue)
);
INSERT INTO Color (red, green, blue, name, recorded_by)
VALUES (
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