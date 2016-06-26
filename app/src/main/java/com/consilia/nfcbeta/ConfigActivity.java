package com.consilia.nfcbeta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class ConfigActivity extends AppCompatActivity {
    Bundle bundle;
    EditText etip;// etpuerto;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        bundle = new Bundle();
        spinner = (Spinner) findViewById(R.id.Estadio);
        etip        = (EditText)findViewById(R.id.etstadio);
        //etpuerto    = (EditText)findViewById(R.id.etpuerto);
        //assert etip != null;
        //bundle.putString("ip",etip.getText().toString());
       // bundle.putString("puerto",etpuerto.getText().toString())
    }
    public void bentrar(View view) {
        bundle.putInt("idStadium",Integer.valueOf(etip.getText().toString()));
        Intent intent = new Intent(ConfigActivity.this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
