package com.example.legoportalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ComBluetooth CommunicationBT;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rc);

        CommunicationBT = new ComBluetooth();

        // To notify user for    permission to enable bt, if needed
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(R.string.bt_permission_request);
        builder.setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                CommunicationBT.setBtPermission(true);
                CommunicationBT.reply();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                CommunicationBT.setBtPermission(false);
                CommunicationBT.reply();
            }
        });

        // Create the AlertDialog
        AlertDialog btPermissionAlert = builder.create();
        Context context = getApplicationContext();
        CharSequence text1 = getString(R.string.bt_disabled);
        CharSequence text2 = getString(R.string.bt_failed);
        Toast btDisabledToast = Toast.makeText(context, text1, Toast.LENGTH_LONG);
        Toast btFailedToast = Toast.makeText(context, text2, Toast.LENGTH_LONG);

        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.MyPREFERENCES), Context.MODE_PRIVATE);
        if (!CommunicationBT.initBT()) {
            // User did not enable Bluetooth
            btDisabledToast.show();
            Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
            startActivity(intent);
        }

        if (!CommunicationBT.connectToEV3(sharedpreferences.getString(getString(R.string.EV3KEY), ""))) {
            //Cannot connect to given mac address, return to connect activity
            btFailedToast.show();
            Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
            startActivity(intent);
        }

        final Button buttonT = (Button) findViewById(R.id.buttonT);
        buttonT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    try {
                        CommunicationBT.writeMessage((byte) 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

        final Button buttonP = (Button) findViewById(R.id.buttonP);
        buttonP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    CommunicationBT.writeMessage((byte) 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button buttonQuitter = (Button) findViewById(R.id.buttonQuitter);
        buttonQuitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CommunicationBT.writeMessage((byte) 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "Déconnexion de la télécommande au portail.", Toast.LENGTH_SHORT).show();
                Intent I = new Intent(MainActivity.this, MenuActivity.class);
                I.addFlags(I.FLAG_ACTIVITY_CLEAR_TOP);
                I.addFlags(I.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(I);
            }
        });

    }
}
