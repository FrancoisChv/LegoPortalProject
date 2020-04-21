package com.example.legoportalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Currency;

public class ParametersActivity extends AppCompatActivity {

    public String mail;
    TextView Mac1, Mac2, Mac3, Tel1, Tel2, Tel3;
    EditText macPortal;
    Button delete1, delete2, delete3, modifMacPortal, delete_user;

    DatabaseReference mDatabase;
    DatabaseReference mDatabase2;
    DatabaseReference mDatabase3;
    Task<Void> mDatabase4;

    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;

    static Integer nb = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);

        //Lien avec le layout
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
        delete_user = findViewById(R.id.delete_user_btn);

        //Permet de rendre les éléments invisibles pour le moment
        Mac1.setVisibility(View.INVISIBLE);
        Mac2.setVisibility(View.INVISIBLE);
        Mac3.setVisibility(View.INVISIBLE);
        Tel1.setVisibility(View.INVISIBLE);
        Tel2.setVisibility(View.INVISIBLE);
        Tel3.setVisibility(View.INVISIBLE);
        delete1.setVisibility(View.INVISIBLE);
        delete2.setVisibility(View.INVISIBLE);
        delete3.setVisibility(View.INVISIBLE);

        //Déclaration des variables en lien avec la base de donnée
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();


        Intent I = getIntent();
        if (I.hasExtra("mail")) {
            mail = I.getStringExtra("mail");
        }

        //Récupération de l'identifiant utilisateur
        String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //On spécifie le chemin à parcourir en base de données
        mDatabase = FirebaseDatabase.getInstance().getReference("Télécommandes").child(idUser).child("MacPortail");
        //Parcours de la base de données
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //On récupère la valeur de l'adresse mac du portail dans la base de données pour l'afficher
                macPortal.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Lorsque l'utilisateur appuie sur le bouton "valider modification"
        modifMacPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mac = macPortal.getText().toString();
                //On appel la fonction validMacAdd pour vérifier la saisie utilisateur
                if(!validMacAdd(mac)){ //Adresse mac non valide
                    // Notify user to enter mac address of brick
                    AlertDialog.Builder builder = new AlertDialog.Builder(ParametersActivity.this);
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
                    //On enregistre l'adresse mac en base de données
                    mDatabase.setValue(mac);
                    //On redirige l'utilisateur vers l'activité MenuActivity
                    Intent I = new Intent(ParametersActivity.this, MenuActivity.class);
                    startActivity(I);
                }
            }
        });

        showData();

        //Lorsque l'utilisateur appuie sur le bouton supprimer de sa première télécommande
        delete1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Appel de la méthode deleteData avec 1 comme paramètre (1ère télécommande)
                deleteData(1);
                //On redirige l'utilisateur vers l'activité MenuActivity
                Intent I = new Intent(ParametersActivity.this, MenuActivity.class);
                startActivity(I);
            }
        });

        //Lorsque l'utilisateur appuie sur le bouton supprimer de sa deuxième télécommande
        delete2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Appel de la méthode deleteData avec 2 comme paramètre (2ème télécommande)
                deleteData(2);
                //On redirige l'utilisateur vers l'activité MenuActivity
                Intent I = new Intent(ParametersActivity.this, MenuActivity.class);
                startActivity(I);
            }
        });

        //Lorsque l'utilisateur appuie sur le bouton supprimer de sa troisième télécommande
        delete3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Appel de la méthode deleteData avec 3 comme paramètre (3ème télécommande)
                deleteData(3);
                //On redirige l'utilisateur vers l'activité MenuActivity
                Intent I = new Intent(ParametersActivity.this, MenuActivity.class);
                startActivity(I);
            }
        });

        //Lorsque l'utilisateur appuie sur le bouton "supprimer définitivement le compte"
        delete_user.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ParametersActivity.this);
                //Message d'alerte pour prévenir l'utilisateur
                dialog.setTitle("Are you sure ?");
                dialog.setMessage("Deleting this account wil result in completely removing your account from the system.");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String idUser  = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        //Suppresion de l'utilisateur et de ses télécommandes de la base de données
                        mDatabase4 = FirebaseDatabase.getInstance().getReference("Télécommandes").child(idUser).removeValue();
                        mFirebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    //Message de validation de suppression
                                    Toast.makeText(ParametersActivity.this, "Account Deleted", Toast.LENGTH_LONG).show();
                                    //On redirige l'utilisateur vers l'activité LoginActivity
                                    Intent I = new Intent(ParametersActivity.this, LoginActivity.class);
                                    I.addFlags(I.FLAG_ACTIVITY_CLEAR_TOP);
                                    I.addFlags(I.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(I);
                                } else {
                                    //Message d'erreur de suppression
                                    Toast.makeText(ParametersActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });


    }

    //Vérification de la validité de l'adresse mac
    public boolean validMacAdd(String macAdd) {
        // Longueur de 17 caractère ou adresse mac vide
        return macAdd.length() == 17 || macAdd.length() == 0;
    }

    //Méthode pour aller chercher les informations des télécommandes de l'utilisateur
    public void showData() {
        nb = 0;
        String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Détail du chemin à parcourir en base de données
        mDatabase2 = FirebaseDatabase.getInstance().getReference("Télécommandes").child(idUser).child("ListeTélécommandes");
        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    nb = nb+ 1; //Compteur de télécommande appartenant à l'utilisateur
                    if (nb == 1) { //Si c'est la première télécommande de l'utilisateur
                        //On affiche dans les TextView le device_id et le nom_tel de la télécommande
                        Mac1.setText(ds.child("device_id_tel").getValue(String.class));
                        Tel1.setText(ds.child("nom_tel").getValue(String.class));
                        //On rend les textView ainsi que le bouton de suppression de la télécommande visible
                        Mac1.setVisibility(View.VISIBLE);
                        Tel1.setVisibility(View.VISIBLE);
                        delete1.setVisibility(View.VISIBLE);
                    }
                    if (nb == 2) { //Si c'est la deuxième télécommande de l'utilisateur
                        //On affiche dans les TextView le device_id et le nom_tel de la télécommande
                        Mac2.setText(ds.child("device_id_tel").getValue(String.class));
                        Tel2.setText(ds.child("nom_tel").getValue(String.class));
                        //On rend les textView ainsi que le bouton de suppression de la télécommande visible
                        Mac2.setVisibility(View.VISIBLE);
                        Tel2.setVisibility(View.VISIBLE);
                        delete2.setVisibility(View.VISIBLE);
                    }
                    if (nb == 3) { //Si c'est la troisième télécommande de l'utilisateur
                        //On affiche dans les TextView le device_id et le nom_tel de la télécommande
                        Mac3.setText(ds.child("device_id_tel").getValue(String.class));
                        Tel3.setText(ds.child("nom_tel").getValue(String.class));
                        //On rend les textView ainsi que le bouton de suppression de la télécommande visible
                        Mac3.setVisibility(View.VISIBLE);
                        Tel3.setVisibility(View.VISIBLE);
                        delete3.setVisibility(View.VISIBLE);
                    }
                    //Le nombre de télécommande est limité à 3 par utilisateur, pas d'autres affichages possibles
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w("tag", "Failed to read value.", error.toException());
            }
        });
    }

    //Méthode pour supprimer une télécommande de la base de données
    public void deleteData(final Integer a){
        nb =0;
        String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Chemin pour parcourir la base de données
        mDatabase3 = FirebaseDatabase.getInstance().getReference("Télécommandes").child(idUser).child("ListeTélécommandes");
        mDatabase3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    nb = nb + 1; //Compteur des télécommandes de l'utilisateur
                    if (a == nb) { //Si la télécommande correspond à celle que l'utilisateur veut supprimer
                        ds.getRef().removeValue(); //Suppression de la télécommande
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