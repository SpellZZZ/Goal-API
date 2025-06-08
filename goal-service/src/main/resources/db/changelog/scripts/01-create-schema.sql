create table goal_description (
    id bigint not null,
    description varchar(255),
    primary key (id)
);

create table goal_payment (
    payment_id bigint not null,
    goal_id bigint not null
);

create table goals (
    id bigint not null,
    current_money numeric(38,2),
    date_deadline timestamp(6),
    date_end timestamp(6),
    date_start timestamp(6),
    goal_name varchar(255),
    is_archived boolean not null,
    is_completed boolean not null,
    is_frozen boolean not null,
    target_money numeric(38,2),
    goal_description_id bigint,
    period_id bigint,
    priority_id bigint,
    primary key (id)
);

create table payments (
    id bigint not null,
    payment_date timestamp(6),
    payment_value numeric(38,2),
    primary key (id)
);

create table periods (
    id bigint not null,
    period_name varchar(255),
    primary key (id)
);

create table priorities (
    id bigint not null,
    priority_name varchar(255),
    primary key (id)
);

create sequence goal_description_seq start with 1 increment by 50;
create sequence goals_seq start with 1 increment by 50;
create sequence payments_seq start with 1 increment by 50;
create sequence periods_seq start with 1 increment by 50;
create sequence priorities_seq start with 1 increment by 50;

alter table if exists goals
   add constraint goal_description_goals_uq unique (goal_description_id);

alter table if exists goal_payment
   add constraint goals_payment_goal_fkey
   foreign key (goal_id)
   references goals;

alter table if exists goal_payment
   add constraint payments_payment_goal_fkey
   foreign key (payment_id)
   references payments;

alter table if exists goals
   add constraint goal_description_goals_fkey
   foreign key (goal_description_id)
   references goal_description;

alter table if exists goals
   add constraint periods_goals_fkey
   foreign key (period_id)
   references periods;

alter table if exists goals
   add constraint priorities_goals_fkey
   foreign key (priority_id)
   references priorities;