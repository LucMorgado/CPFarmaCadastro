package classes;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.SnapshotHolder;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.sql.Struct;
import java.util.Locale;

public class Dados {
    private String codigo;
    private String nome, telefone, email, local;
    public DatabaseReference usuarios, ultimo, emails;

    public Dados(){
        //FIREBASE CONECTION
        FirebaseDatabase database   = FirebaseDatabase.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
        if(!settings.isPersistenceEnabled()){
            database.setPersistenceEnabled(settings.isPersistenceEnabled());
        }
        usuarios   = database.getReference("usuarios");
        ultimo     = database.getReference("ultimo2");
        emails     = database.getReference("emails");


    }

    public void novo(String id){
        int novoCodigo = Integer.parseInt(id) + 1;
        codigo = novoCodigo + "";
        //usuarios.child(codigo).setValue("");
        ultimo.setValue(codigo);
    }

    public void novo(String id,String nome,String telefone,String email){
            DatabaseReference usuario = usuarios.child(lucasTexto(email));
            usuario.child("codigo").setValue(id);
            usuario.child("nome").setValue(nome.toUpperCase(Locale.ROOT));
            usuario.child("telefone").setValue(telefone);
            usuario.child("email").setValue(email.toUpperCase(Locale.ROOT));
    }

    public void addNome(String id, String nome){
        DatabaseReference usuario = usuarios.child(lucasTexto(id));
        usuario.child("nome").setValue(nome.toUpperCase(Locale.ROOT));
    }

    public void addTelefone(String id, String telefone){
        DatabaseReference usuario = usuarios.child(lucasTexto(id));
        usuario.child("telefone").setValue(telefone);
    }

    public void addEmail(String id, String email){
        email = email.toUpperCase();
        DatabaseReference usuario = usuarios.child(lucasTexto(id));
        usuario.child("email").setValue(email);

    }

    public void addLocal(String id, String Local){
        DatabaseReference usuario = usuarios.child(lucasTexto(id));
        usuario.child("Local").setValue(Local.toUpperCase(Locale.ROOT));
    }

    public void addRede(String id, String rede){
        DatabaseReference usuario = usuarios.child(lucasTexto(id));
        usuario.child("rede").setValue(rede.toUpperCase(Locale.ROOT));
    }

    public void addDesconto(String id, String desconto){
        DatabaseReference usuario = usuarios.child(lucasTexto(id));
        usuario.child("desconto").setValue(desconto);
    }

    public void addDescontoExtra(String id, String desconto){
        DatabaseReference usuario = usuarios.child(lucasTexto(id));
        usuario.child("extra").setValue(desconto);
    }

    public static String lucasTexto(String texto){
        texto = texto.toUpperCase();
        texto = texto.replace("@", "0Z88UT");//trocar @ por 0Z88UT
        texto = texto.replace(".", "0Z89UT");//trocar . por 0Z89UT
        texto = texto.replace("-", "0Z90UT");//trocar - por 0Z90UT
        texto = texto.replace("_", "0Z91UT");//trocar _ por 0Z91UT
        return texto;
    }
    public static String desLucasTexto(String texto){
        texto = texto.replace( "0Z88UT","@");//trocar @ por 0Z88UT
        texto = texto.replace( "0Z89UT",".");//trocar . por 0Z89UT
        texto = texto.replace( "0Z90UT","-");//trocar - por 0Z90UT
        texto = texto.replace( "0Z91UT","_");//trocar _ por 0Z91UT
        return texto;
    }


}
