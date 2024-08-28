CREATE TABLE IF NOT EXISTS hit (
  id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  app           VARCHAR(255)                            NOT NULL,
  uri           VARCHAR(255)                            NOT NULL,
  ip            VARCHAR(50)                             NOT NULL,
  hit_timestamp TIMESTAMP                               NOT NULL,
  constraint pk_hit PRIMARY KEY (id)
);
