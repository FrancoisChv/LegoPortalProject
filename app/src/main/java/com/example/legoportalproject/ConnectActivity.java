package com.example.legoportalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ConnectActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    Button button;
    TextView macAddText, nameTelText,IdTelText;
    DatabaseReference mDatabase, mDatabase2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        sharedpreferences = getSharedPreferences(getString(R.string.MyPREFERENCES), Context.MODE_PRIVATE);

        button = findViewById(R.id.connectButton);
        macAddText = findViewById(R.id.MacAddPortalText);
        nameTelText = findViewById(R.id.NametelText);
        IdTelText = findViewById(R.id.IDtelText);

        String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("Télécommandes").child(idUser).child("MacPortail");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                macAddText.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        showData();

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!validMacAdd(macAddText.getText().toString())){
// Notify user to enter mac address of brick
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConnectActivity.this);
                    builder.setMessage(R.string.enter_macadd);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
// User clicked OK button
                        }
                    });
// Create the AlertDialog
                    AlertDialog macDialog = builder.create();
                    macDialog.show();
                } else {
                    SharedPreferences.Editor speditor = sharedpreferences.edit();
                    speditor.putString(getString(R.string.EV3KEY), macAddText.getText().toString());
                    speditor.commit();
                    Intent intent = new Intent(ConnectActivity.this, MainActivity
                            .class);
                    startActivity(intent);
                }
            }
        });
    }

    public String getAndroidId() {

        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    private boolean validMacAdd(String macAdd) {
        return macAdd.length() == 17;
    }

    public void showData() {
        String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String idTel = getAndroidId();

        mDatabase2 = FirebaseDatabase.getInstance().getReference("Télécommandes").child(idUser).child("ListeTélécommandes");
        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("mac_tel").getValue().equals(idTel.toUpperCase())) {
                        nameTelText.setText(ds.child("nom_tel").getValue(String.class));
                        IdTelText.setText(ds.child("mac_tel").getValue(String.class));
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