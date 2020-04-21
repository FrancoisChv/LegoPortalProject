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

/* Association des ID Layout */
        button = findViewById(R.id.connectButton);
        macAddText = findViewById(R.id.MacAddPortalText);
        nameTelText = findViewById(R.id.NametelText);
        IdTelText = findViewById(R.id.IDtelText);

/* Récupération id utilisateur courrant et rechercher dans la base de donnée avec le chemin de rechercher */
        String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("Télécommandes").child(idUser).child("MacPortail");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
/* On vérifie si il existe une valeur enregistrer du MacPortail */
                if(dataSnapshot.getValue(String.class).equals("") || dataSnapshot.getValue(String.class) == null){
/* Si pas d'adresse, on rend le bouton de connexion invisible, et on affiche Aucune Adresse à l'utilisateur */
                            macAddText.setText("Aucune Adresse");
                    button.setVisibility(View.INVISIBLE);
                    return;
                }

/* Si y'a une adresse, on affiche sa valeur à l'utilisateur */
                        macAddText.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

/* On rend le bouton invisible de manière générale avant le 2ème traitement de la télécommande */
                button.setVisibility(View.INVISIBLE);

        showData();

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
/* Vérifie la validité pour entamer une connexion bluetooth en appelant ensuite la class MainActivity si c'est bon */
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

/* Retourne notre device_id */
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    private boolean validMacAdd(String macAdd) {
        return macAdd.length() == 0 || macAdd.length() == 17;
    }

    public void showData() {
        String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String idTel = getAndroidId();

        mDatabase2 = FirebaseDatabase.getInstance().getReference("Télécommandes").child(idUser).child("ListeTélécommandes");
        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
/* Parcours de la bdd */
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
/* Si la bdd contient une télécommande avec un device_id identique à la
télécommande que l'utilisateur est entrain d'utiliser alors on va cherche le nom de la
télécommande et son device_id */

                    if (ds.child("device_id").getValue().equals(idTel.toUpperCase())) {
                        nameTelText.setText(ds.child("nom_tel").getValue(String.class));
                        IdTelText.setText(ds.child("device_id_tel").getValue(String.class));

/* Puis on revérifie l'existance d'une adresse mac en vérifiant le texte de la zone de saisie, s'il n'est pas égal à aucune adresse, il y'a donc une adresse mac du portail. On a donc bien une télécommande enregristée, et une adresse mac du portail, on autorise la connexion bluetooth en affichant le bouton de connexion */
                        if (!macAddText.getText().toString().equals("Aucune Adresse")){
                            button.setVisibility(View.VISIBLE);
                            return;
                        }
                    }
                }

                nameTelText.setText("Votre Télécommande \n n'est pas enregistré");

            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w("tag", "Failed to read value.", error.toException());
            }
        });
    }
}