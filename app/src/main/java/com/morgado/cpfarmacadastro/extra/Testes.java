package com.morgado.cpfarmacadastro.extra;
import com.morgado.cpfarmacadastro.R;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import classes.PrintPic;
import classes.PrinterBT;

public class Testes extends AppCompatActivity {
    ImageView imagem;
    Button bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testes);


        PrinterBT  printerBT = new PrinterBT(getApplicationContext());

        bt = findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 try {
                    Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                    Bitmap desconto = BitmapFactory.decodeResource(getResources(), R.drawable.trinta);
                    Bitmap imgCodigo = printerBT.desenharCodigo(getResources(), "0000");
                    printerBT.printDesconto(logo, desconto, imgCodigo, "TESTE", "21 92122");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



    }
    }
