package com.consilia.nfcbeta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;



public class ServerActivity extends AppCompatActivity {
    TextView dato, tresultado;
    private String stringsoap;
    String idStadium, idSocio;
    Button btn;
    Context context;
    Bitmap decodedByte = null;
    ImageView imageView;
    Bundle bundle;
    String numSocio;
    String tipodoc ="DNI";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        context = getApplicationContext();

        final EditText edt = (EditText) findViewById(R.id.valor);
        btn = (Button) findViewById(R.id.btn);
        imageView = (ImageView) findViewById(R.id.imageView);
        tresultado = (TextView) findViewById(R.id.tresultado);
        dato = (TextView) findViewById(R.id.dato);
        bundle = getIntent().getExtras();
        idSocio = String.valueOf(bundle.getInt("idSocio"));
        idStadium = String.valueOf(bundle.getInt("idStadium"));

        //idSocio = 23974462;

        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if (!connected) Toast.makeText(getBaseContext(), "Falla la coneccion a internet!!!!", Toast.LENGTH_LONG).show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert edt != null;
                if (edt.getText().toString().equals("version")) {
                    getSoap( "getversion");

                } else if (edt.getText().toString().equals("")) {
                    tresultado.setText("pone un valor para hacer en codigo de barras");

                } else if (edt.getText().toString().equals("socio")) {
                    getSoap( "buscar");

                } else if (edt.getText().toString().equals("foto")) {
                    getSoap( "GetFotoSocio");
                }else if (edt.getText().toString().equals("estado")) {
                    getSoap( "getestado");
                }else if (edt.getText().toString().equals("tarjeta")) {
                    getSoap( "getcarnet");
                }

            }
        });

        if ((bundle.getString("NFCTAG")) != null) {
            dato.setText("NFC Tag " + bundle.getString("NFCTAG"));
        }

        if ((bundle.getString("QRCONTENIDO")) != null) {
            dato.setText("Contentido :" + bundle.getString("QRCONTENIDO"));
        }
        if ((bundle.getInt("idSocio")) != 0) {
            dato.setText("ID SOCIO: " + bundle.getInt("idSocio"));
        }
        if (tresultado.getText().toString().equals("exception")) {
            assert edt != null;
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    protected Handler handler = new Handler(new Handler.Callback() {

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

                    String i = stringsoap.substring(stringsoap.indexOf("Puertas="));
                   // stringsoap = stringsoap.substring(0,stringsoap.indexOf("Pu"));
                    i = i.substring(0, i.indexOf('C'));
                   // i = i.replace(";","");
                    stringsoap = stringsoap.replace(i,"");
                    i = i.replace(";","");
                    if (i.contains("Puerta")){

                        if (i.contains("11")){
                            dato.setTextColor(Color.parseColor("#00FF00"));
                        } else dato.setTextColor(Color.parseColor("#FF0000"));
                    }
                    tresultado.setText(stringsoap );
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
                        stringsoap =    String.valueOf(numSocio);
                        idSocio=        numSocio;
                        handler.sendEmptyMessage(2);
                        break;}

                    case "getestado":{
                        String numSocio;
                        numSocio =      ex.getsocio((idStadium),String.valueOf(idSocio ),String.valueOf(idSocio ) ); //type="s:int" devuelve entero ////////// ENTRA unsigned byte idStadium -- string idTipoDoc -- long documento
                        stringsoap =    String.valueOf(numSocio);
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Server Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.consilia.nfcbeta/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Server Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.consilia.nfcbeta/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


   /* private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage= (ImageView)findViewById(R.id.imageView);
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }*/



}


