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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    public EditText loginEmailId, logInpasswd;
    Button btnLogIn;
    TextView signup;
    FirebaseAuth firebaseAuth;
    Vibrator vib;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Lien avec la base de données
        firebaseAuth = FirebaseAuth.getInstance();
        //Lien avec le layout
        loginEmailId = findViewById(R.id.loginEmail);
        logInpasswd = findViewById(R.id.loginpaswd);
        btnLogIn = findViewById(R.id.btnLogIn);
        signup = findViewById(R.id.TVSignIn);
        vib = (Vibrator) getSystemService(ConnexionActivity.VIBRATOR_SERVICE);



        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) { //Connexion acceptée
                    Toast.makeText(LoginActivity.this, "Utilisateur connecté", Toast.LENGTH_SHORT).show();
                    //On redirige l'utilisateur vers l'activité MenuActivity
                    Intent I = new Intent(LoginActivity.this, MenuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("mail", String.valueOf(loginEmailId.getText()));
                    I.putExtras(bundle);
                    startActivity(I);
                } else { //Connexion refusée
                    //Message d'erreur pour l'utilisateur
                    Toast.makeText(LoginActivity.this, "Connectez-vous pour continuer", Toast.LENGTH_SHORT).show();
                }
            }
        };

        //Lorsque l'utilisateur appuie sur "ici"
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vib.vibrate(10);
                //On redirige l'utilisateur sur l'activité ConnexionActivity
                Intent I = new Intent(LoginActivity.this, ConnexionActivity.class);
                startActivity(I);
            }
        });
        //Lorsque l'utilisateur appuie sur le bouton "Se connecter"
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vib.vibrate(10);
                //Récupération des valeurs renseignées par l'utilisateur
                String userEmail = loginEmailId.getText().toString();
                String userPaswd = logInpasswd.getText().toString();
                if (userEmail.isEmpty()) { //Si l'adresse mail est vide
                    //Message d'erreur pour l'utilisateur
                    loginEmailId.setError("Fournissez d'abord votre e-mail !");
                    loginEmailId.requestFocus();
                } else if (userPaswd.isEmpty()) { //Si le mot de passe est vide
                    //Message d'erreur pour l'utilisateur
                    logInpasswd.setError("Entrer un mot de passe !");
                    logInpasswd.requestFocus();
                } else if (userEmail.isEmpty() && userPaswd.isEmpty()) { //Si tous les champs sont vides
                    //Message d'erreur pour l'utilisateur
                    Toast.makeText(LoginActivity.this, "Champs vides !", Toast.LENGTH_SHORT).show();
                } else if (!(userEmail.isEmpty() && userPaswd.isEmpty())) { //Si les champs sont renseignés
                    //Test de connexion avec les valeurs renseignées par l'utilisateur
                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPaswd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (!task.isSuccessful()) { //Connexion ratée
                                //Message d'erreur pour l'utilisateur
                                Toast.makeText(LoginActivity.this, "Sans succès", Toast.LENGTH_SHORT).show();
                            } else { //Connexion réussie
                                //Redirection de l'utilisateur vers l'activité MenuActivity
                                Intent I = new Intent(LoginActivity.this, MenuActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("mail", String.valueOf(loginEmailId.getText()));
                                I.putExtras(bundle);
                                startActivity(I);
                            }
                        }
                    });
                } else {
                    //Message d'erreur pour l'utilisateur
                    Toast.makeText(LoginActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}

