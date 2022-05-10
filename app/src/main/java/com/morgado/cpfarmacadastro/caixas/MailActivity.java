package com.morgado.cpfarmacadastro.caixas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.EventTarget;
import com.morgado.cpfarmacadastro.MainActivity;
import com.morgado.cpfarmacadastro.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import classes.Alerta;
import classes.Dados;
import classes.PrinterBT;
import classes.Tela;
import classes.ValidaEmail;

public class MailActivity extends AppCompatActivity {

    private Button next;
    private EditText input;
    private String codigo, email, nome, telefone;
    private boolean cadastrado = false;
    ValueEventListener fireListen;

    private Handler handler = new Handler();
    private Runnable runnableCodigo;
     Dados dados = new Dados();

      private Handler handler2;
    private Runnable inativo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        //esconder navegaçao
        runnableCodigo = new Runnable() {
            @Override
            public void run() {
                Tela.telaCheia(getWindow().getDecorView());
                handler.postDelayed(runnableCodigo, 100);
            }
        };
        handler.post(runnableCodigo);

        next = findViewById(R.id.btNextMail);
        input = findViewById(R.id.inputMail);
        //input.setText("lucas@mail.com");

        //verificar se tem cadastro em andamento
        if(getIntent().hasExtra("codigo")) {
            codigo = getIntent().getExtras().getString("codigo");
            nome = getIntent().getExtras().getString("nome");
            telefone = getIntent().getExtras().getString("telefone");
        }


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = input.getText().toString();

                fireListen = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(Dados.lucasTexto(email))){
                                Alerta.mensagem(getApplicationContext(), "Usuario já cadastrado");
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                ActivityOptionsCompat aoc = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), R.anim.fade_in,R.anim.fade_out);
                                startActivity(intent, aoc.toBundle());
                                return;

                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                try {
                    dados.usuarios.addValueEventListener( fireListen);


                }catch (Exception e) {
                    Alerta.mensagem(getApplicationContext(), "deun ruim");
                }
                if(ValidaEmail.isValidEmailAddressRegex(email)) {
                    Intent intent = new Intent(getApplicationContext(), LocalActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("codigo", codigo);
                    intent.putExtra("nome", nome);
                    intent.putExtra("telefone", telefone);
                    ActivityOptionsCompat aoc = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), R.anim.fade_in,R.anim.fade_out);
                    startActivity(intent, aoc.toBundle());
                } else
                    Toast.makeText(getApplicationContext(), "Digite um email válido", Toast.LENGTH_LONG).show();
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
        dados.usuarios.removeEventListener(fireListen);
        handler2.removeCallbacks(inativo);
    }

}