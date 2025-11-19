create table if not exists categories(
    id serial not null,
    name varchar(50) not null,
    constraint pk_category primary key(id)
);