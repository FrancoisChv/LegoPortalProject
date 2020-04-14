package com.example.legoportalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AjoutTelActivity extends AppCompatActivity {

    private static final String REQUIRED = "Required";

    Button valide_btn, rmpl_auto_btn;
    EditText mac_text, nom_tel_text;
    DatabaseReference mDatabase;
    private String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_tel);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mac_text = findViewById(R.id.mac_txt);
        nom_tel_text = findViewById(R.id.nom_tel_txt);
        valide_btn = findViewById(R.id.valide_btn);
        rmpl_auto_btn = findViewById(R.id.rmpl_auto_btn);

        Intent I = getIntent();
        if (I.hasExtra("mail")) {
            mail = I.getStringExtra("mail");
        }

        rmpl_auto_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                @SuppressLint("HardwareIds") String address = info.getMacAddress();
                mac_text.setText(address);
            }
        });

        valide_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                submitTelecommande();
                Intent I = new Intent(AjoutTelActivity.this, MenuActivity.class);
                startActivity(I);
            }
        });
    }

    private void submitTelecommande() {

        final String nom = nom_tel_text.getText().toString();
        final String mac = mac_text.getText().toString();

        // nom requis
        if (TextUtils.isEmpty(nom)) {
            nom_tel_text.setError(REQUIRED);
            return;
        }

        // mac requis
        if (TextUtils.isEmpty(mac)) {
            mac_text.setError(REQUIRED);
            return;
        }


        Telecommande tel = new Telecommande(mac, nom, mail);
        mDatabase =  FirebaseDatabase.getInstance().getReference().child("ListTelecommandes");
        mDatabase.push().setValue(tel);

       /* mDatabase.child(mac).child("MAC").setValue(mac);
        mDatabase.child(mac).child("NOM").setValue(nom);
        mDatabase.child(mac).child("MAIL").setValue(mail);*/




    }

}