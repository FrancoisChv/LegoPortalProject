package com.example.legoportalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {


    public Button btnLancer;
    public Button btnAjout;
    public Button btnLog;
    public Button btnParam;
    public Button btnDeco;
    String mail;
    FirebaseAuth firebaseAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        //Lien entre nos boutons et le layout
        btnLancer = findViewById(R.id.lance_btn);
        btnAjout = findViewById(R.id.ajout_btn);
        btnLog = findViewById(R.id.log_btn);
        btnParam = findViewById(R.id.param_btn);
        btnDeco = findViewById(R.id.deco_btn);
        firebaseAuth = FirebaseAuth.getInstance();

        Intent I = getIntent();
        if (I.hasExtra("mail")) {
            mail = I.getStringExtra("mail");
        }

        //Lorsque l'utilisateur appuie sur le bouton lancer télécommande
        btnLancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //On lance l'activité ConnectActivity
                Intent I = new Intent(MenuActivity.this, ConnectActivity.class);
                startActivity(I);
            }
        });

        //Lorsque l'utilisateur appuie sur le bouton déconnexion
        btnDeco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On déconnecte l'utilisateur de l'application et on le redirige vers l'activité LoginActivity
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

        //Lorsque l'utilisateur appuie sur le bouton Ajouter télécommande
        btnAjout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("1", mail);
                editor.apply();
                //On lance l'activité AjoutTelActivity
                Intent I = new Intent(MenuActivity.this, AjoutTelActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("mail", String.valueOf(mail));
                I.putExtras(bundle);
                startActivity(I);

            }
        });

        //Lorsque l'utilisateur appuie sur le bouton des logs
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        //Lorsque l'utilisateur appuie sur le bouton Paramètres
        btnParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //On lance l'activité Parameters Activity
                Intent I = new Intent(MenuActivity.this, ParametersActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("mail", String.valueOf(mail));
                I.putExtras(bundle);
                startActivity(I);
            }
        });

    }
}
