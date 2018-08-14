CREATE TABLE contact (
  id BINARY(16) NOT NULL,
  name VARCHAR(50) NOT NULL,
  CONSTRAINT pk_contact_id PRIMARY KEY(id)
);

DROP TRIGGER IF EXISTS tr_contact_id;

DELIMITER $$
CREATE TRIGGER `tr_contact_id`
BEFORE INSERT ON `contact` FOR EACH ROW
BEGIN
  IF new.id IS NULL THEN
    SET new.id = UNHEX(REPLACE(UUID(), "-",""));
  END IF;
END $$
DELIMITER ;

INSERT INTO contact(name) VALUES ('Joseph Klimber');
INSERT INTO contact(name) VALUES ('Maria Souza');
INSERT INTO contact(name) VALUES ('Jeff Duham');
