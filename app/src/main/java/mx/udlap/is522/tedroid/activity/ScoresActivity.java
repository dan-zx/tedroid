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
package mx.udlap.is522.tedroid.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import mx.udlap.is522.tedroid.util.Typefaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Actividad que muestra la lista de puntajes obtenidos en el juego.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class ScoresActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Map<String, List<Score>>> {

    private TextView classicGameHeaderText;
    private TextView specialGameHeaderText;
    private TableRow.LayoutParams layoutParams;
    private TableLayout scoreClassicTable;
    private TableLayout scoreSpecialTable;
    private Typeface twobitTypeface;
    private float primaryTextSize;
    private float secondaryTextSize;
    private int primaryColor;
    private int secondaryColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        findViews();
        initConstants();
        setupScoresHeaders();
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }

    /** Ecuentra las vistas. */
    private void findViews() {
        classicGameHeaderText = (TextView) findViewById(R.id.classic_game_header_text);
        specialGameHeaderText = (TextView) findViewById(R.id.special_game_header_text);
        scoreClassicTable = (TableLayout) findViewById(R.id.score_classic_table);
        scoreSpecialTable = (TableLayout) findViewById(R.id.score_special_table);
    }

    /** Inicializa las constantes a usar. */
    private void initConstants() {
        layoutParams = new TableRow.LayoutParams();
        layoutParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.score_table_margin);
        layoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.score_table_margin);
        twobitTypeface = Typefaces.get(this, Typefaces.Font.TWOBIT);
        primaryTextSize = getResources().getDimension(R.dimen.primary_text_size);
        secondaryTextSize = getResources().getDimension(R.dimen.secondary_text_size);
        primaryColor = getResources().getColor(R.color.primary_for_background);
        secondaryColor = getResources().getColor(R.color.secondary_for_background);
    }

    /** Setea estilos a los headers. */
    private void setupScoresHeaders() {
        classicGameHeaderText.setTypeface(twobitTypeface);
        specialGameHeaderText.setTypeface(twobitTypeface);
    }

    /**
     * Setea los valores de los objetos Score proporcionados o manda un mensaje si no hay datos.
     * 
     * @param scores objetos Score.
     */
    private void setUpScores(List<Score> scores, TableLayout table) {
        if (scores != null && !scores.isEmpty()) {
            table.removeAllViews();
            table.addView(createHeaderRow());
            for (Score score : scores) table.addView(toTableRow(score));
        } else {
            table.removeAllViews();
            table.addView(emptyScoresRow());
        }
    }

    /** @return una fila con el texto de que no hay puntajes. */
    private TableRow emptyScoresRow() {
        TableRow row = new TableRow(this);
        TextView noScoresText = new TextView(this);
        noScoresText.setText(R.string.no_scores_message);
        applyPrimaryStyleTo(noScoresText);
        row.addView(noScoresText);
        return row;
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
    public ScoreLoader onCreateLoader(int id, Bundle args) {
        return new ScoreLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<Map<String, List<Score>>> loader, Map<String, List<Score>> data) {
        if (data != null && !data.isEmpty()) {
            setUpScores(data.get("classic"), scoreClassicTable);
            setUpScores(data.get("special"), scoreSpecialTable);
        } else {
            setUpScores(null, scoreClassicTable);
            setUpScores(null, scoreSpecialTable);
        }
    }

    @Override
    public void onLoaderReset(Loader<Map<String, List<Score>>> loader) {
        setUpScores(null, scoreClassicTable);
        setUpScores(null, scoreSpecialTable);
    }

    /**
     * Tarea asíncrona para recuperar todos los puntajes.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    private static class ScoreLoader extends AsyncTaskLoader<Map<String, List<Score>>> {

        private ScoreDAO scoreClassicDAO;
        private ScoreDAO scoreSpecialDAO;

        /**
         * Crea una nueva tarea asíncrona.
         * 
         * @param context el contexto de la aplicación.
         */
        private ScoreLoader(Context context) {
            super(context);
            scoreClassicDAO = new DAOFactory(context).getScoreClassicDAO();
            scoreSpecialDAO = new DAOFactory(context).getScoreSpecialDAO();
        }

        @Override
        public Map<String, List<Score>> loadInBackground() {
            HashMap<String, List<Score>> data = new HashMap<>(2);
            data.put("classic", scoreClassicDAO.readAllOrderedByPointsDesc());
            data.put("special", scoreSpecialDAO.readAllOrderedByPointsDesc());
            return data;
        }
    }
}
