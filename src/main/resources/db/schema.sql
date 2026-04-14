DROP TABLE IF EXISTS articles;

CREATE TABLE articles (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    summary VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    content CLOB NOT NULL
);
