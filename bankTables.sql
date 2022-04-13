-- type tables
create table user_role_type (
	user_role_type_id serial primary key,
	user_role_type_name varchar(255)
)
;
insert into user_role_type (user_role_type_name)
values ('ADMIN')
;
insert into user_role_type (user_role_type_name)
values ('USER')
;

create table account_type (
	account_type_id serial primary key,
	account_type_name varchar(255)
)
;
insert into account_type (account_type_name)
values ('CHECKING')
;
insert into account_type (account_type_name)
values ('SAVINGS')
;

create table transaction_type (
	transaction_type_id serial primary key,
	transaction_type_name varchar(255)
)
;
insert into transaction_type (transaction_type_name)
values ('WITHDRAWAL')
;
insert into transaction_type (transaction_type_name)
values ('DEPOSIT')
;


-- main data tables
create table bank_user (
	bank_user_id serial primary key,
	user_role_type_id INT references user_role_type (user_role_type_id) not null,
	first_name varchar(255),
	last_name varchar(255),
	username varchar(255) not null,
	password_hash varchar(255) not null,
	create_date TIMESTAMP not null,
	last_login_date TIMESTAMP
)
;

create table account (
	account_id serial primary key,
	account_type_id INT references account_type (account_type_id) not null,
	bank_user_id INT references bank_user (bank_user_id) not null,
	balance double precision not null,
	create_date TIMESTAMP not null
)
;

create table account_transaction (
	account_transaction_id serial primary key,
	transaction_type_id INT references transaction_type (transaction_type_id) not null,
	account_id INT references account (account_id) not null,
	amount double precision not null,
	account_transaction_date TIMESTAMP not null
)
;