CREATE TABLE contact (
  id BINARY(16) NOT NULL,
  name VARCHAR(50) NOT NULL,
  CONSTRAINT pk_contact PRIMARY KEY(id)
);

CREATE TABLE phone (
  seq INT NOT NULL AUTO_INCREMENT,
  contact BINARY(16) NOT NULL,
  number VARCHAR(10) NOT NULL,
  carrier VARCHAR(10) NOT NULL,
  CONSTRAINT fk_phone_contact FOREIGN KEY(contact) REFERENCES contact(id),
  CONSTRAINT pk_phone PRIMARY KEY(seq, contact)
);

INSERT INTO contact(id, name)
  VALUES (unhex(replace(uuid(),'-','')), 'Joseph Klimber');
INSERT INTO contact(id, name)
  VALUES (unhex(replace(uuid(),'-','')), 'Maria Souza');
INSERT INTO contact(id, name)
  VALUES (unhex(replace(uuid(),'-','')), 'Jeff Duham');
