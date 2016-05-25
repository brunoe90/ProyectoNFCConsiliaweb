package com.consilia.nfcbeta;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class ServerActivity extends AppCompatActivity {
    TextView dato, tresultado;
    private String stringsoap;
    Button btn;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        final EditText edt = (EditText)findViewById(R.id.valor);
        btn = (Button)findViewById(R.id.btn);
        tresultado = (TextView)findViewById(R.id.tresultado);
        dato = (TextView)findViewById(R.id.dato);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert edt != null;
                if (edt.length() > 0) {
                    getSoap(edt.getText().toString(),"getquote");
                } else {
                    tresultado.setText("pone un valor para hacer en codigo de barras");
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if ((bundle.getString("NFCTAG")) != null) {
            dato.setText("NFC Tag " + bundle.getString("NFCTAG"));
        }

        if ((bundle.getString("QRCONTENIDO")) != null) {
            dato.setText("Contentido :" + bundle.getString("QRCONTENIDO"));
        }
        if (tresultado.getText().toString().equals("exception")){
            assert edt != null;

        }



    }
    public Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {

                case 0:
                    tresultado.setText(stringsoap);

                    getSoap(findViewById(R.id.valor).toString(),"genericbarcode");
                    new DownloadImageTask((ImageView) findViewById(R.id.imageView)) .execute(stringsoap);
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
                switch (method) {
                    case "getquote":
                        stringsoap = ex.getquote(toConvert);
                        break;
                    case "genericbarcode":
                        stringsoap = ex.genericbarcode(toConvert);
                        break;
                    default:
                        stringsoap = "vacio";
                        break;
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
    }
}
