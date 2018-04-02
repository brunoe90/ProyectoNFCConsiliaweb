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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;


public class ConfigActivity extends AppCompatActivity {
    Bundle bundle;
    EditText etip;
    Spinner spinner;
    String EstadioCrudo[];
    String estadios[];
    String EstadioPos[];
    int  idStadium;
    String Puerta;
    int posicion;

    FileOutputStream myFileOutput;
    FileInputStream myFileInput;
    String myFilename = "configFilePuerta.cfg";
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
        Puerta =        (bundle.getString("Puerta"));
        // Recupera texto ################################################################
        try {
            // Abrimos stream y creamos buffer...
            myFileInput                         = openFileInput(myFilename);
            InputStreamReader inputStreamReader = new InputStreamReader( myFileInput );
            BufferedReader bufferedReader       = new BufferedReader( inputStreamReader );

            // Obtenemos string
            String strAux = bufferedReader.readLine();

            // Grabamos string
            etip.setText( strAux );

            // Cerramos flujo
            myFileInput.close();
        }
        catch (Exception ignored){

        }
        // ################################################################################

        if (Puerta != null){
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


        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if (!connected) Toast.makeText(getBaseContext(), "Falla la coneccion a internet!!!!", Toast.LENGTH_LONG).show();

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estadios);
        spinner.setAdapter(dataAdapter);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        if (bundle.getInt("pos")!=0){
            spinner.setSelection(bundle.getInt("pos"));
        }


        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {

                        idStadium=Integer.parseInt(EstadioPos[position]);
                        posicion=position;
                        bundle.putInt("pos",posicion);

                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        etip.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!etip.getText().toString().equals("")){
                        bundle.putInt("idStadium",idStadium);
                        bundle.putString("Puerta", (etip.getText().toString()));

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
            bundle.putString("Puerta", (etip.getText().toString()));
            bundle.putInt("idStadium", idStadium);
            // Guarda en archivo ##############################################################
            //String strAux = IP.getText().toString();

            // Al presionar el boton... guardamos en un archivo...
            try{
                myFileOutput = openFileOutput(myFilename, Context.MODE_PRIVATE);
                myFileOutput.write((etip.getText().toString().getBytes()));
                myFileOutput.close();
            }
            catch ( Exception ignored){

            }
            // ###############################################################################
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



