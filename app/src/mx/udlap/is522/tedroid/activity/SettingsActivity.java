package mx.udlap.is522.tedroid.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import mx.udlap.is522.tedroid.R;

/**
 * Actividad para modificar las opciones del juego.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}