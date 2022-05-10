package com.morgado.cpfarmacadastro.extra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.morgado.cpfarmacadastro.MainActivity;
import com.morgado.cpfarmacadastro.R;
import com.morgado.cpfarmacadastro.caixas.NomeActivity;

import java.util.Timer;

import classes.Alerta;
import classes.Dados;
import classes.Tela;

public class AgradecimetoActivity extends AppCompatActivity {
    String codigo, nome;
    TextView txtComNome;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agradecimeto);

        txtComNome = findViewById(R.id.txtComNome);

        Tela.telaCheia(getWindow().getDecorView());

        //verificar se tem cadastro em andamento
        codigo = "0";
        if(getIntent().hasExtra("codigo")) {
            nome = getIntent().getExtras().getString("nome");
            String[] nomes = nome.split(" ");
            nome = nomes[0].toLowerCase();
            nome = nome.substring(0,1).toUpperCase().concat(nome.substring(1));
            txtComNome.setText("OBRIGADO, " + nome + "!");
        }

        ObjectAnimator fadeIn = new ObjectAnimator();
                fadeIn.setDuration(500);
                fadeIn.setPropertyName("alpha");
                fadeIn.setFloatValues(0f,1f);
                fadeIn.setTarget(txtComNome);
                fadeIn.start();

        handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                ActivityOptionsCompat aoc = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), R.anim.fade_in,R.anim.fade_out);
                startActivity(intent, aoc.toBundle());
                finish();
        }
        }, 5000);




    }
}