package com.consilia.nfcbeta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    TextView    tresultado;
    String      stringsoap;
    String      idStadium, idSocio, Puerta, documento, Tarjeta;
    Button      buttonfoto;
    Context     context;
    Bitmap      decodedByte = null;
    ImageView   imageView;
    Bundle      bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasanopasa);

        buttonfoto = (Button) findViewById(R.id.buttonfoto);
        bundle = new Bundle();
        bundle = getIntent().getExtras();
        imageView = (ImageView) findViewById(R.id.imageView);
        tresultado = (TextView) findViewById(R.id.tresultado);
        idSocio =       String.valueOf(bundle.getInt("idSocio"));
        idStadium =     String.valueOf(bundle.getInt("idStadium"));
        Puerta =        String.valueOf(bundle.getInt("Puerta"));
        documento =     String.valueOf(bundle.getInt("documento"));
        Tarjeta =       bundle.getString("Tarjeta");

        if (null != idStadium){

            if (null!=Puerta){

                if (null != Tarjeta){
                    getSoap("getcarnet");
                }
                if (null != idSocio){
                    getSoap("getestado");
                }
                if (null != documento){
                    getSoap("buscar");
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
    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {

                case 0: tresultado.setText(stringsoap); break;

                case 1:{
                    {
                        try {
                            imageView.setImageBitmap(decodedByte);
                        } catch (Exception q) {
                            q.printStackTrace();
                        }
                    }
                    break;}

                case 2: tresultado.setText(stringsoap); break;

                case 3: tresultado.setText(stringsoap);  break;

                case 4: {
                    tresultado.setText(stringsoap); getSoap("getestado");
                    break;}
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
                    case "getversion":

                        stringsoap = ex.getversion();
                        handler.sendEmptyMessage(0);
                        break;
                    case "GetFotoSocio": {

                        byte[] foto = ex.getfotosocio(String.valueOf(idStadium),String.valueOf(idSocio ));
                        decodedByte = BitmapFactory.decodeByteArray(foto, 0, foto.length);
                        handler.sendEmptyMessage(1);
                        break;
                    }
                    case "buscar": {

                        String numSocio;
                        String tipodoc = "DNI";
                        numSocio = ex.getsociobydoc(String.valueOf(idStadium), String.valueOf(idSocio ), tipodoc); //type="s:int" devuelve entero ////////// ENTRA unsigned byte idStadium -- string idTipoDoc -- long documento
                        stringsoap = String.valueOf(numSocio);
                        handler.sendEmptyMessage(2);
                        break;
                    }
                    case "getestado": {

                        String numSocio;
                        numSocio = ex.getsocio(String.valueOf(idStadium),String.valueOf(idSocio ),String.valueOf(idSocio ) ); //type="s:int" devuelve entero ////////// ENTRA unsigned byte idStadium -- string idTipoDoc -- long documento
                        stringsoap = String.valueOf(numSocio);
                        handler.sendEmptyMessage(3);

                        break;
                    }
                    case "getcarnet": {
                        stringsoap = ex.getcaret("2",bundle.getString("NFCTAG"));
                        handler.sendEmptyMessage(4);
                    }
                }
            }
        }).start();
    }
}
