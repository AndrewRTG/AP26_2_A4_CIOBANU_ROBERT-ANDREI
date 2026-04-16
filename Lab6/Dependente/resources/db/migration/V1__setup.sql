CREATE TABLE IF NOT EXISTS genres (
                                      id SERIAL PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS movies (
                                      id SERIAL PRIMARY KEY,
                                      title VARCHAR(255) NOT NULL,
    release_date DATE,
    duration INTEGER,
    score NUMERIC(3, 1),
    genre_id INTEGER REFERENCES genres(id)
    );

CREATE TABLE IF NOT EXISTS actors (
                                      id SERIAL PRIMARY KEY,
                                      name VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS movie_actors (
                                            movie_id INTEGER REFERENCES movies(id) ON DELETE CASCADE,
    actor_id INTEGER REFERENCES actors(id) ON DELETE CASCADE,
    PRIMARY KEY (movie_id, actor_id)
    );