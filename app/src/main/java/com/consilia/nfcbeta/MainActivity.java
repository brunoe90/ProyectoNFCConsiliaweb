package com.consilia.nfcbeta;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Objects;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


//import com.example.webserviceactivity.R;

/*'     (Android modules i.MX51 and i.MX53) ' Android program, when this application runs on the
        android device, it will show "temp" and "humi" buttons on the android UI, and as we click
        on those buttons it will communicate with the UDPserver.
*/
public class MainActivity extends Activity {
    //private static final String host = null;
    // private int port;
    String str = null;
    /**
     * Called when the activity is first created.
     */
    TextView tv, txt1;
    EditText ettemp;
    byte[] send_data = new byte[1024];
    byte[] receiveData = new byte[1024];
    String modifiedSentence, celcius, fahren;
    Button bt1, bt2, bt3, bt4;
    Bundle bundle;
    String ip;
    String puerto;

    // list of NFC technologies detected:
    private final String[][] techList = new String[][]{
            new String[]{
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //txt1   = (TextView)findViewById(R.id.textView1);

        tv = (TextView) findViewById(R.id.tv);
        txt1  = (TextView)findViewById(R.id.text);
        bt1 = (Button) findViewById(R.id.button1);
        bt2 = (Button) findViewById(R.id.button2);
        bt3 = (Button) findViewById(R.id.button3);
        bt4 = (Button) findViewById(R.id.button4);
        //AsyncCallWS task = new AsyncCallWS();
        //Call execute
        //task.execute();

        bt4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //textIn.setText("test");
                //txt2.setText("text2");
                //task.execute(null);
                str = "temp";
                try {
                    client();
                    //txt1.setText(modifiedSentence);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });

        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //textIn.setText("test");
                //txt2.setText("text2");
                //task.execute(null);

                str = "test";
                try {
                    client();
                    //txt1.setText(modifiedSentence);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                // Perform action on click
                //textIn.setText("test");
                //txt2.setText("text2");
                //task.execute(null);
                ettemp = (EditText) findViewById(R.id.ettemp);
                if (ettemp.getText().length() != 0 && !Objects.equals(ettemp.getText().toString(), "")) {
                    //Get the text control value
                    celcius = ettemp.getText().toString();
                    //Create instance for AsyncCallWS
                    AsyncCallWS task = new AsyncCallWS();
                    //Call execute
                    task.execute();
                    //If text control is empty
                } else {
                    tv.setText("Please enter Celcius");
                }

            }

        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void client() throws IOException {


        Thread thread = new Thread() {

            @Override
            public void run() {

                try {
                    {
                        sleep(100);
                        bundle = getIntent().getExtras();

                        ip = bundle.getString("ip");
                        puerto = bundle.getString("puerto");
                        //DatagramSocket client_socket = new DatagramSocket(3996);
                        // InetAddress IPAddress = InetAddress.getByName("192.168.43.133");
                        DatagramSocket client_socket = new DatagramSocket(Integer.parseInt(puerto));
                        InetAddress IPAddress = InetAddress.getByName(ip);


                        //while (true)
                        // {
                        send_data = str.getBytes();
                        //System.out.println("Type Something (q or Q to quit): ");

                        // DatagramPacket send_packet = new DatagramPacket(send_data,str.length(), IPAddress, Integer.parseInt(puerto));
                        DatagramPacket send_packet = new DatagramPacket(send_data, str.length(), IPAddress, 3996);
                        client_socket.send(send_packet);
                        //chandra
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        //client_socket.receive(receivePacket);
                        modifiedSentence = new

                                String(receivePacket.getData()

                        );
                        //System.out.println("FROM SERVER:" + modifiedSentence);

                        modifiedSentence = null;
                        client_socket.close();
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }


        };
        thread.start();
        // }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            ((TextView) findViewById(R.id.text)).setText("NFC Tag " + ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
            Log.d("asd", NfcAdapter.EXTRA_ID);
            Log.d("asd", ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));

        }
    }

    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }


    public void getFahrenheit(String celsius) {
        //Create request
        String METHOD_NAME = "CelsiusToFahrenheit";
        String NAMESPACE = "http://www.w3schools.com/webservices/";
        String SOAP_ACTION = "http://www.w3schools.com/webservices/CelsiusToFahrenheit";
        String URL = "http://www.w3schools.com/webservices/tempconvert.asmx";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        //Set Name
        celsiusPI.setName("Celsius");
        //Set Value
        celsiusPI.setValue(celsius);
        //Set dataType
        celsiusPI.setType(double.class);
        //Add the property to request object
        request.addProperty(celsiusPI);
        //Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        //Set output SOAP object
        envelope.setOutputSoapObject(request);
        //Create HTTP call object

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            //Invole web service

            androidHttpTransport.call(SOAP_ACTION, envelope);
            //Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            //Assign it to fahren static variable
            fahren = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //public void buttontemp(View view) {}

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.consilia.nfcbeta/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.consilia.nfcbeta/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i("TAG", "doInBackground");
            getFahrenheit(celcius);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            tv.setText(fahren + "° F");
        }

        @Override
        protected void onPreExecute() {
            Log.i("TAG", "onPreExecute");
            tv.setText("Calculating...");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }

    }


}