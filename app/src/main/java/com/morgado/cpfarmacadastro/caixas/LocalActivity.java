package com.morgado.cpfarmacadastro.caixas;
import com.morgado.cpfarmacadastro.MainActivity;
import com.morgado.cpfarmacadastro.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import classes.Alerta;
import classes.Dados;
import classes.Tela;

public class LocalActivity extends AppCompatActivity {

    private Button next;
    private EditText input;
    private Spinner select;
    private String codigo,estado, email, nome, telefone;

    private Handler handler = new Handler();
    private Runnable runnableCodigo;
     private Handler handler2;
    private Runnable inativo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

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
            email = getIntent().getExtras().getString("email");
            nome = getIntent().getExtras().getString("nome");
            telefone = getIntent().getExtras().getString("telefone");
        }

        next = findViewById(R.id.btNextLocal);
        input   = findViewById(R.id.inputLocal);
        select = findViewById(R.id.selectUF);
        //input.setText("Duque de Caxias");

        select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                estado = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input.getText().toString().isEmpty() || input.getText().toString().length() < 4) {
                    Alerta.mensagem(getApplicationContext(), "Preencha sua estado e cidade");
                } else {
                    String local =  input.getText().toString() + " - " + estado;
                    Intent intent = new Intent(getApplicationContext(), RedeActivity.class);
                    intent.putExtra("codigo", codigo);
                    intent.putExtra("email", email);
                    Dados dados = new Dados();
                    dados.novo(codigo, nome, telefone, email);
                    dados.addLocal(email,local);
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