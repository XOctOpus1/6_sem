create table client
(
    id       serial PRIMARY KEY,
    name     character varying NOT NULL,
    surname  character varying NOT NULL,
    login    character varying NOT NULL,
    password character varying NOT NULL,
    isAdmin  integer           NOT NULL
);

insert into client(name, surname, login, password, isAdmin)
values ('Denys', 'Gordiichuk', 'XOctopus', '123123', 1),
       ('den', 'gor', '12', '12', 0);

insert into client(name, surname, login, password, isAdmin)
values ('d', 'f', '13', '13', 0);


create table car
(
    id           serial PRIMARY KEY,
    name         character varying NOT NULL,
    availabel    bool              NOT NULL default true,
    cost_per_day integer                    default 100
);

insert into car(name)
values ('seat'),
       ('sdfd'),
       ('sdf');

create table book
(
    client_id            integer NOT NULL,
    car_id               integer NOT NULL,
    rental_period_in_day integer NOT NULL,
    cost                 integer NOT NULL,
    allow                bool    NOT NULL default true,
    paid                 bool    NOT NULL default false,
    cause                character varying,
    FOREIGN KEY (client_id) REFERENCES client (id),
    FOREIGN KEY (car_id) REFERENCES car (id)
);

drop table report
create table report
(
    client_id    integer NOT NULL,
    car_id       integer NOT NULL,
    has_injuries bool    NOT NULL,
    message      character varying,
    cost         integer,
    FOREIGN KEY (client_id) REFERENCES client (id),
    FOREIGN KEY (car_id) REFERENCES car (id)
);
