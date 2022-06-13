  drop table if exists Host;
create table Host (
       id bigint not null auto_increment,
        createdDate datetime(6),
        lastModifiedDate datetime(6),
        ip varchar(255),
        name varchar(255),
        primary key (id)
 );