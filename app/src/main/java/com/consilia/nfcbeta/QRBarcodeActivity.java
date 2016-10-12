package com.consilia.nfcbeta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class QRBarcodeActivity extends AppCompatActivity implements View.OnClickListener {

    Button bqr,benviar,benviarid;
    TextView formatTxt,contentTxt;
    Bundle bundle;
    EditText editText, editText2;
    String idSocio="";
    RadioButton bsocio,bdni;
    String dato="";
    TabHost host;
    Button botonvolver;
    Button benviaridinvitado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrbarcode);

        final Activity activity= this;
        bqr = (Button) findViewById(R.id.bqr);
        benviar = (Button) findViewById(R.id.benviar);
        benviarid = (Button)findViewById(R.id.bidsocio);
        bsocio = (RadioButton)findViewById(R.id.buttonsocio);
        bdni = (RadioButton)findViewById(R.id.buttondni);
        botonvolver = (Button) findViewById(R.id.bvolver);
        benviarid = (Button) findViewById(R.id.bidsocio);
        benviaridinvitado = (Button) findViewById(R.id.binvitado);

        editText = (EditText)findViewById(R.id.editText4);
        bundle= new Bundle();
        bundle = getIntent().getExtras();
        bundle.putString("lastActivity","qr");
        bundle.putString("IP",bundle.getString("IP"));
        bundle.putString("port",bundle.getString("port"));
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if (!connected) Toast.makeText(getBaseContext(), "Falla la coneccion a internet!!!!", Toast.LENGTH_LONG).show();


        host = (TabHost) findViewById(R.id.tabHost);

        assert host != null;
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("ESCANEAR");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("SOCIO");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("INVITADO");
        host.addTab(spec);

        if (bundle.getInt("TAB")!=0){
            int a = bundle.getInt("TAB");

            host.setCurrentTab(a);
        }



        editText = (EditText) findViewById(R.id.edtsocio);
        editText2 = (EditText) findViewById(R.id.edtinvitado);


        benviarid.setOnClickListener(this);
        benviaridinvitado.setOnClickListener(this);




        bqr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                IntentIntegrator scanIntegrator = new IntentIntegrator( activity);
                scanIntegrator.initiateScan();
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!editText.getText().toString().equals("")){
                        benviarid.performClick();
                    }else Toast.makeText(getBaseContext(), "Indicar Socio", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });

        editText2.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!editText2.getText().toString().equals("")){
                        benviaridinvitado.performClick();
                    } else Toast.makeText(getBaseContext(), "Indicar Numero de Invitado o Documento", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });



        benviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!idSocio.equals("")){
                    Intent intent = new Intent(QRBarcodeActivity.this, pasanopasaActivity.class);
                    bundle.putInt("idStadium",bundle.getInt("idStadium"));
                    bundle.putInt(dato,Integer.valueOf(idSocio));
                    bundle.putString("manual","idSocio");
                    bundle.putInt("NumeroAconvertir", Integer.parseInt(idSocio));
//                    bundle.putInt("NumeroAconvertir",Integer.valueOf(editText.getText().toString()));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else Toast.makeText(getBaseContext(), "Indicar Socio", Toast.LENGTH_LONG).show();

            }
        });



    }


    @Override
    public void onClick(View v) {

        benviarid = (Button) findViewById(R.id.bidsocio);
        benviaridinvitado = (Button) findViewById(R.id.binvitado);



        if (v.getId() == R.id.bidsocio) {

            if (!(editText.getText().toString().equals(""))) {
                Intent intent = new Intent(QRBarcodeActivity.this, pasanopasaActivity.class);
                bundle.putInt("idStadium", bundle.getInt("idStadium"));
                bundle.remove("NFCTAG");
                //bundle.putInt(dato, Integer.valueOf(editText.getText().toString()));
                bundle.putString("manual",dato);
                bundle.putInt("NumeroAconvertir",Integer.valueOf(editText.getText().toString()));
                intent.putExtras(bundle);
                startActivity(intent);
            } else Toast.makeText(getBaseContext(), "Indicar Socio", Toast.LENGTH_LONG).show();
        }
        if (v.getId() == R.id.binvitado) {

            if (!(editText2.getText().toString().equals(""))) {
                Intent intent = new Intent(QRBarcodeActivity.this, pasanopasaActivity.class);
                bundle.putInt("idStadium", bundle.getInt("idStadium"));
                bundle.remove("NFCTAG");
                //bundle.putInt(dato, Integer.valueOf(editText.getText().toString()));
                bundle.putString("manual",dato);
                bundle.putInt("NumeroAconvertir",Integer.valueOf(editText2.getText().toString()));
                intent.putExtras(bundle);
                startActivity(intent);
            } else Toast.makeText(getBaseContext(), "Indicar Invitado", Toast.LENGTH_LONG).show();
        }
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
//        bid = (RadioButton) findViewById(R.id.bid);
        bdni = (RadioButton) findViewById(R.id.buttondni);
//        binvitado = (RadioButton) findViewById(R.id.SelectInvitado);
//        bselectsocio = (RadioButton) findViewById(R.id.SelectSocio);

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.idInv:
                if (checked){
                    dato = "idInvitado";
                }
                break;

            case R.id.dniInv:
                if (checked) {
                    dato = "DocInvitado";
                }
                break;
        }

        switch (view.getId()) {
            case R.id.idsocio:
                if (checked){
                    dato = "idSocio";
                }
                break;

            case R.id.docsocio:
                if (checked){
                    dato = "DocSocio";
                }
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
           // Toast.makeText(getBaseContext(), "Volviendo al menu", Toast.LENGTH_LONG).show();
            bundle.putInt("idStadium",bundle.getInt("idStadium"));
            bundle.putInt("Puerta",bundle.getInt("Puerta"));
            Intent intent = new Intent(QRBarcodeActivity.this, MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        formatTxt = (TextView)findViewById(R.id.formatTxt);
        contentTxt= (TextView)findViewById(R.id.contentTxt);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);
            bundle.putString("QRCONTENIDO",scanContent);
            idSocio=scanContent;
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No se pudo escanear, intente nuevamente!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }



}
