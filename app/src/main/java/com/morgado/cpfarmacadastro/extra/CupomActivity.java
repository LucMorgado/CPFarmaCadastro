package com.morgado.cpfarmacadastro.extra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.TextureView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.morgado.cpfarmacadastro.R;

import java.util.Locale;

import classes.Alerta;
import classes.Tela;

public class CupomActivity extends AppCompatActivity {

    private TextView nome, id, desconto;
    private String codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupom);

        nome = findViewById(R.id.txtNome);
        id = findViewById(R.id.txtCodigo);
        desconto = findViewById(R.id.txtTotal);

        Tela.telaCheia(getWindow().getDecorView());

        //verificar se tem cadastro em andamento
        codigo = "0";
        if(getIntent().hasExtra("codigo")) {
            codigo = getIntent().getExtras().getString("codigo");
            Alerta.mensagem(getApplicationContext(), "TEM EXTRA " + codigo);
        }
      //FIREBASE CONECTION
        FirebaseDatabase database   = FirebaseDatabase.getInstance();
        DatabaseReference usuario = database.getReference().child("usuarios").child(codigo);

        usuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                id.setText(snapshot.getKey());
                nome.setText(snapshot.child("nome").getValue().toString());
                desconto.setText(snapshot.child("desconto").getValue().toString() + "%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}