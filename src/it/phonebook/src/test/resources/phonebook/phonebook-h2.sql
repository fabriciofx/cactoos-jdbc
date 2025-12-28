-- SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
-- SPDX-License-Identifier: MIT
CREATE TABLE contact (
  id UUID NOT NULL,
  name VARCHAR(50) NOT NULL,
  CONSTRAINT pk_contact PRIMARY KEY(id)
);

CREATE TABLE phone (
  contact_id UUID NOT NULL,
  number VARCHAR(15) NOT NULL,
  carrier VARCHAR(15) NOT NULL,
  CONSTRAINT fk_phone_contact FOREIGN KEY(contact_id) REFERENCES contact(id),
  CONSTRAINT pk_phone PRIMARY KEY(contact_id, number)
);

INSERT INTO contact(id, name)
  VALUES ('2D1EBC5B7D2741979CF0E84451C5BBB1', 'Joseph Klimber');
INSERT INTO contact(id, name)
  VALUES ('2A1EBC5B7D2741990CF0E84451C5BBB2', 'Maria Souza');
INSERT INTO contact(id, name)
  VALUES ('3B1EBC5B7D2741990CF0E85551C5BBB3', 'Jeff Duham');

INSERT INTO phone(contact_id, number, carrier)
  VALUES('2D1EBC5B7D2741979CF0E84451C5BBB1', '(87)99991-1234', 'TIM');
INSERT INTO phone(contact_id, number, carrier)
  VALUES('2D1EBC5B7D2741979CF0E84451C5BBB1', '(87)89234-9876', 'Oi');
INSERT INTO phone(contact_id, number, carrier)
  VALUES('2A1EBC5B7D2741990CF0E84451C5BBB2', '(83)99971-1233', 'Claro');
INSERT INTO phone(contact_id, number, carrier)
  VALUES('3B1EBC5B7D2741990CF0E85551C5BBB3', '(11)99874-9348', 'TIM');
