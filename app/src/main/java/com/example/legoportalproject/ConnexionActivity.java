package com.example.legoportalproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ConnexionActivity extends AppCompatActivity {

    public EditText emailId, passwd, accesscode;
    Button btnSignUp;
    TextView signIn;
    FirebaseAuth firebaseAuth;
    String code = "1234"; //Code d'accès a entrer pour valider l'inscription
    Vibrator vib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        //Lien avec le layotut
        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.ETemail);
        passwd = findViewById(R.id.ETpassword);
        accesscode = findViewById(R.id.ETaccesscode);
        btnSignUp = findViewById(R.id.btnSignUp);
        signIn = findViewById(R.id.TVSignIn);
        vib=(Vibrator)getSystemService(ConnexionActivity.VIBRATOR_SERVICE);

        //Lorsque l'utilisateur appuie sur le bouton "s'inscrire"
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vib.vibrate(10);

                //Récupération de l'adresse mail entrée par l'utilisateur
                String emailID = emailId.getText().toString();
                //Récupération du mot de passe entré par l'utilisateur
                String paswd = passwd.getText().toString();
                //Récupération du code d'accès entré par l'utilisateur
                String accescd = accesscode.getText().toString();

                if (emailID.isEmpty()) { //Vérification d'une adresse mail remplie
                    emailId.setError("Fournissez d'abord votre e-mail !");
                    emailId.requestFocus();
                } else if (paswd.isEmpty()) { //Vérification d'un mot de passe renseigné
                    passwd.setError("Définir votre mot de passe !");
                    passwd.requestFocus();
                } else if (accescd.isEmpty()) { //Vérification d'un code d'accès renseigné
                    accesscode.setError("Code d'accès requis !");
                    accesscode.requestFocus();
                } else if (!(accescd.equals(code))) { //Vérification que le code d'accès est correct
                    accesscode.setError("Code d'accès invalide !");
                    accesscode.requestFocus();
                } else if (emailID.isEmpty() && paswd.isEmpty() && accescd.isEmpty() && !(accescd.equals(code))) { //Si tous les champs sont vide et mauvais code d'accès
                    Toast.makeText(ConnexionActivity.this, "Champs vides !", Toast.LENGTH_SHORT).show();
                } else if (!(emailID.isEmpty() && paswd.isEmpty() && paswd.isEmpty()) && accescd.equals(code)) { //Si les champs sont renseignés avec le bon code d'accès
                    //Création d'un utilisateur avec les valeurs qu'il a renseigné
                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(ConnexionActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (!task.isSuccessful()) { //Création echoué
                                Toast.makeText(ConnexionActivity.this.getApplicationContext(),
                                        "Inscription infructueuse: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else { //Création réussie
                                //Redirection de l'utilisateur vers l'activité MenuActivity
                                startActivity(new Intent(ConnexionActivity.this, MenuActivity.class));
                            }
                        }
                    });
                } else {
                    //Message d'erreur pour l'utilisateur, création de compte échouée
                    Toast.makeText(ConnexionActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Lorsque l'utilisateur appuie sur "ici"
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vib.vibrate(10);
                //On redirige l'utilisateur vers l'activité LoginActivity
                Intent I = new Intent(ConnexionActivity.this, LoginActivity.class);
                startActivity(I);
            }
        });
    }
}
