package com.morgado.cpfarmacadastro.caixas;
import com.morgado.cpfarmacadastro.MainActivity;
import com.morgado.cpfarmacadastro.extra.GanhouDesconto;
import com.morgado.cpfarmacadastro.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import classes.Dados;
import classes.Tela;

public class RedeActivity extends AppCompatActivity {

    private Button next;
    private RadioGroup radios;
    private String codigo, email;

     private Handler handler2;
    private Runnable inativo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rede);

        Tela.telaCheia(getWindow().getDecorView());

        next = findViewById(R.id.btNextRede);
        radios = findViewById(R.id.inputRede);

        //verificar se tem cadastro em andamento
        codigo = "0";
        if(getIntent().hasExtra("codigo")) {
            codigo = getIntent().getExtras().getString("codigo");
            email  = getIntent().getExtras().getString("email");
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destino;
                if(radios.getCheckedRadioButtonId() == R.id.radioNao) {
                    destino = GanhouDesconto.class;
                }else {
                    destino = QualRedeActivity.class;
                }
                Intent intent = new Intent(getApplicationContext(), destino);
                intent.putExtra("codigo", codigo);
                intent.putExtra("email", email);
                Dados dados = new Dados();
                dados.addRede(email, "");
                ActivityOptionsCompat aoc = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), R.anim.fade_in,R.anim.fade_out);
                startActivity(intent, aoc.toBundle());
            }
        });

        handler2 = new Handler();
        handler2.postDelayed(inativo = new Runnable() {
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                ActivityOptionsCompat aoc = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), R.anim.fade_in,R.anim.fade_out);
                startActivity(intent, aoc.toBundle());
                finish();
        }
        },60000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler2.removeCallbacks(inativo);
    }
}