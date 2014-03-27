package mx.udlap.is522.tedroid.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.data.Score;
import mx.udlap.is522.tedroid.data.dao.DAOFactory;
import mx.udlap.is522.tedroid.data.dao.ScoreDAO;

import java.util.List;

/**
 * Actividad que muestra la lista de puntajes obtenidos en el juego.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class ScoresActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<List<Score>> {

    private TableRow.LayoutParams layoutParams;
    private TableLayout scoreTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        scoreTable = (TableLayout) findViewById(R.id.score_table);
        layoutParams = new TableRow.LayoutParams();
        layoutParams.rightMargin = dpToPixel(10);
        layoutParams.topMargin = dpToPixel(10);
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }

    /**
     * Setea los valores de los objetos Score proporcionados o manda un mensaje
     * si no hay datos.
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
        pointsText.setLayoutParams(layoutParams);
        TextView levelText = new TextView(this);
        levelText.setText(String.valueOf(score.getLevel()));
        levelText.setLayoutParams(layoutParams);
        TextView linesText = new TextView(this);
        linesText.setText(String.valueOf(score.getLines()));
        linesText.setLayoutParams(layoutParams);
        TextView dateText = new TextView(this);
        String dateStr = getString(R.string.datetime_format, 
                DateFormat.getDateFormat(getApplicationContext()).format(score.getObtainedAt()),
                DateFormat.getTimeFormat(getApplicationContext()).format(score.getObtainedAt()));
        dateText.setText(dateStr);
        dateText.setLayoutParams(layoutParams);
        row.addView(pointsText);
        row.addView(levelText);
        row.addView(linesText);
        row.addView(dateText);
        return row;
    }

    /**
     * @return la fila de encabezados de la tabla.
     */
    private TableRow createHeaderRow() {
        TableRow headerRow = new TableRow(this);
        TextView pointsHeaderText = new TextView(this);
        pointsHeaderText.setText("Puntos");
        pointsHeaderText.setLayoutParams(layoutParams);
        TextView levelHeaderText = new TextView(this);
        levelHeaderText.setText("Nivel");
        levelHeaderText.setLayoutParams(layoutParams);
        TextView linesHeaderText = new TextView(this);
        linesHeaderText.setText("Lineas");
        linesHeaderText.setLayoutParams(layoutParams);
        TextView dateHeaderText = new TextView(this);
        dateHeaderText.setText("Fecha");
        dateHeaderText.setLayoutParams(layoutParams);
        headerRow.addView(pointsHeaderText);
        headerRow.addView(levelHeaderText);
        headerRow.addView(linesHeaderText);
        headerRow.addView(dateHeaderText);
        return headerRow;
    }

    /**
     * Convierte dps a pixeles.
     * 
     * @param dp dps
     * @return pixeles.
     */
    public int dpToPixel(int dp) {
        return (int) ((float) dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public Loader<List<Score>> onCreateLoader(int id, Bundle args) {
        return new ScoreLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Score>> loader, List<Score> data) {
        setUpScores(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Score>> loader) {
        setUpScores(null);
    }

    /**
     * Tarea asíncrona para recuperar todos los puntajes. 
     * (TODO: debe estar en otro lado esta clase)
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
        public ScoreLoader(Context context) {
            super(context);
            scoreDAO = new DAOFactory(context.getApplicationContext()).get(ScoreDAO.class);
        }

        @Override
        public List<Score> loadInBackground() {
            return scoreDAO.readAllOrderedByPointsDesc();
        }
    }
}