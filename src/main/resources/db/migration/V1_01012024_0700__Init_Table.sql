CREATE TABLE authors
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250) NOT NULL
);

CREATE TABLE tags
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250) NOT NULL
);

CREATE TABLE posts
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    title     VARCHAR(250) NOT NULL,
    body      VARCHAR(250) NOT NULL,
    author_id INT,
    FOREIGN KEY (author_id) REFERENCES authors (id)
);

CREATE TABLE post_tags
(
    post_id INT,
    tag_id  INT,
    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (tag_id) REFERENCES tags (id)
);

INSERT INTO authors (name)
VALUES ('Naruto'),
       ('Sasuke');

INSERT INTO tags (name)
VALUES ('Technology'),
       ('Social');

INSERT INTO posts (title, body, author_id)
VALUES ('My First Blog', 'bla bla bla', 1),
       ('Opening Party!', 'bla bla bla', 2);

INSERT INTO post_tags (post_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (2, 1);
