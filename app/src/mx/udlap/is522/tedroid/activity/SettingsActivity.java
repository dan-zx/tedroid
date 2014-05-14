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

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.data.dao.ScoreDAO;
import mx.udlap.is522.tedroid.data.dao.impl.DAOFactory;

/**
 * Actividad para modificar las opciones del juego.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class SettingsActivity extends PreferenceActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private Preference deleteScoresPreference;
    private Preference versionPreference;
    private Preference openSourceLicencesPreference;
    private Preference sfxLicencesPreference;
    private AlertDialog deleteScoresWarnDialog;
    private AlertDialog openSourceLicencesDialog;
    private AlertDialog sfxLicencesDialog;

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) displayHomeAsUp();
        findPreferences();
        setUpDeleteScoresPreference();
        setUpOpenSourceLicencesPreference();
        setUpSfxLicencesPreference();
        setUpVersionPreference();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    /** Encuentra todas los Preferences en el esquema xml. */
    @SuppressWarnings("deprecation")
    private void findPreferences() {
        deleteScoresPreference = findPreference(getString(R.string.delete_scores_key));
        openSourceLicencesPreference = findPreference(getString(R.string.open_source_licenses_key));
        sfxLicencesPreference = findPreference(getString(R.string.sfx_licenses_key));
        versionPreference = findPreference(getString(R.string.version_key));
    }

    /** Inicializa la configuración de borrado de puntajes. */
    private void setUpDeleteScoresPreference() {
        deleteScoresWarnDialog = new AlertDialog.Builder(this)
            .setTitle(R.string.delete_scores_warn_title)
            .setMessage(R.string.delete_scores_warn_message)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
    
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new ScoreDeleter().execute();
                }
            })
            .create();
        deleteScoresPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                deleteScoresWarnDialog.show();
                return true;
            }
        });
    }

    /** Inicializa la configuración del anuncio de licensias de código libre */
    private void setUpOpenSourceLicencesPreference() {
        WebView webView = new WebView(this);
        webView.loadUrl(getString(R.string.open_source_licenses_url));
        openSourceLicencesDialog = new AlertDialog.Builder(this)
            .setTitle(R.string.open_source_licenses_pref_title)
            .setView(webView)
            .create();
        openSourceLicencesPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                openSourceLicencesDialog.show();
                return true;
            }
        });
    }
    
    private void setUpSfxLicencesPreference() {
        WebView webView = new WebView(this);
        webView.loadUrl(getString(R.string.sfx_licenses_url));
        sfxLicencesDialog = new AlertDialog.Builder(this)
            .setTitle(R.string.sfx_licenses_pref_title)
            .setView(webView)
            .create();
        sfxLicencesPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                sfxLicencesDialog.show();
                return true;
            }
        });
    }

    /** Inicializa la configuración de la versión de la aplicación */
    private void setUpVersionPreference() {
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionPreference.setSummary(versionName);
        } catch (NameNotFoundException ex) {
            Log.e(TAG, "Couldn't find version name", ex);
        }
    }

    /**
     * Muestra el icóno de la actividad como atajo para regresar a la actividad anterior (Solo en
     * API 11+).
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void displayHomeAsUp() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Tarea asyncrona para borrar todos los puntajes locales.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    private class ScoreDeleter extends AsyncTask<Void, Void, Void> {

        private ScoreDAO scoreDAO;

        private ScoreDeleter() {
            scoreDAO = new DAOFactory(getApplicationContext()).get(ScoreDAO.class);
        }

        @Override
        protected Void doInBackground(Void... params) {
            scoreDAO.deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(getApplicationContext(), 
                    R.string.done_delete_scores_message, 
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
}