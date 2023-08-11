CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(255)                            NOT NULL,
    CONSTRAINT PK_USER PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS requests
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY           NOT NULL,
    description      VARCHAR(1000)                                     NOT NULL,
    created          TIMESTAMP WITHOUT TIME ZONE                       NOT NULL,
    request_maker_id BIGINT                                            NOT NULL,
    CONSTRAINT PK_REQUESTS PRIMARY KEY (id),
    CONSTRAINT FK_REQUESTS_ON_USER FOREIGN KEY (request_maker_id) REFERENCES users (id) ON DELETE CASCADE
                                                                                        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS items
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name         VARCHAR(255)                            NOT NULL,
    description  VARCHAR(1024)                           NOT NULL,
    is_available BOOLEAN                                 NOT NULL DEFAULT FALSE,
    owner_id     BIGINT                                  NOT NULL,
    request_id   BIGINT,
    CONSTRAINT PK_ITEM PRIMARY KEY (id),
    CONSTRAINT FK_ITEM_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (id)                ON DELETE CASCADE
                                                                                            ON UPDATE CASCADE,
    CONSTRAINT FK_ITEM_ON_REQUEST FOREIGN KEY (request_id) REFERENCES requests (id) ON DELETE CASCADE
                                                                                            ON UPDATE CASCADE,
    CONSTRAINT UQ_OWNER_ITEM_NAME UNIQUE(owner_id, name)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_booking TIMESTAMP WITHOUT TIME ZONE          NOT NULL,
    end_booking   TIMESTAMP WITHOUT TIME ZONE          NOT NULL,
    item_id    BIGINT                                  NOT NULL,
    booker_id  BIGINT                                  NOT NULL,
    status     VARCHAR(8)                              NOT NULL,
    CONSTRAINT PK_BOOKING PRIMARY KEY (id),
    CONSTRAINT FK_BOOKING_ON_BOOKER FOREIGN KEY (booker_id) REFERENCES users (id)    ON DELETE CASCADE
                                                                                     ON UPDATE CASCADE,
    CONSTRAINT FK_BOOKING_ON_ITEM FOREIGN KEY (item_id) REFERENCES items (id)        ON DELETE CASCADE
                                                                                     ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text      VARCHAR(1024)                           NOT NULL,
    created   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    item_id   BIGINT                                  NOT NULL,
    author_id BIGINT                                  NOT NULL,
    CONSTRAINT PK_COMMENT PRIMARY KEY (id),
    CONSTRAINT FK_COMMENT_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES users (id)    ON DELETE CASCADE
                                                                                     ON UPDATE CASCADE,
    CONSTRAINT FK_COMMENT_ON_ITEM FOREIGN KEY (item_id) REFERENCES items (id)        ON DELETE CASCADE
                                                                                     ON UPDATE CASCADE
    );