package mx.udlap.is522.tedroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import mx.udlap.is522.tedroid.R;

public class MainMenuActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        View game_start = findViewById(R.id.game_start);
        game_start.setOnClickListener((OnClickListener) this);

    }

    @Override
    public void onClick(View vista) {
        // TODO Auto-generated method stub
        if (vista.getId() == findViewById(R.id.game_start).getId()) {

            Intent j = new Intent(this, GameActivity.class);
            startActivity(j);
        }

    }
}
