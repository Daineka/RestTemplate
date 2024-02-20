-- Удаление таблиц из базы данных
DROP TABLE IF EXISTS authors, books, genres, bookgenres CASCADE;

-- Создание таблицы авторов
CREATE TABLE authors (
                         author_id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL
);

-- Создание таблицы жанров
CREATE TABLE genres (
                        genre_id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL
);

-- Создание таблицы книг
CREATE TABLE books (
                       book_id SERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       published_year INTEGER,
                       author_id INTEGER REFERENCES authors(author_id) ON DELETE CASCADE
);

-- Создание таблицы для связи многие ко многим между книгами и жанрами
CREATE TABLE bookgenres (
                            book_id INTEGER REFERENCES books(book_id) ON DELETE CASCADE,
                            genre_id INTEGER REFERENCES genres(genre_id) ON DELETE CASCADE,
                            PRIMARY KEY (book_id, genre_id)
);

-- Добавление авторов
INSERT INTO authors (name) VALUES
                               ('Александр Пушкин'),
                               ('Лев Толстой'),
                               ('Фёдор Достоевский'),
                               ('Анна Ахматова'),
                               ('Иван Бунин');

-- Добавление жанров
INSERT INTO genres (name) VALUES
                              ('Роман'),
                              ('Поэзия'),
                              ('Фантастика'),
                              ('Детектив'),
                              ('Драма');

-- Добавление книг
INSERT INTO books (title, published_year, author_id) VALUES
                                                         ('Евгений Онегин', 1833, 1),
                                                         ('Анна Каренина', 1877, 2),
                                                         ('Преступление и наказание', 1866, 3),
                                                         ('Белая гвардия', 1923, 4),
                                                         ('Темные аллеи', 1949, 5),
                                                         ('Война и мир', 1869, 2),
                                                         ('Братья Карамазовы', 1880, 3),
                                                         ('Идиот', 1869, 3),
                                                         ('Руслан и Людмила', 1820, 1),
                                                         ('Собачье сердце', 1925, 3);

-- Добавление связей между книгами и жанрами
-- Допустим, что у книги "Евгений Онегин" и "Анна Каренина" есть жанр "Роман"
INSERT INTO bookgenres (book_id, genre_id) VALUES
                                               (1, 1),
                                               (2, 1);

-- Допустим, что у книги "Преступление и наказание" есть жанр "Драма"
INSERT INTO bookgenres (book_id, genre_id) VALUES
    (3, 5);

-- Допустим, что у книги "Белая гвардия" есть жанры "Драма" и "Роман"
INSERT INTO bookgenres (book_id, genre_id) VALUES
                                               (4, 5),
                                               (4, 1);

-- Допустим, что у книги "Темные аллеи" есть жанры "Поэзия" и "Драма"
INSERT INTO bookgenres (book_id, genre_id) VALUES
                                               (5, 2),
                                               (5, 5);