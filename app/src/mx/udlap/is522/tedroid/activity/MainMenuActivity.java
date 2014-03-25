package mx.udlap.is522.tedroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.games.Games;
import mx.udlap.is522.tedroid.R;

public class MainMenuActivity extends BaseGameHelperActivity {

    private static final int UNUSED_REQUEST_CODE = 5471;

    private LinearLayout signInLayout;
    private Button leaderboardsButton;
    private Button achievementsButton;
    private SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        signInLayout = (LinearLayout) findViewById(R.id.sign_in_layout);
        leaderboardsButton = (Button) findViewById(R.id.leaderboards_button);
        achievementsButton = (Button) findViewById(R.id.achievements_button);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                beginUserInitiatedSignIn();
            }
        });
    }

    public void onPlayButtonClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void onScoresButtonClick(View view) {
        Intent intent = new Intent(this, ScoresActivity.class);
        startActivity(intent);
    }

    public void onLeaderboardsButtonClick(View view) {
        Intent intent = Games.Leaderboards.getAllLeaderboardsIntent(getApiClient());
        startActivityForResult(intent, UNUSED_REQUEST_CODE);
    }

    public void onAchievementsButtonClick(View view) {
        Intent intent = Games.Achievements.getAchievementsIntent(getApiClient());
        startActivityForResult(intent, UNUSED_REQUEST_CODE);
    }

    public void onSettingsButtonClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSignInFailed() {
        super.onSignInFailed();
        signInLayout.setVisibility(View.VISIBLE);
        leaderboardsButton.setVisibility(View.GONE);
        achievementsButton.setVisibility(View.GONE);
    }

    @Override
    public void onSignInSucceeded() {
        super.onSignInSucceeded();
        signInLayout.setVisibility(View.GONE);
        leaderboardsButton.setVisibility(View.VISIBLE);
        achievementsButton.setVisibility(View.VISIBLE);
    }
}