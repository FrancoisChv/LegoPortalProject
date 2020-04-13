package com.example.legoportalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    public Button btnLancer;
    public Button btnAjout;
    public Button btnLog;
    public Button btnParam;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        btnLancer = findViewById(R.id.lance_btn);
        btnAjout = findViewById(R.id.ajout_btn);
        btnLog = findViewById(R.id.log_btn);
        btnParam = findViewById(R.id.param_btn);

        btnLancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(MenuActivity.this, ConnectActivity.class);
                startActivity(I);
            }
        });

        btnAjout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(MenuActivity.this, AjoutTelActivity.class);
                startActivity(I);

            }
        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        btnParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(MenuActivity.this, ParametersActivity.class);
                startActivity(I);
            }
        });

    }
}
