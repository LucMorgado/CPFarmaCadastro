package com.morgado.cpfarmacadastro.extra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.morgado.cpfarmacadastro.R;

import java.util.Random;

import classes.Alerta;
import classes.Dados;
import classes.PrinterBT;
import classes.Sorteio;
import classes.Tela;

public class RoletaActivity extends AppCompatActivity {

    private ImageView base, result;
    boolean rodou = false;
    private  String codigo, nome, telefone, email;
    private Handler handler = new Handler();
    private int descontoBit;
    private Intent  intent;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roleta);

        Tela.telaCheia(getWindow().getDecorView());

        //verificar se tem cadastro em andamento
        codigo = "0";
        if(getIntent().hasExtra("codigo")) {
            codigo = getIntent().getExtras().getString("codigo");
            telefone = getIntent().getExtras().getString("telefone");
            nome = getIntent().getExtras().getString("nome");
            email = getIntent().getExtras().getString("email");
        }

        ImageView roleta = findViewById(R.id.roleta);
        base   = findViewById(R.id.baseRoleta);
        result = findViewById(R.id.imgResult);

        PrinterBT printerBT = new PrinterBT(getApplicationContext());

        base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rodou) return;
                rodou = true;
                int descontoExtra = Sorteio.sortear();
                //animacao roleta
                ObjectAnimator rodar = new ObjectAnimator();
                rodar.setTarget(roleta);
                rodar.setPropertyName("rotation");
                rodar.setDuration(5000);
                switch (descontoExtra){
                    case 50:
                        rodar.setFloatValues(0f, 1080f);
                        result.setImageDrawable(getDrawable(R.drawable.ic_mais_50_gold));
                        descontoBit = R.drawable.mais_50;
                        break;
                    case 30:
                        rodar.setFloatValues(0f,1910f);
                        result.setImageDrawable(getDrawable(R.drawable.ic_mais_30_gold));
                        descontoBit = R.drawable.mais_30;
                        break;
                    default:
                        rodar.setFloatValues(0f, 1460f);
                        result.setImageDrawable(getDrawable(R.drawable.ic_mais_20_gold));
                        descontoBit = R.drawable.mais_20;
                        break;
                }
                rodar.start();

                //sumir roleta e base
                ObjectAnimator sumir = new ObjectAnimator();
                sumir.setTarget(roleta);
                sumir.setPropertyName("alpha");
                sumir.setDuration(500);
                sumir.setFloatValues(1f,0f);
                sumir.setStartDelay(5000);
                ObjectAnimator sumir2 = new ObjectAnimator();
                sumir2.setTarget(base);
                sumir2.setPropertyName("alpha");
                sumir2.setDuration(500);
                sumir2.setFloatValues(1f,0f);
                sumir2.setStartDelay(5000);
                sumir.start();
                sumir2.start();

                //mostrar resultado
                ObjectAnimator mostarAlpha = new ObjectAnimator();
                mostarAlpha.setTarget(result);
                mostarAlpha.setPropertyName("alpha");
                mostarAlpha.setDuration(10);
                mostarAlpha.setFloatValues(0f,1f);
                mostarAlpha.setStartDelay(5000);
                mostarAlpha.start();
                ObjectAnimator fadeReultado = new ObjectAnimator();
                fadeReultado.setTarget(result);
                fadeReultado.setPropertyName("scaleY");
                fadeReultado.setDuration(500);
                fadeReultado.setFloatValues(0f,1.5f);
                fadeReultado.setStartDelay(5000);
                fadeReultado.start();
                fadeReultado = new ObjectAnimator();
                fadeReultado.setTarget(result);
                fadeReultado.setPropertyName("scaleX");
                fadeReultado.setDuration(500);
                fadeReultado.setFloatValues(0f,1.5f);
                fadeReultado.setStartDelay(5000);
                fadeReultado.start();
                fadeReultado = new ObjectAnimator();// esconder resultado
                fadeReultado.setTarget(result);
                fadeReultado.setPropertyName("scaleY");
                fadeReultado.setDuration(500);
                fadeReultado.setFloatValues(1.5f,0f);
                fadeReultado.setStartDelay(7000);
                fadeReultado.start();
                fadeReultado = new ObjectAnimator();
                fadeReultado.setTarget(result);
                fadeReultado.setPropertyName("scaleX");
                fadeReultado.setDuration(500);
                fadeReultado.setFloatValues(1.5f,0f);
                fadeReultado.setStartDelay(7000);
                fadeReultado.start();

                TextView txt = findViewById(R.id.txtRoleta);
                txt.setText("RETIRE O SEU CUPOM\nDE DESCONTO ESPECIAL");
                ObjectAnimator fadeOut = new ObjectAnimator();
                fadeOut.setDuration(500);
                fadeOut.setPropertyName("alpha");
                fadeOut.setFloatValues(0f,1f);
                fadeOut.setStartDelay(7500);
                fadeOut.setTarget(txt);
                fadeOut.start();
                fadeOut = new ObjectAnimator();
                fadeOut.setDuration(500);
                fadeOut.setStartDelay(14000);
                fadeOut.setPropertyName("alpha");
                fadeOut.setFloatValues(1f,0f);
                fadeOut.setTarget(txt);
                fadeOut.start();


        handler.postDelayed(new Runnable() {
            public void run() {
                intent = new Intent(getApplicationContext(), AgradecimetoActivity.class);
                intent.putExtra("codigo", codigo);
                intent.putExtra("nome", nome);
                Dados dadosFirebase = new Dados();
                dadosFirebase.addDescontoExtra(email, descontoExtra + "");

                try {
                    Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                    Bitmap extra = BitmapFactory.decodeResource(getResources(), descontoBit);
                    Bitmap imgCodigo = printerBT.desenharCodigo(getResources(), codigo);
                    printerBT.printExtra(logo, extra, imgCodigo, nome, telefone);
                }catch (Exception e){
                    e.printStackTrace();
                }
        }
        }, 10000);

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
        }, 15000);

            }
        });

    }
}