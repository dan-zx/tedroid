package mx.udlap.is522.tedroid.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.data.Score;
import mx.udlap.is522.tedroid.data.dao.ScoreDAO;
import mx.udlap.is522.tedroid.data.dao.impl.DAOFactory;
import mx.udlap.is522.tedroid.util.Identifiers;
import mx.udlap.is522.tedroid.util.Typefaces;

import java.util.List;

/**
 * Actividad que muestra la lista de puntajes obtenidos en el juego.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class ScoresActivity extends ActivityWithMusic implements LoaderManager.LoaderCallbacks<List<Score>> {

    private TableRow.LayoutParams layoutParams;
    private TableLayout scoreTable;
    private Typeface twobitTypeface;
    private float primaryTextSize;
    private float secondaryTextSize;
    private int primaryColor;
    private int secondaryColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        scoreTable = (TableLayout) findViewById(R.id.score_table);
        layoutParams = new TableRow.LayoutParams();
        layoutParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.score_table_margin);
        layoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.score_table_margin);
        twobitTypeface = Typefaces.get(this, Typefaces.Font.TWOBIT);
        primaryTextSize = getResources().getDimension(R.dimen.primary_text_size);
        secondaryTextSize = getResources().getDimension(R.dimen.secondary_text_size);
        primaryColor = getResources().getColor(R.color.primary_for_background);
        secondaryColor = getResources().getColor(R.color.secondary_for_background);
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }

    /** {@inheritDoc} */
    @Override
    protected MediaPlayer setUpMediaPlayer() {
        if (isMusicEnabled()) {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.music_score);
            mediaPlayer.setLooping(true);
            return mediaPlayer;
        } else return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        playOrResumeTrack();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTrack();
    }

    @Override
    public void finish() {
        super.finish();
        stopPlayback();
    }

    /**
     * Setea los valores de los objetos Score proporcionados o manda un mensaje si no hay datos.
     * 
     * @param scores objetos Score.
     */
    private void setUpScores(List<Score> scores) {
        if (scores != null && !scores.isEmpty()) {
            scoreTable.removeAllViews();
            scoreTable.addView(createHeaderRow());
            for (Score score : scores) scoreTable.addView(toTableRow(score));
        } else {
            scoreTable.removeAllViews();
            if (scores != null && scores.isEmpty()) {
                new AlertDialog.Builder(this)
                    .setMessage(R.string.no_scores_message)
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .show();
            }
        }
    }

    /**
     * Convierte un objeto Score en objeto TableRow.
     * 
     * @param score el objeto Score a convertir.
     * @return un objeto TableRow.
     */
    private TableRow toTableRow(Score score) {
        TableRow row = new TableRow(this);
        TextView pointsText = new TextView(this);
        pointsText.setText(String.valueOf(score.getPoints()));
        applySecondaryStyleTo(pointsText);
        pointsText.setLayoutParams(layoutParams);
        TextView levelText = new TextView(this);
        levelText.setText(String.valueOf(score.getLevel()));
        applySecondaryStyleTo(levelText);
        TextView linesText = new TextView(this);
        linesText.setText(String.valueOf(score.getLines()));
        applySecondaryStyleTo(linesText);
        TextView dateText = new TextView(this);
        String dateStr = getString(R.string.datetime_format, 
                DateFormat.getDateFormat(getApplicationContext()).format(score.getObtainedAt()), 
                DateFormat.getTimeFormat(getApplicationContext()).format(score.getObtainedAt()));
        dateText.setText(dateStr);
        applySecondaryStyleTo(dateText);
        row.addView(pointsText);
        row.addView(levelText);
        row.addView(linesText);
        row.addView(dateText);
        return row;
    }

    /** Aplica el tema estilo secundario */
    private void applySecondaryStyleTo(TextView textView) {
        textView.setTextColor(secondaryColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondaryTextSize);
        textView.setTypeface(twobitTypeface);
        textView.setLayoutParams(layoutParams);
    }

    /** @return la fila de encabezados de la tabla. */
    private TableRow createHeaderRow() {
        TableRow headerRow = new TableRow(this);
        TextView pointsHeaderText = new TextView(this);
        pointsHeaderText.setText(R.string.points_header);
        applyPrimaryStyleTo(pointsHeaderText);
        TextView levelHeaderText = new TextView(this);
        levelHeaderText.setText(R.string.level_header);
        applyPrimaryStyleTo(levelHeaderText);
        TextView linesHeaderText = new TextView(this);
        linesHeaderText.setText(R.string.lines_header);
        applyPrimaryStyleTo(linesHeaderText);
        TextView dateHeaderText = new TextView(this);
        dateHeaderText.setText(R.string.date_header);
        applyPrimaryStyleTo(dateHeaderText);
        headerRow.addView(pointsHeaderText);
        headerRow.addView(levelHeaderText);
        headerRow.addView(linesHeaderText);
        headerRow.addView(dateHeaderText);
        return headerRow;
    }

    /** Aplica el tema estilo primario. */
    private void applyPrimaryStyleTo(TextView textView) {
        textView.setTextColor(primaryColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, primaryTextSize);
        textView.setTypeface(twobitTypeface);
        textView.setLayoutParams(layoutParams);
    }

    @Override
    public Loader<List<Score>> onCreateLoader(int id, Bundle args) {
        return new ScoreLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Score>> loader, List<Score> data) {
        setUpScores(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Score>> loader) {
        setUpScores(null);
    }

    /** @return si la musica esta habilitada o no. */
    private boolean isMusicEnabled() {
        String resIdName = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.music_type_key), getString(R.string.default_music_type));
        return Identifiers.getFrom(resIdName, Identifiers.ResourceType.RAW, this) != Identifiers.NOT_FOUND;
    }

    /**
     * Tarea asíncrona para recuperar todos los puntajes.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    private static class ScoreLoader extends AsyncTaskLoader<List<Score>> {

        private ScoreDAO scoreDAO;

        /**
         * Crea una nueva tarea asíncrona.
         * 
         * @param context el contexto de la aplicación.
         */
        private ScoreLoader(Context context) {
            super(context);
            scoreDAO = new DAOFactory(context).get(ScoreDAO.class);
        }

        @Override
        public List<Score> loadInBackground() {
            return scoreDAO.readAllOrderedByPointsDesc();
        }
    }
}