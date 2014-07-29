CREATE TABLE IF NOT EXISTS score (
    obtained_at DATETIME NOT NULL PRIMARY KEY DEFAULT CURRENT_TIMESTAMP,
    level INTEGER NOT NULL,
    lines INTEGER NOT NULL,
    points INTEGER NOT NULL
);

CREATE INDEX IF NOT EXISTS score_points_idx ON score (points DESC);

ALTER TABLE score RENAME TO score_classic;

CREATE TABLE score_special (
    obtained_at DATETIME NOT NULL PRIMARY KEY DEFAULT CURRENT_TIMESTAMP,
    level INTEGER NOT NULL,
    lines INTEGER NOT NULL,
    points INTEGER NOT NULL
);

CREATE INDEX score_special_points_idx ON score_special (points DESC);