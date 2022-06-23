package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText etNomeCompleto, etNomeCamiseta;
    private Button btnSalvar;
    private String acao;
    private Jogador jogador;

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNomeCompleto = findViewById(R.id.etNomeCompleto);
        etNomeCamiseta = findViewById(R.id.etNomeCamiseta);
        btnSalvar = findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvar();
            }
        });

        acao = getIntent().getExtras().getString("acao");
        if (acao.equals("editar")){
            jogador = new Jogador();
            jogador.setId(getIntent().getExtras().getString("idJogador"));
            etNomeCompleto.setText(getIntent().getExtras().getString("nomeCompleto"));
            etNomeCamiseta.setText(getIntent().getExtras().getString("nomeCamiseta"));
        }

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if( auth.getCurrentUser() == null ){
                    finish();
                }
            }
        };
        auth.addAuthStateListener( authStateListener );
    }

    private void salvar(){
        if(acao.equals("novo")){
            jogador = new Jogador();
        }

        String nomeCompleto = etNomeCompleto.getText().toString();
        String nomeCamiseta = etNomeCamiseta.getText().toString();

        if( !nomeCompleto.isEmpty() && !nomeCamiseta.isEmpty()){
            jogador.setNomeCompleto(nomeCompleto);
            jogador.setNomeCamiseta(nomeCamiseta);
            jogador.setIdUsuario(auth.getCurrentUser().getUid());

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference();

            if(acao.equals("novo")){
                reference.child("jogadores").push().setValue(jogador);
            }else{
                reference.child("jogadores").child(jogador.getId()).setValue(jogador);
            }

            finish();
        }
    }
}