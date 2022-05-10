package com.morgado.cpfarmacadastro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.morgado.cpfarmacadastro.caixas.NomeActivity;
import com.morgado.cpfarmacadastro.extra.AgradecimetoActivity;

import java.util.Timer;
import java.util.TimerTask;

import classes.Alerta;
import classes.Dados;
import classes.PrinterBT;
import classes.Tela;

public class MainActivity extends AppCompatActivity {

    private Button btIniciar;
    private String codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tela.telaCheia(getWindow().getDecorView());

        Dados dados = new Dados();
        DatabaseReference ult = dados.ultimo;
        ult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                codigo = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        PrinterBT printerBT = new PrinterBT(getApplicationContext());
        printerBT.setCenter();

        btIniciar = findViewById(R.id.btIniciar);
        btIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dados.novo(codigo);
                int novo = Integer.parseInt(codigo) + 1;
                String novoCodigo = novo + "";
                Intent intent = new Intent(getApplicationContext(), NomeActivity.class);
                intent.putExtra("codigo", novoCodigo);
                //startActivity(intent);
                ActivityOptionsCompat aoc = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), R.anim.fade_in,R.anim.fade_out);
                startActivity(intent, aoc.toBundle());
            }
        });

    }

}