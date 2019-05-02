CREATE TABLE contact (
  id BINARY(16) NOT NULL,
  name VARCHAR(50) NOT NULL,
  CONSTRAINT pk_contact PRIMARY KEY(id)
);

CREATE TABLE phone (
  contact_id BINARY(16) NOT NULL,
  number VARCHAR(10) NOT NULL,
  carrier VARCHAR(10) NOT NULL,
  CONSTRAINT fk_phone_contact FOREIGN KEY(contact_id) REFERENCES contact(id),
  CONSTRAINT pk_phone PRIMARY KEY(contact_id, number)
);

INSERT INTO contact(id, name)
  VALUES (unhex(replace(uuid(),'-','')), 'Joseph Klimber');
INSERT INTO contact(id, name)
  VALUES (unhex(replace(uuid(),'-','')), 'Maria Souza');
INSERT INTO contact(id, name)
  VALUES (unhex(replace(uuid(),'-','')), 'Jeff Duham');
