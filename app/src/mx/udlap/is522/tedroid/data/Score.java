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

    private static final long serialVersionUID = -2512169818539926092L;

    private Date obtainedAt;
    private Integer level;
    private Integer lines;
    private Integer points;
    private Boolean isUploadedToGooglePlay;

    public Date getObtainedAt() {
        return obtainedAt;
    }

    public void setObtainedAt(Date obtainedAt) {
        this.obtainedAt = obtainedAt;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLines() {
        return lines;
    }

    public void setLines(Integer lines) {
        this.lines = lines;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Boolean getIsUploadedToGooglePlay() {
        return isUploadedToGooglePlay;
    }

    public void setIsUploadedToGooglePlay(Boolean isUploadedToGooglePlay) {
        this.isUploadedToGooglePlay = isUploadedToGooglePlay;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((isUploadedToGooglePlay == null) ? 0 : isUploadedToGooglePlay.hashCode());
        result = prime * result + ((level == null) ? 0 : level.hashCode());
        result = prime * result + ((lines == null) ? 0 : lines.hashCode());
        result = prime * result + ((obtainedAt == null) ? 0 : obtainedAt.hashCode());
        result = prime * result + ((points == null) ? 0 : points.hashCode());
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
        if (isUploadedToGooglePlay == null) {
            if (other.isUploadedToGooglePlay != null) return false;
        } else if (!isUploadedToGooglePlay.equals(other.isUploadedToGooglePlay)) return false;
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