package com.consilia.nfcbeta;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ConfigActivity extends AppCompatActivity {
    Bundle bundle;
    EditText etip;
    Spinner spinner;
    int  idStadium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        bundle = new Bundle();
        spinner = (Spinner) findViewById(R.id.Estadio);
        etip        = (EditText)findViewById(R.id.etstadio);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.country_arrays, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
       // spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        idStadium=position;
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        etip.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!etip.getText().toString().equals("")){
                        bundle.putInt("idStadium",Integer.valueOf(etip.getText().toString()));
                        bundle.putInt("Puerta", Integer.valueOf(etip.getText().toString()));
                        Intent intent = new Intent(ConfigActivity.this, MainActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else Toast.makeText(getBaseContext(), "Indicar Puerta de acceso", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });
    }
    public void bentrar(View view) {
        if (!etip.getText().toString().equals("")) {
            bundle.putInt("Puerta", Integer.valueOf(etip.getText().toString()));
            bundle.putInt("idStadium", idStadium);
            Intent intent = new Intent(ConfigActivity.this, MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else Toast.makeText(getBaseContext(), "Indicar Puerta de acceso", Toast.LENGTH_LONG).show();
    }

}
