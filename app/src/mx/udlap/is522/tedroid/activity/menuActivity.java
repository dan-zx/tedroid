package mx.udlap.is522.tedroid.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.data.Score;
import mx.udlap.is522.tedroid.data.dao.DAOFactory;


public class menuActivity extends Activity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        View game_start = findViewById(R.id.game_start);
        game_start.setOnClickListener((OnClickListener) this);
        
	
	}

	@Override
	public void onClick(View vista) {
		// TODO Auto-generated method stub
		if(vista.getId()==findViewById(R.id.game_start).getId()){
			
			Intent j = new Intent(this,GameActivity.class);
			startActivity(j);
		}
		
	}
}
