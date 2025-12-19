INSERT INTO User_join_Color(app_user_id, color_id) VALUES
    ((SELECT id from App_User where username='jdoe'), (SELECT id from Color where name='Sulyom')),
    ((SELECT id from App_User where username='jdoe'), (SELECT id from Color where name='Brazil menta')),
    ((SELECT id from App_User where username='jdoe'), (SELECT id from Color where name='Havasi eukaliptusz')),
    ((SELECT id from App_User where username='jdoe'), (SELECT id from Color where name='Szarkaláb')),
    ((SELECT id from App_User where username='Alex'), (SELECT id from Color where name='Sulyom')),
    ((SELECT id from App_User where username='Alex'), (SELECT id from Color where name='Havasi eukaliptusz')),
    ((SELECT id from App_User where username='Alex'), (SELECT id from Color where name='Palástfű'));
