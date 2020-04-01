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

public class MainActivity extends AppCompatActivity {

    public EditText emailId, passwd, accesscode;
    Button btnSignUp;
    TextView signIn;
    FirebaseAuth firebaseAuth;
    String code = "1234";
    Vibrator vib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.ETemail);
        passwd = findViewById(R.id.ETpassword);
        accesscode = findViewById(R.id.ETaccesscode);
        btnSignUp = findViewById(R.id.btnSignUp);
        signIn = findViewById(R.id.TVSignIn);
        vib=(Vibrator)getSystemService(MainActivity.VIBRATOR_SERVICE);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vib.vibrate(10);
                String emailID = emailId.getText().toString();
                String paswd = passwd.getText().toString();
                String accescd = accesscode.getText().toString();
                if (emailID.isEmpty()) {
                    emailId.setError("Fournissez d'abord votre e-mail !");
                    emailId.requestFocus();
                } else if (paswd.isEmpty()) {
                    passwd.setError("Définir votre mot de passe !");
                    passwd.requestFocus();
                } else if (accescd.isEmpty()) {
                    accesscode.setError("Code d'accès requis !");
                    accesscode.requestFocus();
                } else if (!(accescd.equals(code))) {
                    accesscode.setError("Code d'accès invalide !");
                    accesscode.requestFocus();
                } else if (emailID.isEmpty() && paswd.isEmpty() && accescd.isEmpty() && !(accescd.equals(code))) {
                    Toast.makeText(MainActivity.this, "Champs vides !", Toast.LENGTH_SHORT).show();
                } else if (!(emailID.isEmpty() && paswd.isEmpty() && paswd.isEmpty()) && accescd.equals(code)) {
                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(MainActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this.getApplicationContext(),
                                        "Inscription infructueuse: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(MainActivity.this, UserActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vib.vibrate(10);
                Intent I = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(I);
            }
        });
    }
}
