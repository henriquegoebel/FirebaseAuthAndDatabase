package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private Button btnEntrar, btnCadastro;

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnCadastro = findViewById(R.id.btnCadastro);

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrar();
            }
        });

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                usuario = auth.getCurrentUser();
                if( usuario != null ){
                    Intent intent = new Intent(Login.this, ListaJogadores.class);
                    startActivity(intent);
                }
            }
        };
        auth.addAuthStateListener( authStateListener );
    }

    private void cadastrar(){
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();
        if( email.isEmpty() || senha.isEmpty() ){
            Toast.makeText(this, "Todos os campos são obrigatórios!", Toast.LENGTH_LONG).show();
        }else{
            auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if( task.isSuccessful() ){
                                usuario = auth.getCurrentUser();
                            }else{
                                String exception = task.getException().toString();
                                Toast.makeText(Login.this, exception, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void entrar(){
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();
        if( email.isEmpty() || senha.isEmpty() ){
            Toast.makeText(this, "Todos os campos são obrigatórios!", Toast.LENGTH_LONG).show();
        }else{
            auth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if( task.isSuccessful() ){
                                usuario = auth.getCurrentUser();
                            }else{
                                task.getException().toString();
                                Toast.makeText(Login.this, "Erro ao fazer o login!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}