create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

create table address
(
    id   bigserial not null primary key,
    client_id bigint      not null references client (id),
    street varchar(255) not null
);

create table phone
(
    id   bigserial not null primary key,
    number varchar(50),
    client_id bigint      not null references client (id)
);

