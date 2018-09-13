package com.consilia.nfcbeta;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class loginActivity extends AppCompatActivity   {

    Button entrar;
    EditText  IP;
    Bundle bundle;
    String stringsoap;
    SoapObject GetStadiums_Response;
    FileOutputStream myFileOutput;
    FileInputStream myFileInput;
    String myFilename = "configFile.cfg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        entrar  = (Button) findViewById(R.id.ENTRAR);
        //port    = (EditText)findViewById(R.id.port);
        IP      = (EditText)findViewById(R.id.ip);
        bundle = new Bundle();

        if (bundle.getString("IP")!=(null)){
            IP.setText(bundle.getString("IP"));
        }
//        if (bundle.getString("port")!=(null)){
//            port.setText(bundle.getString("port"));
//        }



        entrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        SoapRequests ex = new SoapRequests();
                        stringsoap = ex.GetStadiums(IP.getText().toString());
                        //stringsoap = ex.getversion(IP.getText().toString());//paradebug

                        // Guarda en archivo ##############################################################
                        String strAux = IP.getText().toString();

                        // Al presionar el boton... guardamos en un archivo...
                        try{
                            myFileOutput = openFileOutput(myFilename, Context.MODE_PRIVATE);
                            myFileOutput.write( strAux.getBytes() );
                            myFileOutput.close();
                        }
                        catch ( Exception ignored){

                        }
                        // ###############################################################################



                        if (stringsoap!=null ) {
                            if (!stringsoap.equals("0")) {
                                handler.sendEmptyMessage(1);
                            } else {
                                handler.sendEmptyMessage(0);
                            }
                        }else handler.sendEmptyMessage(0);

                    }

                }).start();

            }
        });

        // Recupera texto ################################################################
        try {
            // Abrimos stream y creamos buffer...
            myFileInput                         = openFileInput(myFilename);
            InputStreamReader inputStreamReader = new InputStreamReader( myFileInput );
            BufferedReader bufferedReader       = new BufferedReader( inputStreamReader );

            // Obtenemos string
            String strAux = bufferedReader.readLine();

            // Grabamos string
            IP.setText( strAux );

            // Cerramos flujo
            myFileInput.close();
        }
        catch (Exception ignored){

        }
        // ################################################################################



    }


    public Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {

                case 1: // Conexion exitosa con el server
                    String estadios[] =stringsoap.split(";");

                    bundle.putStringArray("Estadios",estadios);
                    bundle.putString("IP",IP.getText().toString());
                    Intent intent = new Intent(loginActivity.this, ConfigActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;

                case 0: // Error de conexion con el server!break;
                    Toast.makeText(getBaseContext(), "Falla la coneccion a internet!!!!", Toast.LENGTH_LONG).show();
                    break;

                default:
            }

            return false;
        }
    });

        // benviaridinvitado = (Button) findViewById(R.id.binvitado);


}
