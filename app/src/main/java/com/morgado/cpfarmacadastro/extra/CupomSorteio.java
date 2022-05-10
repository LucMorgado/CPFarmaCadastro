package com.morgado.cpfarmacadastro.extra;

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
import android.widget.TextView;

import com.morgado.cpfarmacadastro.R;

import java.util.Random;

import classes.Dados;
import classes.PrinterBT;
import classes.Tela;

public class CupomSorteio extends AppCompatActivity {
    String codigo, nome, telefone, email;
    Button next;
    Handler handler = new Handler();
    boolean click = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupom_sorteio);

        Tela.telaCheia(getWindow().getDecorView());

        PrinterBT printerBT = new PrinterBT(getApplicationContext());

        //verificar se tem cadastro em andamento
        if(getIntent().hasExtra("codigo")) {
            codigo = getIntent().getExtras().getString("codigo");
            telefone = getIntent().getExtras().getString("telefone");
            nome = getIntent().getExtras().getString("nome");
            email = getIntent().getExtras().getString("email");
        }

        next = findViewById(R.id.btImprimir2);
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
                    Bitmap sorteio = BitmapFactory.decodeResource(getResources(), R.drawable.sorteio);
                    Bitmap imgCodigo = printerBT.desenharCodigo(getResources(), codigo);
                    printerBT.printSorteio(logo, sorteio, imgCodigo, nome, telefone);
                }catch (Exception e){
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
                TextView txt = findViewById(R.id.txtSorteio);
                fadeOut.setPropertyName("alpha");
                fadeOut.setFloatValues(1f,0f);
                fadeOut.setTarget(txt);
                fadeOut.start();
                txt.setText("RETIRE O SEU CUPOM DO SORTEIO");
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