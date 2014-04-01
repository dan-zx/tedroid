DROP TABLE IF EXISTS score;
DROP TABLE IF EXISTS pending_achievement;
DROP TABLE IF EXISTS unlocked_achievement;

CREATE TABLE score (
    obtained_at DATETIME NOT NULL PRIMARY KEY DEFAULT CURRENT_TIMESTAMP,
    level INTEGER NOT NULL,
    lines INTEGER NOT NULL,
    points INTEGER NOT NULL
);

CREATE TABLE unlocked_achievement (
	achievement_id VARCHAR(40) NOT NULL PRIMARY KEY
);

CREATE TABLE pending_achievement (
	achievement_id VARCHAR(40) NOT NULL PRIMARY KEY,
	FOREIGN KEY(achievement_id) REFERENCES unlocked_achievement(achievement_id) ON DELETE CASCADE ON UPDATE CASCADE 
);

CREATE INDEX score_points_idx ON score (points DESC);