package com.consilia.nfcbeta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.server.converter.StringToIntConverter;


public class ServerActivity extends AppCompatActivity {
    TextView dato, tresultado;
    private String stringsoap;
    int idStadium, idSocio=0;
    Button btn;
    Context context;
    Bitmap decodedByte = null;
    ImageView imageView;
    Bundle bundle;
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
        tresultado = (TextView) findViewById(R.id.tresultado);
        dato = (TextView) findViewById(R.id.dato);
        bundle = getIntent().getExtras();
        idSocio = (bundle.getInt("idSocio"));
        idStadium = bundle.getInt("idStadium");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert edt != null;
                if (edt.getText().toString().equals("version")) {
                    getSoap(edt.getText().toString(), "getversion");

                } else if (edt.getText().toString().equals("")) {
                    tresultado.setText("pone un valor para hacer en codigo de barras");

                } else if (edt.getText().toString().equals("socio")) {
                    getSoap(edt.getText().toString(), "buscar");

                } else if (edt.getText().toString().equals("foto")) {
                    getSoap(edt.getText().toString(), "GetFotoSocio");
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

    public Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {

                case 0:
                    tresultado.setText(stringsoap);
                    if (decodedByte != null) {
                        imageView.setImageBitmap(decodedByte);
                    }

                    //getSoap(findViewById(R.id.valor).toString(),"genericbarcode");
                    //new DownloadImageTask((ImageView) findViewById(R.id.imageView)) .execute(stringsoap);

                    break;
            }
            return false;
        }
    });

    private void getSoap(final String toConvert, final String method) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SoapRequests ex = new SoapRequests();
                context = getApplicationContext();
                switch (method) {
                    case "getversion":

                        stringsoap = ex.getversion();
                        break;
                    case "GetFotoSocio": {

                        String foto = ex.getfotosocio(1, idStadium);
                        byte[] decodedString = Base64.decode(foto, Base64.URL_SAFE);
                        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, foto.length());
                        break; //type="s:base64Binary" devuelve base 64
                    }
                    case "buscar": {

                        String numSocio;
                        String tipodoc = "DNI";
                        numSocio = ex.getsociobydoc("2", "23974462", tipodoc); //type="s:int" devuelve entero ////////// ENTRA unsigned byte idStadium -- string idTipoDoc -- long documento
                        stringsoap = String.valueOf(numSocio);
                        break;
                    }
                    //case


                }

                handler.sendEmptyMessage(0);
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
