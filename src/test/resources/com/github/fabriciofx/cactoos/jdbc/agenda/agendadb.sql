CREATE TABLE contact (
  id UUID DEFAULT RANDOM_UUID(),
  name VARCHAR(50) NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE phone (
  contact UUID NOT NULL,
  seq INT AUTO_INCREMENT,
  number VARCHAR(10) NOT NULL,
  carrier VARCHAR(10) NOT NULL,
  FOREIGN KEY(contact) REFERENCES contact(id),
  PRIMARY KEY(contact, seq)
);

INSERT INTO contact(name) VALUES ('Joseph Klimber');
INSERT INTO contact(name) VALUES ('Maria Souza');
INSERT INTO contact(name) VALUES ('Jeff Duham');
