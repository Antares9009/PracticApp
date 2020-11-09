package com.antares.practicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText editTextEmail,editTextPassword;
    MaterialButton btnLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

    }

    private void validate(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Correo invalido");
            return;
        }else{
            editTextEmail.setError(null);
        }

        if(password.isEmpty()||password.length() < 8){
            editTextPassword.setError("Debe de ser mayor a 8 caracteres");
            return;
        }else if (!Pattern.compile("[0-9]").matcher(password).find()){
            editTextPassword.setError("Debe contener al menos un numero");
            return;
        }else{
            editTextPassword.setError(null);
        }

        loginSesion(email,password);
    }

    private void loginSesion(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this,UserActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this,"El usuario o contrasena son incorrectos. Intenta de nuevo",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}