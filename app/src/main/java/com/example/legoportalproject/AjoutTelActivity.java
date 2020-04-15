package com.example.legoportalproject;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    TextView mac_text;
    DatabaseReference mDatabase;
    private String mail;
    ArrayList<String> ListUser = new ArrayList<String>();



    FirebaseAuth firebaseAuth;
    private String SerialNumber;
    
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

                mac_text.setText(getAndroidId());
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
        ListUser.add("idUser");

       /* mDatabase.child(mac).child("MAC").setValue(mac);
        mDatabase.child(mac).child("NOM").setValue(nom);
        mDatabase.child(mac).child("MAIL").setValue(mail);*/

    }

}