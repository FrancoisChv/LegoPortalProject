package com.example.legoportalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParametersActivity extends AppCompatActivity {
    public String mail;
    TextView Mac1, Mac2, Mac3, Tel1, Tel2, Tel3;
    DatabaseReference mDatabase;
    private Integer nb = 0;

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

        Intent I = getIntent();
        if (I.hasExtra("mail")) {
            mail = I.getStringExtra("mail");
        }

        if (mail.equals("")){
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            mail = sharedPreferences.getString("1", "");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("ListTelecommandes");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("user_tel").getValue().equals(mail)) {
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

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("tag", "Failed to read value.", error.toException());
            }
        });
    }
}
