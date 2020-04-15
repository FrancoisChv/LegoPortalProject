package com.example.legoportalproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.provider.Settings;
import android.provider.Settings.System;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AjoutTelActivity extends AppCompatActivity {


    private static final String REQUIRED = "Required";
    private static BluetoothAdapter BA;

    Button valide_btn, rmpl_auto_btn;
    EditText nom_tel_text;
    TextView mac_text, canAdd;
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
        canAdd = findViewById(R.id.canAdd_txt);
        canAdd.setVisibility(View.INVISIBLE);

        Intent I = getIntent();
        if (I.hasExtra("mail")) {
            mail = I.getStringExtra("mail");
        }


        String idUser  = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("Télécommandes").child(idUser).child("ListeTélécommandes");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer result = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    result = result + 1;
                }
                canAdd.setText(result.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("tag", "Failed to read value.", error.toException());
            }
        });

        rmpl_auto_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mac_text.setText(getAndroidId());
            }
        });

        valide_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (canAdd.getText().toString().equals("3")) {
                    Toast.makeText(AjoutTelActivity.this, "Vous ne pouvez ajouter d'autres télécommandes", Toast.LENGTH_SHORT).show();
                } else {
                    submitTelecommande();
                    Intent I = new Intent(AjoutTelActivity.this, MenuActivity.class);
                    startActivity(I);
                }

            }
        });

    }



    public String getAndroidId() {

         return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


    private void submitTelecommande() {

        final String nom = nom_tel_text.getText().toString();
        final String mac = mac_text.getText().toString();

        String idUser  = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

        Telecommande tel = new Telecommande(nom, mac);
        mDatabase =  FirebaseDatabase.getInstance().getReference().child("Télécommandes").child(idUser).child("ListeTélécommandes");
        mDatabase.push().setValue(tel);

    }

}