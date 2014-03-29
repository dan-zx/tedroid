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

    private static final long serialVersionUID = -9043388811203816706L;

    private Date obtainedAt;
    private int level;
    private int lines;
    private int points;
    private boolean isUploadedToGooglePlay;

    public Date getObtainedAt() {
        return obtainedAt;
    }

    public void setObtainedAt(Date obtainedAt) {
        this.obtainedAt = obtainedAt;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isUploadedToGooglePlay() {
        return isUploadedToGooglePlay;
    }
    
    public void setUploadedToGooglePlay(boolean isUploadedToGooglePlay) {
        this.isUploadedToGooglePlay = isUploadedToGooglePlay;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isUploadedToGooglePlay ? 1231 : 1237);
        result = prime * result + level;
        result = prime * result + lines;
        result = prime * result + ((obtainedAt == null) ? 0 : obtainedAt.hashCode());
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
        if (isUploadedToGooglePlay != other.isUploadedToGooglePlay) return false;
        if (level != other.level) return false;
        if (lines != other.lines) return false;
        if (obtainedAt == null) {
            if (other.obtainedAt != null) return false;
        } else if (!obtainedAt.equals(other.obtainedAt)) return false;
        if (points != other.points) return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringBuilder().append('{')
                .append("obtainedAt: ").append(obtainedAt).append(", ")
                .append("level: ").append(level).append(", ")
                .append("lines: ").append(lines).append(", ")
                .append("points: ").append(points).append(", ")
                .append("isUploadedToGooglePlay: ").append(isUploadedToGooglePlay)
                .append('}')
                .toString();
    }
}