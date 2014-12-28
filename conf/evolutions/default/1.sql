# --- !Ups
 
CREATE TABLE shortened (
    key varchar(255) NOT NULL,
    original varchar(2048) NOT NULL,
    PRIMARY KEY (key)
);
 
# --- !Downs
 
DROP TABLE shortened;