drop table if exists beer_audit;

create table beer_audit
(
    audit_id            varchar(36) not null primary key,
    id                  varchar(36) not null,
    version             integer,
    beer_name           varchar(50),
    beer_style          smallint,
    upc                 varchar(255),
    quantity_on_hand    integer,
    price               decimal(38,2),
    created_date        datetime(6),
    update_date         datetime(6),
    created_date_audit  datetime(6) not null,
    principal_name      varchar(255),
    audit_event_type    varchar(255)
) engine=InnoDB;