package com.codename.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormCadastro extends AppCompatActivity {

    private EditText edit_nome, edit_email, edit_senha;
    private Button bt_cadastrar;
    String[] mensagens = {"Preencha todos os campos", "Cadastro realizado com sucesso"};
    String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);

        iniciarComponentes();

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = edit_nome.getText().toString();
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();

                if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(FormCadastro.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    CadastrarUsuario();
                }
            }
        });
    }

    private void CadastrarUsuario() {
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    SalvarDadosUsuario();
                    Toast.makeText(FormCadastro.this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String erro;
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "A senha deve ter no mínimo 6 caracteres";

                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "Esta conta já foi cadastrada";

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "E-mail inválido";

                    } catch (Exception e) {
                        erro = "Erro ao cadastrar usuário";
                        e.printStackTrace();
                    }
                    Toast.makeText(FormCadastro.this, erro, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SalvarDadosUsuario() {
        String nome = edit_nome.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome", nome);
        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuarios").document(usuarioId);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db", "Sucesso ao salvar os dados do usuário");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db", "Erro ao salvar os dados" + e.toString());
            }
        });
    }

    private void iniciarComponentes() {
        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.editSenha);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);
    }
}