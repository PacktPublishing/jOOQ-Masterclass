DROP TABLE IF EXISTS rsvp_document;

CREATE TABLE rsvp_document (
  id bigint NOT NULL,  
  rsvp varchar(4000) NOT NULL,  
  status varchar(25) DEFAULT NULL,  
  CONSTRAINT id_pk PRIMARY KEY (id)
);