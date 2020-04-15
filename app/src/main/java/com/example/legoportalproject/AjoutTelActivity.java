package com.example.legoportalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.provider.Settings;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AjoutTelActivity extends AppCompatActivity {


    private static final String REQUIRED = "Required";
    private static BluetoothAdapter BA;

    Button valide_btn, rmpl_auto_btn;
    EditText nom_tel_text;
    TextView mac_text, canAdd;
    DatabaseReference mDatabase;
    private String mail;

    String idUser  = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_tel);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mac_text = findViewById(R.id.mac_txt);
        nom_tel_text = findViewById(R.id.nom_tel_txt);
        valide_btn = findViewById(R.id.valide_btn);
        rmpl_auto_btn = findViewById(R.id.rmpl_auto_btn);
        canAdd = findViewById(R.id.canAdd_txt);
        // canAdd.setVisibility(View.INVISIBLE);

        Intent I = getIntent();
        if (I.hasExtra("mail")) {
            mail = I.getStringExtra("mail");
        }

        rmpl_auto_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mDatabase = FirebaseDatabase.getInstance().getReference("Télécommandes").child(idUser).child("ListeTélécommandes");
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer result = 0;
                        Boolean present = true;
                        final String nom = nom_tel_text.getText().toString().toUpperCase();
                        final String mac = mac_text.getText().toString().toUpperCase();

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            result = result + 1;

                            if (ds.child("mac_tel").getValue().equals(mac) || ds.child("nom_tel").getValue().equals(nom) ) {
                                present = false;
                            }

                        }

                        canAdd.setText(result.toString() + ' ' +  present.toString().toUpperCase());

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("tag", "Failed to read value.", error.toException());
                    }
                });

                mac_text.setText(getAndroidId());

            }
        });

        valide_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (canAdd.getText().toString().contains("3")) {
                    Toast.makeText(AjoutTelActivity.this, "Vous ne pouvez ajouter d'autres télécommandes", Toast.LENGTH_SHORT).show();
                } else {

                    verification();

                }

            }
        });

    }

    private void verification() {
        String nom = nom_tel_text.getText().toString().toUpperCase();
        String mac = mac_text.getText().toString().toUpperCase();

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

        if(canAdd.getText().toString().contains("TRUE")){

                submitTelecommande();
                Intent I = new Intent(AjoutTelActivity.this, MenuActivity.class);
                startActivity(I);

        } else if (canAdd.getText().toString().contains("FALSE")){
            Toast.makeText(AjoutTelActivity.this, "Cette télécommande existe déjà", Toast.LENGTH_SHORT).show();
        }

    }


    public String getAndroidId() {

        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

    }


    private void submitTelecommande() {

        String nom = nom_tel_text.getText().toString().toUpperCase();
        String mac = mac_text.getText().toString().toUpperCase();

        String idUser  = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Telecommande tel = new Telecommande(nom, mac);
        mDatabase =  FirebaseDatabase.getInstance().getReference().child("Télécommandes").child(idUser).child("ListeTélécommandes");
        mDatabase.push().setValue(tel);

    }



}