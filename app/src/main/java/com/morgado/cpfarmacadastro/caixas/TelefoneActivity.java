package com.morgado.cpfarmacadastro.caixas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.morgado.cpfarmacadastro.MainActivity;
import com.morgado.cpfarmacadastro.R;

import java.util.Random;

import classes.Alerta;
import classes.Dados;
import classes.MaskEditUtil;
import classes.Tela;

public class TelefoneActivity extends Activity {

    private Button next;
    private EditText inputTelefone;
    private String codigo, nome;

    private Handler handler = new Handler();
    private Runnable runnableCodigo;

     private Handler handler2;
    private Runnable inativo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telefone);

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
            nome = getIntent().getExtras().getString("nome");
        }else {
            int ale = new Random().nextInt(10) * 5032;
            codigo = Integer.toString(ale);
        }

        inputTelefone = findViewById(R.id.inputTelefone);
        //inputTelefone.setText("(21) 9 9251-6172");
        inputTelefone.addTextChangedListener(MaskEditUtil.mask(inputTelefone));

        next = findViewById(R.id.btNextTelefone);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputTelefone.getText().toString().isEmpty() || inputTelefone.getText().toString().length() < 14){
                    Toast.makeText(getApplicationContext(), "Digite seu telefone válido", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), MailActivity.class);
                    intent.putExtra("codigo", codigo);
                    intent.putExtra("nome", nome);
                    intent.putExtra("telefone", inputTelefone.getText().toString());
                    Dados dados = new Dados();
                    //dados.addTelefone(codigo, inputTelefone.getText().toString());
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