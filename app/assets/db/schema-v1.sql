DROP TABLE IF EXISTS score;
DROP TABLE IF EXISTS pending_achievement;
DROP TABLE IF EXISTS unlocked_achievement;

CREATE TABLE score (
    _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    obtained_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP UNIQUE,
    level INTEGER NOT NULL,
    lines INTEGER NOT NULL,
    points INTEGER NOT NULL,
    is_uploaded_to_google_play BOOLEAN NOT NULL DEFAULT 0
);

CREATE TABLE pending_achievement (
	achievement_id VARCHAR(40) NOT NULL PRIMARY KEY
);

CREATE TABLE unlocked_achievement (
	achievement_id VARCHAR(40) NOT NULL PRIMARY KEY
);

CREATE INDEX score_points_idx ON score (points DESC);