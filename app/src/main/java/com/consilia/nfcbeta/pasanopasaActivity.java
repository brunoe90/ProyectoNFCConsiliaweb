package com.consilia.nfcbeta;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class pasanopasaActivity extends AppCompatActivity {
    TextView    dato,tresultado;
    String      stringsoap;
    String      idStadium;
    String      idSocio;
    String      Puerta;
    String      documento;
    String      Tarjeta;
    Button      buttonfoto;
    Button      bvolver;
    Context     context;
    Bitmap      decodedByte = null;
    ImageView   imageView;
    Bundle      bundle;
    String      numSocio;
    String      tipodoc ="DNI";
    String      UltimaActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasanopasa);

        bvolver   =     (Button) findViewById(R.id.bvolver);
        buttonfoto =    (Button) findViewById(R.id.buttonfoto);
        bundle =        new Bundle();
        bundle =        getIntent().getExtras();
        imageView =     (ImageView) findViewById(R.id.imageView);
        tresultado =    (TextView) findViewById(R.id.tresultado);
        dato =          (TextView) findViewById(R.id.dato);
        idSocio =       String.valueOf(bundle.getInt("idSocio"));
        idStadium =     String.valueOf(bundle.getInt("idStadium"));
        Puerta =        String.valueOf(bundle.getInt("Puerta"));
        documento =     String.valueOf(bundle.getInt("documento"));
        Tarjeta =       bundle.getString("Tarjeta");
        Puerta = "10";
        UltimaActivity = bundle.getString("lastActivity");

        if (null != idStadium){

            if (null!=Puerta){

                assert UltimaActivity != null;
                if (UltimaActivity.equals("qr")){
                    if (null != idSocio){
                        getSoap("getestado");
                    }else if (null != documento){
                        getSoap("buscar");
                    }
                }
                if (UltimaActivity.equals("nfc")){
                    if (null != idSocio){
                        getSoap("getestado");
                    }else if (null != Tarjeta){
                        getSoap("getcarnet");
                    }
                }
            }   else Toast.makeText(getBaseContext(), "Falla al encontrar Puerta", Toast.LENGTH_LONG).show();
        } else Toast.makeText(getBaseContext(), "Falla al encontrar idStadio", Toast.LENGTH_LONG).show();

        buttonfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != idStadium){
                    if (null!=Puerta){
                        getSoap("GetFotoSocio");
                    }   else Toast.makeText(getBaseContext(), "Falla al encontrar Puerta", Toast.LENGTH_LONG).show();
                } else Toast.makeText(getBaseContext(), "Falla al encontrar idStadio", Toast.LENGTH_LONG).show();
            }
        });

        bvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assert UltimaActivity != null;
                if (UltimaActivity.equals("qr")){
                    Intent intent = new Intent(pasanopasaActivity.this, QRBarcodeActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (UltimaActivity.equals("nfc")){
                    Intent intent = new Intent(pasanopasaActivity.this, NfcActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {

                case 0: tresultado.setText(stringsoap); break;

                case 1:{
                    try {
                        imageView.setImageBitmap(decodedByte);
                    } catch (Exception q) {
                        q.printStackTrace();
                    }
                    break;}

                case 2: tresultado.setText(stringsoap); getSoap("getestado");break;

                case 3: {
                    String i;
                    int a = stringsoap.indexOf("Puertas=");
                    if (a<1) {
                        i="TARJETA NO VALIDA";
                        dato.setTextColor(Color.parseColor("#FF0000"));
                        tresultado.setText("Tarjeta no encontrada en base de datos");
                    }
                    else{
                        i= stringsoap.substring(a);
                        i = i.substring(0, i.indexOf('C'));
                        stringsoap = stringsoap.replace(i,"");
                        i = i.replace(";","");
                        if (i.contains("Puerta")){

                            if (i.contains("11")){
                                dato.setTextColor(Color.parseColor("#00FF00")); tresultado.setTextColor(Color.parseColor("#00FF00"));
                            } else dato.setTextColor(Color.parseColor("#FF0000")); tresultado.setTextColor(Color.parseColor("#FF0000"));
                        }
                        tresultado.setText(stringsoap );
                    }


                    dato.setText(i);
                    break;}

                case 4: {
                    tresultado.setText(stringsoap); getSoap("getestado");
                    break;}

                default: Toast.makeText(getBaseContext(), "Fallo comando", Toast.LENGTH_LONG).show(); break;
            }
            return false;
        }
    });

    private void getSoap( final String method) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SoapRequests ex = new SoapRequests();
                context = getApplicationContext();
                switch (method) {
                    case "getversion":{
                        stringsoap =    ex.getversion();
                        handler.sendEmptyMessage(0);
                        break;}

                    case "GetFotoSocio":{
                        byte[] foto =   ex.getfotosocio((idStadium),String.valueOf(idSocio ));
                        decodedByte =   BitmapFactory.decodeByteArray(foto, 0, foto.length);
                        handler.sendEmptyMessage(1);
                        break;}

                    case "buscar":{

                        numSocio =      ex.getsociobydoc((idStadium), String.valueOf(idSocio ), tipodoc); //type="s:int" devuelve entero ////////// ENTRA unsigned byte idStadium -- string idTipoDoc -- long documento
                        stringsoap =    numSocio;
                        idSocio=        numSocio;
                        handler.sendEmptyMessage(2);
                        break;}

                    case "getestado":{

                        numSocio =      ex.getsocio((idStadium),String.valueOf(idSocio ),String.valueOf(idSocio ) ); //type="s:int" devuelve entero ////////// ENTRA unsigned byte idStadium -- string idTipoDoc -- long documento
                        stringsoap =    numSocio;
                        handler.sendEmptyMessage(3);
                        break;}

                    case "getcarnet":{
                        stringsoap =    ex.getcaret(idStadium,bundle.getString("NFCTAG"));
                        idSocio=        numSocio;
                        handler.sendEmptyMessage(4);
                        break;}
                }
            }
        }).start();
    }
}
