package mx.udlap.is522.tedroid.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Representa el puntaje obtenido en las partidas del juego.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class Score implements Serializable {

    private static final long serialVersionUID = 2888499014768693222L;

    private Date obtainedAt;
    private Integer level;
    private Integer lines;
    private Integer points;

    /** @return la fecha en la que se obtuvo este puntaje. */
    public Date getObtainedAt() {
        return obtainedAt;
    }

    /** @param obtainedAt la fecha en la que se obtuvo este puntaje. */
    public void setObtainedAt(Date obtainedAt) {
        this.obtainedAt = obtainedAt;
    }

    /** @return el nivel alcanzado en la partida. */
    public Integer getLevel() {
        return level;
    }

    /** @param level el nivel alcanzado en la partida. */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /** @return las lineas borradas en la partida. */
    public Integer getLines() {
        return lines;
    }

    /** @param lines las lineas borradas en la partida. */
    public void setLines(Integer lines) {
        this.lines = lines;
    }

    /** @return los puntos conseguidos en la partida. */
    public Integer getPoints() {
        return points;
    }

    /** @param points los puntos conseguidos en la partida. */
    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((level == null) ? 0 : level.hashCode());
        result = prime * result + ((lines == null) ? 0 : lines.hashCode());
        result = prime * result + ((obtainedAt == null) ? 0 : obtainedAt.hashCode());
        result = prime * result + ((points == null) ? 0 : points.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Score other = (Score) obj;
        if (level == null) {
            if (other.level != null) return false;
        } else if (!level.equals(other.level)) return false;
        if (lines == null) {
            if (other.lines != null) return false;
        } else if (!lines.equals(other.lines)) return false;
        if (obtainedAt == null) {
            if (other.obtainedAt != null) return false;
        } else if (!obtainedAt.equals(other.obtainedAt)) return false;
        if (points == null) {
            if (other.points != null) return false;
        } else if (!points.equals(other.points)) return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append('{')
            .append("obtainedAt: ").append(obtainedAt).append(", ")
            .append("level: ").append(level).append(", ")
            .append("lines: ").append(lines).append(", ")
            .append("points: ").append(points)
            .append('}')
            .toString();
    }
}