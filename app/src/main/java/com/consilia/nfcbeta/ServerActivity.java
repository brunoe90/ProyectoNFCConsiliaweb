package com.consilia.nfcbeta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ServerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);



        Bundle bundle = getIntent().getExtras();
        if ((bundle.getString("NFCTAG")) != null) {
            ((TextView) findViewById(R.id.text)).setText("NFC Tag " + bundle.getString("NFCTAG"));
        }

        if ((bundle.getString("QRCONTENIDO")) != null) {
            ((TextView) findViewById(R.id.text)).setText("Contentido :" + bundle.getString("QRCONTENIDO"));
        }


    }
}
