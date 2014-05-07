/*
 * Copyright 2014 Tedroid developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
DROP TABLE IF EXISTS score;

CREATE TABLE score (
    obtained_at DATETIME NOT NULL PRIMARY KEY DEFAULT CURRENT_TIMESTAMP,
    level INTEGER NOT NULL,
    lines INTEGER NOT NULL,
    points INTEGER NOT NULL
);

CREATE INDEX score_points_idx ON score (points DESC);