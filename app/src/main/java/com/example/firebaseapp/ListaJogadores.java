package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class ListaJogadores extends AppCompatActivity {

    private ListView lvJogadores;
    private Button btnAdicionar;

    private List<Jogador> listaJogadores = new ArrayList<>();
    private ArrayAdapter adapter;

    FirebaseDatabase database;
    DatabaseReference reference;

    ChildEventListener childEventListener;
    Query query;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_jogadores);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        lvJogadores = findViewById(R.id.lvLista);
        btnAdicionar = findViewById(R.id.btnAdicionar);

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaJogadores.this, MainActivity.class);
                intent.putExtra("acao", "novo");
                startActivity(intent);

            }
        });

        lvJogadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Jogador jogadorSelecionado = listaJogadores.get(i);

                Intent intent = new Intent(ListaJogadores.this, MainActivity.class);
                intent.putExtra("acao", "editar");
                intent.putExtra("idJogador", jogadorSelecionado.getId());
                intent.putExtra("nomeCompleto", jogadorSelecionado.getNomeCompleto());
                intent.putExtra("nomeCamiseta", jogadorSelecionado.getNomeCamiseta());
                startActivity(intent);
            }
        });

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

    protected void onStart(){
        super.onStart();
        carregarJogadores();

        listaJogadores.clear();

        query = reference.child("jogadores").orderByChild("nomeCompleto");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try{
                    String idUser = snapshot.child("idUsuario").getValue(String.class);

                    if(idUser.equals(auth.getCurrentUser().getUid())) {
                        Jogador j = new Jogador();
                        j.setId(snapshot.getKey());
                        j.setNomeCompleto(snapshot.child("nomeCompleto").getValue(String.class));
                        j.setNomeCamiseta(snapshot.child("nomeCamiseta").getValue(String.class));
                        j.setIdUsuario(idUser);
                        listaJogadores.add(j);
                        adapter.notifyDataSetChanged();
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String idJogador = snapshot.getKey();
                for(Jogador j : listaJogadores){
                    if(j.getId().equals(idJogador)){
                        j.setNomeCompleto(snapshot.child("nomeCompleto").getValue(String.class));
                        j.setNomeCamiseta(snapshot.child("nomeCamiseta").getValue(String.class));
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String idJogador = snapshot.getKey();
                for(Jogador j : listaJogadores){
                    if(j.getId().equals(idJogador)){
                        listaJogadores.remove(j);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addChildEventListener(childEventListener);
    }

    protected void onStop(){
        super.onStop();
        query.removeEventListener(childEventListener);
    }

    private void carregarJogadores(){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaJogadores);
        lvJogadores.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuSair){
            auth.signOut();
        }
        return super.onOptionsItemSelected(item);

    }
}