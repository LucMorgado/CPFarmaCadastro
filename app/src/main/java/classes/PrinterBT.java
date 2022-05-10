package classes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.morgado.cpfarmacadastro.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

public class PrinterBT {

    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;
    Context context;

    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    private static final byte[] ESPACO = "\n \n \n \n \n \n \n \n \n \n \n".getBytes();
    private static final byte[] ESC_ALIGN_CENTER = new byte[]{0x1b, 'a', 0x01};

    public PrinterBT(Context context) {
        this.context = context;
        try {
            FindBluetoothDevice();
            openBluetoothPrinter();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("MissingPermission")
    void FindBluetoothDevice() {

        try {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            @SuppressLint("MissingPermission")
            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();

            if (pairedDevice.size() > 0) {
                for (BluetoothDevice pairedDev : pairedDevice) {
                    if (pairedDev.getName().equals("MPT-II")) {
                        bluetoothDevice = pairedDev;
                        //lblPrinterName.setText("Bluetooth Printer Attached: "+pairedDev.getName());
                        break;
                    }
                }
            }
            //lblPrinterName.setText("Bluetooth Printer Attached");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Open Bluetooth Printer

    @SuppressLint("MissingPermission")
    void openBluetoothPrinter() throws IOException {
        try {

            //Standard uuid from string //
            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

            beginListenData();

        } catch (Exception ex) {

        }
    }

    void beginListenData() {
        try {

            final Handler handler = new Handler();
            final byte delimiter = 10;
            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                        try {
                            int byteAvailable = inputStream.available();
                            if (byteAvailable > 0) {
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for (int i = 0; i < byteAvailable; i++) {
                                    byte b = packetByte[i];
                                    if (b == delimiter) {
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedByte, 0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte, "US-ASCII");//US-ASCII
                                        readBufferPosition = 0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //lblPrinterName.setText(data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            stopWorker = true;
                        }
                    }

                }
            });

            thread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Printing Text to Bluetooth Printer //
    public void printDesconto(Bitmap logo, Bitmap desconto, Bitmap codigo, String nome, String telefone) throws IOException {
        try {
            PrintPic pic = PrintPic.getInstance();//conversor de bitma para bytes

            //logo
            Bitmap logoReduzida = Bitmap.createScaledBitmap(logo, 320,90,true);
            pic.init(logoReduzida);
            byte[] logoB = pic.printDraw();

             //desconto imagem
            Bitmap descontoReduzida = Bitmap.createScaledBitmap(desconto, 320,150,true);
            pic.init(descontoReduzida);
            byte[] descontoB = pic.printDraw();

            //codigo
            pic.init(codigo);
            byte[] codigoB = pic.printDraw();

            //nome
            nome += "\n";
            byte[] nomeB = nome.toUpperCase(Locale.ROOT).getBytes();

            //telefone
            telefone += "\n \n \n";
            byte[] telefoneB = telefone.getBytes();

            //pos desconto
            String pos = "\n\n";
            byte[] posB = pos.getBytes();

            //info rodape
            String rodape = "Procure nossos consultores\n e aproveite seu desconto.";
            byte[] rodapeB = rodape.getBytes();


            byte[] saida = new byte[logoB.length + codigoB.length + nomeB.length + telefoneB.length + descontoB.length + posB.length + rodapeB.length + ESPACO.length + ESPACO.length];
            int cont = 0;

            for(byte b: logoB){ saida[cont] = b;cont++;}
            for(byte b: codigoB){ saida[cont] = b;cont++;}
            for(byte b: nomeB){ saida[cont] = b;cont++;}
            for(byte b: telefoneB){ saida[cont] = b;cont++;}
            for(byte b: descontoB){ saida[cont] = b;cont++;}
            for(byte b: posB){ saida[cont] = b;cont++;}
            for(byte b: rodapeB){ saida[cont] = b;cont++;}
            for(byte b: ESPACO){ saida[cont] = b;cont++;}
            for(byte b: ESPACO){ saida[cont] = b;cont++;}

            outputStream.write(saida);

        } catch (Exception ex) {
        }
    }

    public void printSorteio(Bitmap logo, Bitmap sorteio, Bitmap codigo, String nome, String telefone) throws IOException {
        try {
            PrintPic pic = PrintPic.getInstance();//conversor de bitma para bytes

            //logo
            Bitmap logoReduzida = Bitmap.createScaledBitmap(logo, 320,90,true);
            pic.init(logoReduzida);
            byte[] logoB = pic.printDraw();

             //sorteio imagem
            Bitmap sorteioReduzida = Bitmap.createScaledBitmap(sorteio, 320,110,true);
            pic.init(sorteioReduzida);
            byte[] sorteioB = pic.printDraw();

            //codigo
            pic.init(codigo);
            byte[] codigoB = pic.printDraw();

            //nome
            nome += "\n";
            byte[] nomeB = nome.toUpperCase(Locale.ROOT).getBytes();

            //codigo
            telefone += "\n \n \n";
            byte[] telefoneB = telefone.getBytes();

            //pos sorteio
            String pos = "\n\n";
            byte[] posB = pos.getBytes();

            //info rodape
            String rodape = "DEPOSITE ESTE CUPOM\nNA NOSSA URNA.";
            byte[] rodapeB = rodape.getBytes();

            byte[] saida = new byte[logoB.length + codigoB.length + nomeB.length + telefoneB.length + sorteioB.length + ESPACO.length + posB.length + rodapeB.length];
            int cont = 0;
            for(byte b: logoB){ saida[cont] = b;cont++;}
            for(byte b: codigoB){ saida[cont] = b;cont++;}
            for(byte b: nomeB){ saida[cont] = b;cont++;}
            for(byte b: telefoneB){ saida[cont] = b;cont++;}
            for(byte b: sorteioB){ saida[cont] = b;cont++;}
            for(byte b: posB){ saida[cont] = b;cont++;}
            for(byte b: rodapeB){ saida[cont] = b;cont++;}
            for(byte b: ESPACO){ saida[cont] = b;cont++;}


            outputStream.write(saida);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printExtra(Bitmap logo, Bitmap extra, Bitmap codigo, String nome, String telefone) throws IOException {
        try {
            PrintPic pic = PrintPic.getInstance();//conversor de bitma para bytes

            //logo
            Bitmap logoReduzida = Bitmap.createScaledBitmap(logo, 320,90,true);
            pic.init(logoReduzida);
            byte[] logoB = pic.printDraw();

             //extra imagem
            Bitmap extraReduzida = Bitmap.createScaledBitmap(extra, 320,125,true);
            pic.init(extraReduzida);
            byte[] extraB = pic.printDraw();

            //codigo
            pic.init(codigo);
            byte[] codigoB = pic.printDraw();

            //nome
            nome += "\n";
            byte[] nomeB = nome.toUpperCase(Locale.ROOT).getBytes();

            //telefone
            telefone += "\n \n \n";
            byte[] telefoneB = telefone.getBytes();

            //pos extra
            String pos = "\n\n";
            byte[] posB = pos.getBytes();

            //info rodape
            String rodape = "Procure nossos consultores\n e aproveite seu desconto.\nDESCONTO DA ROLETA VÁLIDO \nPARA DATA DE HOJE";
            byte[] rodapeB = rodape.getBytes();

            byte[] saida = new byte[logoB.length + codigoB.length + nomeB.length +  telefoneB.length + extraB.length + ESPACO.length + posB.length + rodapeB.length + ESPACO.length];
            int cont = 0;
            for(byte b: logoB){ saida[cont] = b;cont++;}
            for(byte b: codigoB){ saida[cont] = b;cont++;}
            for(byte b: nomeB){ saida[cont] = b;cont++;}
            for(byte b: telefoneB){ saida[cont] = b;cont++;}
            for(byte b: extraB){ saida[cont] = b;cont++;}
            for(byte b: posB){ saida[cont] = b;cont++;}
            for(byte b: rodapeB){ saida[cont] = b;cont++;}
            for(byte b: ESPACO){ saida[cont] = b;cont++;}
            for(byte b: ESPACO){ saida[cont] = b;cont++;}


            outputStream.write(saida);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // Disconnect Printer //
    public void disconnectBT() throws IOException {
        try {
            stopWorker = true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
            //lblPrinterName.setText("Printer Disconnected.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setCenter() {
        try {

        outputStream.write(ESC_ALIGN_CENTER);
        disconnectBT();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

       public Bitmap desenharCodigo(Resources res, String codigo){
        int w = 40;
        int h = (int) (w/0.5);
        Bitmap base = Bitmap.createBitmap((w*codigo.length())+w*2,h, Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(base);
        canvas.drawColor(res.getColor(R.color.black));
        for(int i = 0; i<codigo.length();i++){
            String n = codigo.substring(i, i+1);
            canvas.drawBitmap(getCodigo(res, n, w , h), w * (i+1),0,null);
        }
        return base;
    }


    public Bitmap getCodigo(Resources res,String n, int w, int h){
        int id;
        switch (n) {
            case "1":
                id = R.drawable.n1;
                break;
            case "2":
                id = R.drawable.n2;
                break;
            case "3":
                id = R.drawable.n3;
                break;
            case "4":
                id = R.drawable.n4;
                break;
            case "5":
                id = R.drawable.n5;
                break;
            case "6":
                id = R.drawable.n6;
                break;
            case "7":
                id = R.drawable.n7;
                break;
            case "8":
                id = R.drawable.n8;
                break;
            case "9":
                id = R.drawable.n9;
                break;
            default:
                id = R.drawable.n0;
                break;
        }
        Bitmap n0 = BitmapFactory.decodeResource( res, id);
        return  Bitmap.createScaledBitmap(n0,w,h,true);
    }
}
