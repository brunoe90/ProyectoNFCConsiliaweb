package com.consilia.nfcbeta;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class ConfigActivity extends AppCompatActivity {
    Bundle bundle;
    EditText etip;
    Spinner spinner;
    int  idStadium;
    //Typeface face= Typeface.createFromAsset(getAssets(),"fonts/digital.ttf");
    //txtV.setTypeface(face);
    private static final int PROTEINS = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        bundle = new Bundle();
        spinner = (Spinner) findViewById(R.id.Estadio);
        etip        = (EditText)findViewById(R.id.etstadio);
        bundle = getIntent().getExtras();





        //assert etip != null;
        //etip.setTypeface(face);

        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if (!connected) Toast.makeText(getBaseContext(), "Falla la coneccion a internet!!!!", Toast.LENGTH_LONG).show();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.country_arrays, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
       // spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        switch (position){
                            case 1:idStadium =11; break;
                            case 2:idStadium =23; break;
                            case 3:idStadium =25; break;
                            case 4:idStadium =21; break;
                            case 5:idStadium =24; break;
                            case 6:idStadium =20; break;
                            case 7:idStadium =26; break;
                            case 8:idStadium =22; break;
                            case 9:idStadium =15; break;
                            case 10:idStadium =28; break;
                            case 11:idStadium =27; break;
                            case 12:idStadium =29; break;
                            case 13:idStadium =9; break;
                            case 14:idStadium =10; break;
                            case 15:idStadium =19; break;
                            case 16:idStadium =8; break;
                            case 17:idStadium =30; break;
                            case 18:idStadium =12; break;
                            case 19:idStadium =2; break;
                            case 20:idStadium =13; break;
                            case 21:idStadium =17; break;
                            case 22:idStadium =18; break;
                            case 23:idStadium =14; break;





                        }
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


}



