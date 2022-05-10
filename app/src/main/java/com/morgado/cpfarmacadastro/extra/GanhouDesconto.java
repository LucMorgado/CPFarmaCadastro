package com.morgado.cpfarmacadastro.extra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.morgado.cpfarmacadastro.R;

import java.lang.reflect.Array;
import java.util.Random;

import classes.Alerta;
import classes.Dados;
import classes.MaskEditUtil;
import classes.PrinterBT;
import classes.Tela;

public class GanhouDesconto extends AppCompatActivity {
    String codigo, nome, telefone, email;;
    Button next;
    boolean click = false;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganhou_desconto);

        Tela.telaCheia(getWindow().getDecorView());

        //verificar se tem cadastro em andamento
        if(getIntent().hasExtra("codigo")) {
            codigo = getIntent().getExtras().getString("codigo");
            email = getIntent().getExtras().getString("email");
        }
        //adicionar desconto
        Dados dados = new Dados();
        dados.addDesconto(email, "30");
        DatabaseReference usuario = dados.usuarios.child(dados.lucasTexto(email));


         usuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    nome =      snapshot.child("nome").getValue().toString();
                    telefone =  snapshot.child("telefone").getValue().toString();
                }catch (Exception e){
                    Alerta.mensagem(getApplicationContext(), "Ocorreu um erro");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         PrinterBT printerBT = new PrinterBT(getApplicationContext());

        next = findViewById(R.id.btImprimir1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(click) return;
                click = true;
                Intent intent = new Intent(getApplicationContext(), GanharMais.class);
                intent.putExtra("codigo", codigo);
                intent.putExtra("nome", nome);
                intent.putExtra("telefone", telefone);
                intent.putExtra("email", email);

                try {
                    Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                    Bitmap desconto = BitmapFactory.decodeResource(getResources(), R.drawable.trinta);
                    Bitmap imgCodigo = printerBT.desenharCodigo(getResources(), codigo);
                    printerBT.printDesconto(logo, desconto, imgCodigo, nome, telefone);
                }catch (Exception e){
                    Alerta.mensagem(getApplicationContext(), "Erro na impressora");
                    e.printStackTrace();
                }

                ObjectAnimator fadeOut = new ObjectAnimator();
                fadeOut.setDuration(500);
                fadeOut.setPropertyName("alpha");
                fadeOut.setFloatValues(1f,0f);
                fadeOut.setTarget(next);
                fadeOut.start();
                fadeOut = new ObjectAnimator();
                fadeOut.setDuration(500);
                TextView txt = findViewById(R.id.textView16);
                fadeOut.setPropertyName("alpha");
                fadeOut.setFloatValues(1f,0f);
                fadeOut.setTarget(txt);
                fadeOut.start();
                txt.setText("RETIRE O SEU CUPOM DE DESCONTO");
                fadeOut = new ObjectAnimator();
                fadeOut.setDuration(500);
                fadeOut.setPropertyName("alpha");
                fadeOut.setFloatValues(0f,1f);
                fadeOut.setTarget(txt);
                fadeOut.start();
                fadeOut = new ObjectAnimator();
                fadeOut.setDuration(500);
                fadeOut.setStartDelay(6000);
                fadeOut.setPropertyName("alpha");
                fadeOut.setFloatValues(1f,0f);
                fadeOut.setTarget(txt);
                fadeOut.start();


                handler.postDelayed(new Runnable() {
                    public void run() {
                        try {
                        printerBT.disconnectBT();

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        ActivityOptionsCompat aoc = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), R.anim.fade_in,R.anim.fade_out);
                        startActivity(intent, aoc.toBundle());
                        finish();
                }
                }, 7000);

            }
        });
    }
}