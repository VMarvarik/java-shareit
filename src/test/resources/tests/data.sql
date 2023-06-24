INSERT INTO users (name, email) values ('Варвара', 'varvara@gmail.com');
INSERT INTO users (name, email) values ('Семен', 'semyon@gmail.com');
INSERT INTO users (name, email) values ('Михаил', 'michael@gmail.com');

INSERT INTO items (name, description, is_available, owner_id) VALUES
            ('Лопата', 'Лопата для сада', true, 1);
INSERT INTO items (name, description, is_available, owner_id) VALUES
            ('Трактор', 'Трактор белорусский', true, 2);

INSERT INTO item_requests (description, requestor_id, date_created) VALUES
            ('Мне нужна лопата', 2, '2023-06-20T15:39:00');
INSERT INTO item_requests (description, requestor_id, date_created) VALUES
            ('Мне нужны грабли', 3, '2023-06-20T17:40:00');

INSERT INTO items (name, description, is_available, owner_id, request_id) VALUES
            ('Грабли', 'Грабли для сада', true, 2, 1);

INSERT INTO comments (text, item_id, author_id, created) VALUES
    ('Эти грабли идеально мне подходят', 2, 3, '2023-06-21T15:15:00');