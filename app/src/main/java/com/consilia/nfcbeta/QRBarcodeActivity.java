package com.consilia.nfcbeta;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class QRBarcodeActivity extends AppCompatActivity {

    Button bqr,benviar,benviarid;
    TextView formatTxt,contentTxt;
    Bundle bundle;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrbarcode);

        final Activity activity= this;
        bqr = (Button) findViewById(R.id.bqr);
        benviar = (Button) findViewById(R.id.benviar);
        benviarid = (Button)findViewById(R.id.bidsocio);
        editText = (EditText)findViewById(R.id.editText4);
        bundle= new Bundle();

        bqr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                IntentIntegrator scanIntegrator = new IntentIntegrator( activity);
                scanIntegrator.initiateScan();
            }
        });

        benviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(QRBarcodeActivity.this, ServerActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        benviarid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                bundle.putInt("idSocio",Integer.valueOf(editText.getText().toString()));
                Intent intent = new Intent(QRBarcodeActivity.this, ServerActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
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
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
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
