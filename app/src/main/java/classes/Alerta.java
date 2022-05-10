package classes;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public abstract class Alerta {

    public static void mensagem(Context contexto, String msg){
        Toast.makeText(contexto , msg , Toast.LENGTH_SHORT).show();
    }

}
