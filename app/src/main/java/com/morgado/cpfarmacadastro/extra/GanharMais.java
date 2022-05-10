package com.morgado.cpfarmacadastro.extra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.morgado.cpfarmacadastro.R;

import java.util.Random;

import classes.PrinterBT;
import classes.Tela;

public class GanharMais extends AppCompatActivity {
    String codigo, nome, telefone, email;
    Button next;
    TextView finalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganhar_mais);

        Tela.telaCheia(getWindow().getDecorView());

        //verificar se tem cadastro em andamento
        int ale = new Random().nextInt(10) * 5032;
        codigo = Integer.toString(ale);
        if(getIntent().hasExtra("codigo")) {
            codigo = getIntent().getExtras().getString("codigo");
            nome = getIntent().getExtras().getString("nome");
            telefone = getIntent().getExtras().getString("telefone");
            email = getIntent().getExtras().getString("email");
        }

        next = findViewById(R.id.btGanharMais2);
        finalizar = findViewById(R.id.btFinalizar);

       next.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RoletaActivity.class);
                intent.putExtra("codigo", codigo);
                intent.putExtra("nome", nome);
                intent.putExtra("telefone", telefone);
                intent.putExtra("email", email);
                ActivityOptionsCompat aoc = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), R.anim.fade_in,R.anim.fade_out);
                startActivity(intent, aoc.toBundle());
           }
       });

        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AgradecimetoActivity.class);
                intent.putExtra("codigo", codigo);
                intent.putExtra("nome", nome);
                ActivityOptionsCompat aoc = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), R.anim.fade_in,R.anim.fade_out);
                startActivity(intent, aoc.toBundle());
                finish();
            }
        });
    }
}