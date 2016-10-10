package com.consilia.nfcbeta;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    String EstadioCrudo[];
    String estadios[];
    String EstadioPos[];
    int  idStadium,Puerta;
    //Typeface face= Typeface.createFromAsset(getAssets(),"fonts/digital.ttf");
    //txtV.setTypeface(face);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        bundle = new Bundle();
        spinner = (Spinner) findViewById(R.id.Estadio);
        etip        = (EditText)findViewById(R.id.etstadio);
        bundle = getIntent().getExtras();

        idStadium =     (bundle.getInt("idStadium"));
        Puerta =        (bundle.getInt("Puerta"));

        if (Puerta!=0){
            etip.setText(String.valueOf(Puerta));
        }

        EstadioCrudo = bundle.getStringArray("Estadios");
        assert EstadioCrudo != null;
        estadios = new String[EstadioCrudo.length/2];
        EstadioPos= new String[EstadioCrudo.length/2];
        int j=0;
        for(int i = 0; i!=(EstadioCrudo.length); i++){
            if ( (i %2)==0 ) {

                EstadioPos[i/2]=EstadioCrudo[i];
                            //even... PAR
            } else {

                estadios[j]=EstadioCrudo[i];
                j++;
                            //odd... IMPAR
            }
        }

        //assert etip != null;
        //etip.setTypeface(face);

        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if (!connected) Toast.makeText(getBaseContext(), "Falla la coneccion a internet!!!!", Toast.LENGTH_LONG).show();

        ArrayAdapter<String> dataAdapter;

        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estadios);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //   ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,estadios, android.R.layout.simple_spinner_item);
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinner.setAdapter(adapter);
//        adapter.setDropDownViewResource(R.layout.spinner_item);


        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {

                        idStadium=Integer.parseInt(EstadioPos[position]);

                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        etip.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!etip.getText().toString().equals("")){
                        bundle.putInt("idStadium",idStadium);
                        bundle.putInt("Puerta", Integer.valueOf(etip.getText().toString()));
                        bundle.putString("IP",bundle.getString("IP"));
                        bundle.putString("port",bundle.getString("port"));
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
            bundle.putString("IP",bundle.getString("IP"));
            bundle.putString("port",bundle.getString("port"));
            Intent intent = new Intent(ConfigActivity.this, loginActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

}



