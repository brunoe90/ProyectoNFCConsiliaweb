package com.consilia.nfcbeta;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



public class ServerActivity extends AppCompatActivity {
    private static String SOAP_ACTION = "http://tempuri.org/HelloWorld";

    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME = "HelloWorld";

    private static String URL = "http://bimbim.in/Sample/TestService.asmx?WSDL";
    TextView dato, tresultado;
    private String celsius;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);


        final EditText edt = (EditText)findViewById(R.id.valor);
        btn = (Button)findViewById(R.id.btn);
        tresultado = (TextView)findViewById(R.id.tresultado);
        dato = (TextView)findViewById(R.id.dato);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert edt != null;
                if (edt.length() > 0) {
                    //getCelsius(edt.getText().toString());
                } else {
                    tresultado.setText("Fahrenheit value can not be empty.");
                }
            }
        });
/////////////////////////////////////////////////////////////////////////////////////////////////////

        Thread thread = new Thread() {

            @Override
            public void run() {

                try {
                    {
                        sleep(100);
                                 //Initialize soap request + add parameters
                        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                        //Use this to add parameters
                        //request.addProperty("Parameter","Value");

                        //Declare the version of the SOAP request
                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                        envelope.setOutputSoapObject(request);

                        //Needed to make the internet call
                        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                        try {
                            //this is the actual part that will call the webservice
                            androidHttpTransport.call(SOAP_ACTION, envelope);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Get the SoapResult from the envelope body.
                        SoapObject result = (SoapObject)envelope.bodyIn;

                    }
                    } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            };
            thread.start();

        Bundle bundle = getIntent().getExtras();
        if ((bundle.getString("NFCTAG")) != null) {
            ((TextView) findViewById(R.id.dato)).setText("NFC Tag " + bundle.getString("NFCTAG"));
        }

        if ((bundle.getString("QRCONTENIDO")) != null) {
            ((TextView) findViewById(R.id.dato)).setText("Contentido :" + bundle.getString("QRCONTENIDO"));
        }


    }
    public Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {

                case 0:
                    tresultado.setText(celsius);
                    break;
            }
            return false;
        }
    });

    private void getCelsius(final String toConvert) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SoapRequests ex = new SoapRequests();
                celsius = ex.getCelsiusConversion(toConvert);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }
}
