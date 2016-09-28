package com.consilia.nfcbeta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class loginActivity extends AppCompatActivity   {

    Button entrar;
    EditText port, IP;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        entrar  = (Button) findViewById(R.id.ENTRAR);
        port    = (EditText)findViewById(R.id.port);
        IP      = (EditText)findViewById(R.id.ip);
        bundle = new Bundle();

        entrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bundle.putString("IP",IP.getText().toString());
                bundle.putString("port",port.getText().toString());
                Intent intent = new Intent(loginActivity.this, ConfigActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }



        // benviaridinvitado = (Button) findViewById(R.id.binvitado);


}
