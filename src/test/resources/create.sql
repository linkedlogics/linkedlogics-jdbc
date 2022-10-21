CREATE TABLE ll_queue (
    id INTEGER NOT NULL AUTO_INCREMENT,
    queue VARCHAR(128) NOT NULL,
    payload VARCHAR(4000) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    consumed_by VARCHAR(128),
    PRIMARY KEY (id)
);

CREATE TABLE ll_topic (
    id INTEGER NOT NULL AUTO_INCREMENT,
    queue VARCHAR(128) NOT NULL,
    payload VARCHAR(4000) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    consumed_by VARCHAR(128),
    PRIMARY KEY (id)
);

CREATE TABLE ll_topic_consumed (
    id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    consumed_by VARCHAR(128) NOT NULL,
    consumed_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id, consumed_by)
);
