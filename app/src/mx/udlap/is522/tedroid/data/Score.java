package mx.udlap.is522.tedroid.data;

import java.io.Serializable;

/**
 * Representa el puntaje obtenido en las partidas del juego.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class Score implements Serializable {

    private static final long serialVersionUID = -6306589594223400629L;

    private long id;
    private int points;
    private int level;
    private int lines;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + level;
        result = prime * result + lines;
        result = prime * result + points;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Score other = (Score) obj;
        if (id != other.id) return false;
        if (level != other.level) return false;
        if (lines != other.lines) return false;
        if (points != other.points) return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringBuilder().append('{')
                .append("id: ").append(id).append(", ")
                .append("points: ").append(points).append(", ")
                .append("level: ").append(level).append(", ")
                .append("lines: ").append(lines)
                .append('}')
                .toString();
    }
}