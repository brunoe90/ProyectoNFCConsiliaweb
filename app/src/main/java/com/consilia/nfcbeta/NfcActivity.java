package com.consilia.nfcbeta;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;

public class NfcActivity extends AppCompatActivity {

    Button      botonvolver, benviarid;
    EditText    editText;
    long        Resultado=0;
    int         signo=0;
    Bundle      bundle;
    String      texto="";
    RadioButton bsocio,bdni;
    String      dato="";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        botonvolver = (Button) findViewById(R.id.bvolver);
        benviarid = (Button)findViewById(R.id.bidsocio);
        editText = (EditText)findViewById(R.id.edtsocio);
        bsocio = (RadioButton)findViewById(R.id.buttonsocio);
        bdni = (RadioButton)findViewById(R.id.buttondni);
        bundle= new Bundle();
        bundle = getIntent().getExtras();
        bundle.putString("lastActivity","nfc");

        boolean connected;
        bundle.remove("NFCTAG");

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if (!connected) Toast.makeText(getBaseContext(), "Falla la coneccion a internet!!!!", Toast.LENGTH_LONG).show();
        botonvolver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!texto.equals("")){
                    Intent intent = new Intent(NfcActivity.this, pasanopasaActivity.class);
                    bundle.putInt("idStadium",bundle.getInt("idStadium"));
                    bundle.remove("idSocio");
                    //bundle.putInt("idSocio",Integer.valueOf(editText.getText().toString()));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else Toast.makeText(getBaseContext(), "Volver a pasar tarjeta", Toast.LENGTH_LONG).show();
            }
        });

        benviarid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!editText.getText().toString().equals("")){
                    Intent intent = new Intent(NfcActivity.this, pasanopasaActivity.class);
                    bundle.putInt("idStadium",bundle.getInt("idStadium"));
                    bundle.remove("NFCTAG");

                    bundle.putInt(dato,Integer.valueOf(editText.getText().toString()));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else Toast.makeText(getBaseContext(), "Indicar Socio", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
            Toast.makeText(getBaseContext(), "Volviendo al menu", Toast.LENGTH_LONG).show();
            bundle.putInt("idStadium",bundle.getInt("idStadium"));
            bundle.putInt("Puerta",bundle.getInt("Puerta"));
            Intent intent = new Intent(NfcActivity.this, MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.buttonsocio:
                if (checked)
                    dato="idSocio";
                    break;
            case R.id.buttondni:
                if (checked)
                    dato="documento";
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

        }else Toast.makeText(getBaseContext(), "Prender el NFC desde el menu!!!!", Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onNewIntent(Intent intent) {

        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            if ((findViewById(R.id.text)) != null) {
                texto= ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));

                if (findViewById(R.id.text) != null) {
                    ((TextView) findViewById(R.id.text)).setText( "NFC Tag " +texto);
                    bundle.putString("NFCINVITADO",texto);


                    if (!Objects.equals(texto,"")){
                        intent = new Intent(NfcActivity.this, pasanopasaActivity.class);
                        bundle.putInt("idStadium",bundle.getInt("idStadium"));
                        bundle.remove("idSocio");
                        //bundle.putInt("idSocio",Integer.valueOf(editText.getText().toString()));
                        intent.putExtras(bundle);
                        startActivity(intent);

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
        signo=0;
        byte buffer[] = new byte[inarray.length];
       // buffer=inarray;

        for (j = inarray.length; j >0 ; j--){

            buffer[buffer.length-j]=inarray[j-1];
        }

        if (buffer[buffer.length-1]<0){
            signo=1;

            for (j = inarray.length; j >0 ; j--){

                buffer[buffer.length-j]= (byte) ~buffer[buffer.length-j];
            }
            buffer[buffer.length-1]= (byte) ((byte) 0x7f &buffer[buffer.length-1]);
            buffer[0]++;
        }

        for (j = buffer.length; j >0 ; j--) {

            in = (int) buffer[j-1] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }



        if(signo==1){

            Resultado =-Long.parseLong(out,16);

        }else Resultado =Long.parseLong(out,16);


        return String.valueOf(Resultado);
    }

}
