package com.example.legoportalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ConnectActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences(getString(R.string.MyPREFERENCES), Context.MODE_PRIVATE);
        final Button button = (Button) findViewById(R.id.connectButton);
        final EditText macAddText = (EditText) findViewById(R.id.editMacAddText);
        macAddText.setText(sharedpreferences.getString(getString(R.string.EV3KEY),""));
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
                    Intent intent = new Intent(ConnectActivity.this, ConnexionActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validMacAdd(String macAdd) {
        return macAdd.length() == 17;
    }
}
