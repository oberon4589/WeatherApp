package com.codename.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FormLogin extends AppCompatActivity {

    private TextView textCadastrar;
    private EditText editEmail, editSenha;
    private Button btEntrar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        IniciarComponentes();

        textCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                startActivity(intent);
            }
        });

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(FormLogin.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    autenticarUsuario();
                }
            }
        });
    }

    private void autenticarUsuario() {
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(FormLogin.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            telaPrincipal();
                        }
                    }, 1500);
                }
            }
        });
    }

    private void telaPrincipal() {
        Intent intent = new Intent(FormLogin.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void IniciarComponentes() {
        textCadastrar = findViewById(R.id.textCadastrar);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        btEntrar = findViewById(R.id.btEntrar);
        progressBar = findViewById(R.id.progressBar);
    }
}