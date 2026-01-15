CREATE TABLE App_User(
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    name VARCHAR(70) NOT NULL,
    is_admin boolean NOT NULL
);

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

CREATE TABLE User_join_Color(
    app_user_id INTEGER NOT NULL REFERENCES App_User(id) ON DELETE CASCADE,
    color_id INTEGER NOT NULL REFERENCES Color(id) ON DELETE CASCADE,
    CONSTRAINT unique_user_color UNIQUE (app_user_id, color_id)
);