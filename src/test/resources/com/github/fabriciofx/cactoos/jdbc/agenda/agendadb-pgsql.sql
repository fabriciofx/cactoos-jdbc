CREATE TABLE contact (
  id UUID DEFAULT gen_random_uuid(),
  name VARCHAR(50) NOT NULL,
  CONSTRAINT pk_contact_id PRIMARY KEY(id)
);

CREATE TABLE phone (
  seq SERIAL,
  contact UUID NOT NULL,
  number VARCHAR(10) NOT NULL,
  operator VARCHAR(10) NOT NULL,
  CONSTRAINT fk_phone_contact FOREIGN KEY(contact) REFERENCES contact(id),
  CONSTRAINT pk_phone_contact_seq PRIMARY KEY(seq, contact)
);

INSERT INTO contact(name) VALUES ('Joseph Klimber');
INSERT INTO contact(name) VALUES ('Maria Souza');
INSERT INTO contact(name) VALUES ('Jeff Duham');
