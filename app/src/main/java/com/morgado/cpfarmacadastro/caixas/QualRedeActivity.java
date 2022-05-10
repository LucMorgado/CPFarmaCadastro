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
import android.widget.EditText;
import android.widget.Toast;

import classes.Dados;
import classes.Tela;

public class QualRedeActivity extends AppCompatActivity {

    private Button next;
    private EditText input;
    private String codigo, email;

    private Handler handler = new Handler();
    private Runnable runnableCodigo;

     private Handler handler2;
    private Runnable inativo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qual_rede);

        //esconder navegaçao
        runnableCodigo = new Runnable() {
            @Override
            public void run() {
                Tela.telaCheia(getWindow().getDecorView());
                handler.postDelayed(runnableCodigo, 1000);
            }
        };
        handler.post(runnableCodigo);

        next = findViewById(R.id.btNextQual);
        input = findViewById(R.id.inputQualRede);
        //input.setText("ALHPA");

        //verificar se tem cadastro em andamento
        codigo = "0";
        if(getIntent().hasExtra("codigo")) {
            codigo = getIntent().getExtras().getString("codigo");
            email  = getIntent().getExtras().getString("email");
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input.getText().toString().isEmpty() || input.getText().toString().length() < 3) {
                    Toast.makeText(getApplicationContext(), "Preencha com o nome da sua rede", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), GanhouDesconto.class);
                    intent.putExtra("codigo", codigo);
                    intent.putExtra("email", email);
                    Dados dados = new Dados();
                    dados.addRede(email, input.getText().toString());
                    ActivityOptionsCompat aoc = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), R.anim.fade_in,R.anim.fade_out);
                startActivity(intent, aoc.toBundle());
                }
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
        handler.removeCallbacks(runnableCodigo);
        handler2.removeCallbacks(inativo);
    }
}