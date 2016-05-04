package com.consilia.nfcbeta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ConfigActivity extends AppCompatActivity {
    Bundle bundle;
    EditText etip, etpuerto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        bundle = new Bundle();
        etip        = (EditText)findViewById(R.id.etip);
        etpuerto    = (EditText)findViewById(R.id.etpuerto);

        bundle.putString("ip",etip.getText().toString());
        bundle.putString("puerto",etpuerto.getText().toString());



    }
    public void bentrar(View view) {
        Intent intent = new Intent(ConfigActivity.this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
