package mx.udlap.is522.tedroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import mx.udlap.is522.tedroid.R;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        TextView appTitle = (TextView) findViewById(R.id.app_title);
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/twobit.ttf");
        appTitle.setTypeface(customFont);
    }
    
    public void onPlayButtonClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void onScoresButtonClick(View view) {
        Intent intent = new Intent(this, ScoresActivity.class);
        startActivity(intent);
    }

    public void onSettingsButtonClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}