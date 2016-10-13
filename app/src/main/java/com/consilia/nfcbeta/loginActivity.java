package com.consilia.nfcbeta;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity   {

    Button entrar;
    EditText  IP;
    Bundle bundle;
    String stringsoap;
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
