package mx.udlap.is522.tedroid.activity;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;

import mx.udlap.is522.tedroid.R;

public class InterfazActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interfaz1);
        View titulo = findViewById(R.id.titulo);
        titulo.setOnClickListener((OnClickListener) this);
    }

    @Override
    public void onClick(View vista) {
        // TODO Auto-generated method stub
        if (vista.getId() == findViewById(R.id.titulo).getId()) {
            Intent j = new Intent(this, GameActivity.class);
            startActivity(j);
        }
    }

}