delete from message;

insert into message(id, text, tag, user_id) values
                                                (1, 'first', 'tag', 1),
                                                (2, 'second', 'tag2', 1),
                                                (3, 'third', 'tag', 1),
                                                (4, 'fourth', 'tag4', 1);
alter sequence message_id_seq restart with 10;