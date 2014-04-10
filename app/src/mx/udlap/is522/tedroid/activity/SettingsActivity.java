package mx.udlap.is522.tedroid.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.data.source.TedroidSQLiteOpenHelper;

/**
 * Actividad para modificar las opciones del juego.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class SettingsActivity extends PreferenceActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private ListPreference musicTypePreference;
    private Preference deleteScoresPreference;
    private Preference restoreSettingsPreference;
    private Preference versionPreference;
    private Preference openSourceLicencesPreference;
    private AlertDialog deleteScoresWarnDialog;
    private AlertDialog restoreSettingsWarnDialog;
    private AlertDialog openSourceLicencesDialog;

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) displayHomeAsUp();
        findPreferences();
        setUpMusicTypePreference();
        setUpDeleteScoresPreference();
        setUpRestoreSettingsPreference();
        setUpOpenSourceLicencesPreference();
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
        musicTypePreference = (ListPreference) findPreference(getString(R.string.music_type_key));
        deleteScoresPreference = findPreference(getString(R.string.delete_scores_key));
        restoreSettingsPreference = findPreference(getString(R.string.restore_settings_key));
        openSourceLicencesPreference = findPreference(getString(R.string.open_source_licenses_key));
        versionPreference = findPreference(getString(R.string.version_key));
    }

    /** Inicializa la configuración de música */
    private void setUpMusicTypePreference() {
        musicTypePreference.setSummary(getString(R.string.music_type_pref_summary, musicTypePreference.getEntry()));
        musicTypePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ListPreference listPref = (ListPreference) preference;
                listPref.setValue(newValue.toString());
                listPref.setSummary(getString(R.string.music_type_pref_summary, listPref.getEntry()));
                return true;
            }
        });
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
                    TedroidSQLiteOpenHelper.destroyDb(getApplicationContext());
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), 
                            R.string.done_delete_scores_message, 
                            Toast.LENGTH_SHORT)
                            .show();
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

    /** Inicializa la configuración de restauración de configuración. */
    private void setUpRestoreSettingsPreference() {
        restoreSettingsWarnDialog = new AlertDialog.Builder(this)
            .setTitle(R.string.restore_settings_warn_title)
            .setMessage(R.string.restore_settings_warn_message)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
    
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    restoreSettings();
                }
            })
            .create();
        
        restoreSettingsPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
    
            @Override
            public boolean onPreferenceClick(Preference preference) {
                restoreSettingsWarnDialog.show();
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
     * Muestra el icóno de la actividad como atajo para regresar a la actividad
     * anterior (Solo en API 11+).
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void displayHomeAsUp() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /** Restaura las configuraciones a las premeditadas reiniciando esta actividad. */
    private void restoreSettings() {
        PreferenceManager.getDefaultSharedPreferences(this)
            .edit()
            .clear()
            .commit();
        PreferenceManager.setDefaultValues(this, R.xml.settings, true);
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
        Toast.makeText(getApplicationContext(), 
                R.string.done_restore_settings_message, 
                Toast.LENGTH_SHORT)
                .show();
    }
}