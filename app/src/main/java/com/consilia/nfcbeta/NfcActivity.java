package com.consilia.nfcbeta;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


public class NfcActivity extends AppCompatActivity implements View.OnClickListener {

    Button botonvolver, benviarid;
    Button benviaridinvitado;
    EditText editText, editText2;
    long Resultado = 0;
    int signo = 0;
    Bundle bundle;
    String texto = "";
    String NFC = "";
    //RadioButton bid, bdni;
    String dato = "";
    TabHost host;

    private final String[][] techList = new String[][]{
            new String[]{
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        bundle = new Bundle();
        bundle = getIntent().getExtras();
        bundle.putString("lastActivity", "nfc");
        bundle.putString("IP",bundle.getString("IP"));
        bundle.putString("port",bundle.getString("port"));
        boolean connected;
        bundle.remove("NFCTAG");
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



        botonvolver = (Button) findViewById(R.id.bvolver);
        benviarid = (Button) findViewById(R.id.bidsocio);
        benviaridinvitado = (Button) findViewById(R.id.binvitado);
        editText = (EditText) findViewById(R.id.edtsocio);
        editText2 = (EditText) findViewById(R.id.edtinvitado);
//        bid = (RadioButton) findViewById(R.id.buttonsocio);
//        bdni = (RadioButton) findViewById(R.id.buttondni);
        //binvitado = (RadioButton) findViewById(R.id.SelectInvitado);
        //bselectsocio = (RadioButton) findViewById(R.id.SelectSocio);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if (!connected)
            Toast.makeText(getBaseContext(), "Falla la coneccion a internet!!!!", Toast.LENGTH_LONG).show();

        botonvolver.setOnClickListener(this);
        benviarid.setOnClickListener(this);
        benviaridinvitado.setOnClickListener(this);


        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!editText.getText().toString().equals("")){
                        benviarid.performClick();
                    } else Toast.makeText(getBaseContext(), "Indicar Numero de Socio o Documento", Toast.LENGTH_LONG).show();
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


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        botonvolver = (Button) findViewById(R.id.bvolver);
        benviarid = (Button) findViewById(R.id.bidsocio);
        benviaridinvitado = (Button) findViewById(R.id.binvitado);

        if (v.getId() == R.id.bvolver) {
            if (!(texto.equals(""))) {
                Intent intent = new Intent(NfcActivity.this, pasanopasaActivity.class);
                bundle.putInt("idStadium", bundle.getInt("idStadium"));
                bundle.remove("idSocio");

                //bundle.putInt("idSocio",Integer.valueOf(editText.getText().toString()));
                intent.putExtras(bundle);
                startActivity(intent);
            } else
                Toast.makeText(getBaseContext(), "Volver a pasar tarjeta", Toast.LENGTH_LONG).show();
        }


        if (v.getId() == R.id.bidsocio) {

            if (!(editText.getText().toString().equals(""))) {
                Intent intent = new Intent(NfcActivity.this, pasanopasaActivity.class);
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
                Intent intent = new Intent(NfcActivity.this, pasanopasaActivity.class);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
         //   Toast.makeText(getBaseContext(), "Volviendo al menu", Toast.LENGTH_LONG).show();
            bundle.putInt("idStadium", bundle.getInt("idStadium"));
            bundle.putInt("Puerta", bundle.getInt("Puerta"));
            Intent intent = new Intent(NfcActivity.this, MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
//        bid = (RadioButton) findViewById(R.id.bid);
//        bdni = (RadioButton) findViewById(R.id.buttondni
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
    protected void onResume() {
        super.onResume();
        // creating pending intent:
        Context context = this;
        NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            // creating intent receiver for NFC events:
            IntentFilter filter = new IntentFilter();
            filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
            filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
            filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
            // enabling foreground dispatch for getting intent from NFC event:
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);

        } else{
            for (int i=0; i < 30; i++)
            {
                Toast.makeText(getBaseContext(), "Prender el NFC desde el menu!!!!", Toast.LENGTH_LONG).show();
            }
        }



    }


    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }


    @Override
    protected void onNewIntent(Intent intent) {

        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            if ((findViewById(R.id.text)) != null) {
                texto = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));

                if (findViewById(R.id.text) != null) {
                   // ((TextView) findViewById(R.id.text)).setText("NFC Tag " + texto);
                    //bundle.putString("NFCINVITADO",texto);////
                    NFC = texto;
                    int a =host.getCurrentTab();
                    if (a==0){
                        bundle.putString("NFCTAG",texto);
                        bundle.remove("NumeroAconvertir");
                        bundle.remove("manual");
                        botonvolver.performClick();
                    }else {
                        bundle.putString("NFCINVITADO",texto);
                        bundle.remove("NumeroAconvertir");
                        bundle.remove("manual");
                        botonvolver.performClick();
                    }


                }
            }
            Log.d("asd", NfcAdapter.EXTRA_ID);
            //Log.d("asd", ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));

        }
    }

    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";
        signo = 0;
        byte buffer[] = new byte[inarray.length];
        // buffer=inarray;

        for (j = inarray.length; j > 0; j--) {

            buffer[buffer.length - j] = inarray[j - 1];
        }

        if (buffer[buffer.length - 1] < 0) {
            signo = 1;

            for (j = inarray.length; j > 0; j--) {

                buffer[buffer.length - j] = (byte) ~buffer[buffer.length - j];
            }
            buffer[buffer.length - 1] = (byte) ((byte) 0x7f & buffer[buffer.length - 1]);
            buffer[0]++;
        }

        for (j = buffer.length; j > 0; j--) {

            in = (int) buffer[j - 1] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }


        if (signo == 1) {

            Resultado = -Long.parseLong(out, 16);

        } else Resultado = Long.parseLong(out, 16);


        return String.valueOf(Resultado);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Nfc Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
