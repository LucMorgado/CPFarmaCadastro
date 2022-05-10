package com.morgado.cpfarmacadastro.caixas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.morgado.cpfarmacadastro.MainActivity;
import com.morgado.cpfarmacadastro.R;

import java.util.Random;

import classes.Dados;
import classes.Tela;

public class NomeActivity extends AppCompatActivity {
    private Button next;
    private EditText inputName;
    private String codigo;

    private Handler handler = new Handler();
    private Handler handler2;
    private Runnable inativo;
    private Runnable runnableCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nome);
        View decorView = getWindow().getDecorView();

        //esconder navegaçao
        runnableCodigo = new Runnable() {
            @Override
            public void run() {
                Tela.telaCheia(getWindow().getDecorView());
                handler.postDelayed(runnableCodigo, 1000);
            }
        };
        handler.post(runnableCodigo);

        //verificar se tem cadastro em andamento
        if(getIntent().hasExtra("codigo")) {
            codigo = getIntent().getExtras().getString("codigo");
        }else {
            int ale = new Random().nextInt(10) * 5032;
            codigo = Integer.toString(ale);
        }

        next = findViewById(R.id.btNextNome);
        inputName  = findViewById(R.id.inputName);
        //inputName.setText("Lucas Morgado");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputName.getText() == null || inputName.getText().toString().isEmpty() || inputName.getText().toString().length() < 8){
                    Toast.makeText(getApplicationContext(), "Digite seu nome completo", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), TelefoneActivity.class);
                    intent.putExtra("codigo", codigo);
                    intent.putExtra("nome", inputName.getText().toString());
                    Dados dados = new Dados();
                    //dados.addNome(codigo, inputName.getText().toString() );
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