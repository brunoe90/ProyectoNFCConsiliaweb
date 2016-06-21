package com.consilia.nfcbeta;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

public class NfcActivity extends AppCompatActivity {

    Button botonvolver, benviarid;
    EditText editText;

    final Bundle bundle= new Bundle();

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

        botonvolver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent = new Intent(NfcActivity.this, ServerActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        benviarid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                bundle.putInt("idSocio",Integer.valueOf(editText.getText().toString()));
                Intent intent = new Intent(NfcActivity.this, ServerActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
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
                ((TextView) findViewById(R.id.text)).setText("NFC Tag " + ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
            }
            Log.d("asd", NfcAdapter.EXTRA_ID);
            Log.d("asd", ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
            bundle.putString("NFCTAG",ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));

        }
    }

    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        long  a;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = inarray.length; j >0 ; j--) {
            in = (int) inarray[j-1] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        //try{
          a =Long.parseLong(out,16);

        /*}  catch (Exception q) {
        q.printStackTrace();
        }*/
        return String.valueOf(a);
    }




}
