CREATE TABLE IF NOT EXISTS movie_lists (
                                           id SERIAL PRIMARY KEY,
                                           name VARCHAR(255) NOT NULL,
    creation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS movie_list_items (
                                                list_id INTEGER REFERENCES movie_lists(id) ON DELETE CASCADE,
    movie_id INTEGER REFERENCES movies(id) ON DELETE CASCADE,
    PRIMARY KEY (list_id, movie_id)
    );