CREATE TABLE contact (
  id UUID DEFAULT RANDOM_UUID(),
  name VARCHAR(50) NOT NULL,
  CONSTRAINT pk_contact PRIMARY KEY(id)
);

CREATE TABLE phone (
  contact UUID NOT NULL,
  seq INT AUTO_INCREMENT,
  number VARCHAR(10) NOT NULL,
  carrier VARCHAR(10) NOT NULL,
  CONSTRAINT fk_phone_contact FOREIGN KEY(contact) REFERENCES contact(id),
  CONSTRAINT pk_phone PRIMARY KEY(seq, contact)
);

INSERT INTO contact(name) VALUES ('Joseph Klimber');
INSERT INTO contact(name) VALUES ('Maria Souza');
INSERT INTO contact(name) VALUES ('Jeff Duham');
