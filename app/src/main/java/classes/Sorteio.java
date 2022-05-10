package classes;


import java.util.Random;

public abstract class Sorteio {

    public static int sortear(){
        int numeroDaSorte = new Random().nextInt(100);
        if(numeroDaSorte <=5 ) {
            numeroDaSorte = 50;
        }else if(numeroDaSorte <= 20){
            numeroDaSorte = 30;
        }else{
            numeroDaSorte = 20;
        }
        return numeroDaSorte;
    }
}
