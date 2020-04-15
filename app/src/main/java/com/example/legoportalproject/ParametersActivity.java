package com.example.legoportalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParametersActivity extends AppCompatActivity {
    public String mail;
    TextView Mac1, Mac2, Mac3, Tel1, Tel2, Tel3;
    EditText macPortal;
    Button delete1, delete2, delete3, modifMacPortal;
    DatabaseReference mDatabase;
    static Integer nb = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);

        Mac1 = findViewById(R.id.mac1);
        Mac2 = findViewById(R.id.mac2);
        Mac3 = findViewById(R.id.mac3);

        Tel1 = findViewById(R.id.tel1);
        Tel2 = findViewById(R.id.tel2);
        Tel3 = findViewById(R.id.tel3);

        macPortal = findViewById(R.id.mac_portal);

        delete1 = findViewById(R.id.delete_btn1);
        delete2 = findViewById(R.id.delete_btn2);
        delete3 = findViewById(R.id.delete_btn3);
        modifMacPortal = findViewById(R.id.modif_mac_btn);

        Intent I = getIntent();
        if (I.hasExtra("mail")) {
            mail = I.getStringExtra("mail");
        }

        String idUser  = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("Télécommandes").child(idUser).child("MacPortail");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                macPortal.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        modifMacPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mac = macPortal.getText().toString();
                mDatabase.setValue(mac);
                Intent I = new Intent(ParametersActivity.this, MenuActivity.class);
                startActivity(I);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("Télécommandes").child(idUser).child("ListeTélécommandes");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    nb = nb+ 1;
                    if (nb == 1) {
                        Mac1.setText(ds.child("nom_tel").getValue(String.class));
                        Tel1.setText(ds.child("mac_tel").getValue(String.class));
                    }
                    if (nb == 2) {
                        Mac2.setText(ds.child("nom_tel").getValue(String.class));
                        Tel2.setText(ds.child("mac_tel").getValue(String.class));
                    }
                    if (nb == 3) {
                        Mac3.setText(ds.child("nom_tel").getValue(String.class));
                        Tel3.setText(ds.child("mac_tel").getValue(String.class));
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("tag", "Failed to read value.", error.toException());
            }
        });

        delete1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                deleteData(1);
                Intent I = new Intent(ParametersActivity.this, ParametersActivity.class);
                startActivity(I);
            }
        });

        delete2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                deleteData(2);
                Intent I = new Intent(ParametersActivity.this, ParametersActivity.class);
                startActivity(I);
            }
        });

        delete3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                deleteData(3);
                Intent I = new Intent(ParametersActivity.this, ParametersActivity.class);
                startActivity(I);
            }
        });


    }

    public void deleteData(final Integer a){
        nb =0;
        String idUser  = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("Télécommandes").child(idUser).child("ListeTélécommandes");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    nb = nb + 1;
                    if (a == nb) {
                        ds.getRef().removeValue();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("tag", "Failed to read value.", error.toException());
            }
        });
    }
}