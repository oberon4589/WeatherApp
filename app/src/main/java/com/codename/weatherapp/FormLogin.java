package com.codename.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.biometric.BiometricManager;

import android.content.Intent;
import android.os.Build;
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

import java.util.concurrent.Executor;

public class FormLogin extends AppCompatActivity {

    private TextView textCadastrar;
    private EditText editEmail, editSenha;
    private Button btEntrar, btnLoginBiometric;
    private ProgressBar progressBar;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        IniciarComponentes();

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                autenticarUsuarioBiometrico();
                progressBar.setVisibility(View.GONE);
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("Use sua biometria para entrar")
                .setNegativeButtonText("Cancelar")
                .build();

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

        btnLoginBiometric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BiometricManager biometricManager = BiometricManager.from(FormLogin.this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
                        biometricPrompt.authenticate(promptInfo);
                    } else {
                        Toast.makeText(FormLogin.this, "Não foi possível usar a biometria", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void autenticarUsuarioBiometrico() {
        telaPrincipal();
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
                } else {
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        Toast.makeText(FormLogin.this, "Erro ao efetuar login", Toast.LENGTH_SHORT).show();
                    }
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
        btnLoginBiometric = findViewById(R.id.btnLoginBiometric);
    }
}