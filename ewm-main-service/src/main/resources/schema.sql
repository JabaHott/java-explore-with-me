DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;

CREATE TABLE IF NOT EXISTS users (
  id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  email         VARCHAR(255)                            NOT NULL UNIQUE,
  name          VARCHAR(255)                            NOT NULL,
  constraint pk_users PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories (
  id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name          VARCHAR(50)                             NOT NULL,
  constraint pk_categories PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS location (
  id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  lat           REAL                                    NOT NULL,
  lon           REAL                                    NOT NULL,
  CONSTRAINT pk_location PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS events (
  id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  annotation         VARCHAR(2000)                           NOT NULL,
  category_id        BIGINT                                  NOT NULL,
  confirmed_requests INT                                     NOT NULL,
  created_on         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
  description        VARCHAR(10000)                                  ,
  event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
  initiator_id       BIGINT                                  NOT NULL,
  location_id        BIGINT                                  NOT NULL,
  paid               BOOLEAN                                 NOT NULL,
  participant_limit  INT                                             ,
  published_on       TIMESTAMP WITHOUT TIME ZONE                     ,
  request_moderation BOOLEAN                                 NOT NULL,
  state              VARCHAR(50)                             NOT NULL,
  title              VARCHAR(255)                             NOT NULL,
  views              INT                                     NOT NULL,
  CONSTRAINT pk_events PRIMARY KEY(id),
  CONSTRAINT fk_events_categories FOREIGN KEY (category_id) REFERENCES categories (id),
  CONSTRAINT fk_events_location FOREIGN KEY (location_id) REFERENCES location (id),
  CONSTRAINT fk_events_users FOREIGN KEY (initiator_id) REFERENCES users (id)
);

create table if not exists requests
(
  id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  created            TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
  event_id           BIGINT                                  NOT NULL,
  requester_id       BIGINT                                  NOT NULL,
  status             VARCHAR(50)                             NOT NULL,
  CONSTRAINT pk_requests PRIMARY KEY(id),
  CONSTRAINT fk_requests_events FOREIGN KEY (event_id) REFERENCES events (id),
  CONSTRAINT fk_requests_users FOREIGN KEY (requester_id) REFERENCES users (id)
);


create table if not exists compilations
(
    id bigint generated by default as identity not null,
    title varchar(51) unique not null,
    pinned boolean not null,
    CONSTRAINT pk_compilations PRIMARY KEY(id)
);

create table if not exists compilations_events
(
    compilation_id bigint not null,
    event_id bigint not null,
    CONSTRAINT fk_compilation FOREIGN KEY (compilation_id) REFERENCES compilations(id),
    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events(id),
    CONSTRAINT pk_compilation_events PRIMARY KEY (compilation_id, event_id)
);