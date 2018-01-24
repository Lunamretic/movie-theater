DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS tickets;

CREATE TABLE users (
  id INT GENERATED ALWAYS AS IDENTITY CONSTRAINT pk_user PRIMARY KEY,
  firstName VARCHAR(100) NOT NULL,
  lastName VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL
);

CREATE TABLE events (
  id INT GENERATED ALWAYS AS IDENTITY CONSTRAINT pk_event PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  basePrice INT NOT NULL,
  rating VARCHAR(100) NOT NULL
);

CREATE TABLE tickets (
  id INT GENERATED ALWAYS AS IDENTITY CONSTRAINT pk_ticket PRIMARY KEY,
  user_id INT CONSTRAINT fk_ticket_user
  REFERENCES users ON UPDATE RESTRICT ON DELETE RESTRICT,
  event_id INT CONSTRAINT fk_ticket_event
  REFERENCES events ON UPDATE RESTRICT ON DELETE RESTRICT,
  dateTime DATETIME NOT NULL,
  seat INT NOT NULL
);