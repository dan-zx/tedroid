package mx.udlap.is522.tedroid.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import mx.udlap.is522.tedroid.R;

public class MainMenuActivity extends BaseGameActivity {

    private static final int UNUSED_REQUEST_CODE = 5471;

    private LinearLayout signInLayout;
    private Button achievementsButton;
    private SignInButton signInButton;
    private TextView signedUser;
    private AlertDialog offlineAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        TextView appTitle = (TextView) findViewById(R.id.app_title);
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/twobit.ttf");
        appTitle.setTypeface(customFont);
        offlineAlertDialog = new AlertDialog.Builder(this)
            .setTitle(R.string.offline_warn_title)
            .setMessage(R.string.offline_warn_message)
            .setPositiveButton(R.string.offline_warn_understand,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                        startActivity(intent);
                    }
                })
            .setNegativeButton(R.string.offline_warn_sign_in,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        beginUserInitiatedSignIn();
                    }
                })
            .create();
        signInLayout = (LinearLayout) findViewById(R.id.sign_in_layout);
        achievementsButton = (Button) findViewById(R.id.achievements_button);
        signedUser = (TextView) findViewById(R.id.signed_user);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                beginUserInitiatedSignIn();
            }
        });
    }

    public void onPlayButtonClick(View view) {
        offlineAlertDialog.show();
    }

    public void onScoresButtonClick(View view) {
        Intent intent = new Intent(this, ScoresActivity.class);
        startActivity(intent);
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
        signInLayout.setVisibility(View.VISIBLE);
        achievementsButton.setVisibility(View.GONE);
    }

    @Override
    public void onSignInSucceeded() {
        signInLayout.setVisibility(View.GONE);
        achievementsButton.setVisibility(View.VISIBLE);
        Player currentPlayer = Games.Players.getCurrentPlayer(getApiClient());
        signedUser.setText(currentPlayer.getDisplayName());
    }
}