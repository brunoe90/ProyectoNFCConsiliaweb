package com.consilia.nfcbeta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends Activity {

    //String str = null;
    /**
     * Called when the activity is first created.
     */
    TextView tv, formatTxt,contentTxt;
    //EditText ettemp;
    //byte[] send_data = new byte[1024];
    //byte[] receiveData = new byte[1024];
    //String modifiedSentence;// celcius, fahren;
    Button bt1;
    ImageButton bnfc, bt4;
    Bundle bundle;
    //String ip;
    //String puerto;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        tv = (TextView) findViewById(R.id.text);
        formatTxt = (TextView) findViewById(R.id.formatTxt);
        contentTxt  = (TextView)findViewById(R.id.contentTxt);
        bt1 = (Button) findViewById(R.id.button1);
        bt4 = (ImageButton) findViewById(R.id.button4);
        bnfc= (ImageButton) findViewById(R.id.buttonNFC);
        bundle = new Bundle();

        bnfc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, NfcActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, QRBarcodeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        SoapRequests ex = new SoapRequests();
                        String stringsoap = ex.getversion();
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

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            Context context = getApplicationContext();
            switch (msg.what) {

                case 1:{ Toast toast = Toast.makeText(context, "Se conecto al servidor!", Toast.LENGTH_SHORT);
                toast.show(); break;
                }
                case 0:{
                Toast toast = Toast.makeText(context, "No se logro conectar!", Toast.LENGTH_LONG);
                toast.show();break;
                }

            }
            return false;
        }
    });

   /* public void client() throws IOException {

        Thread thread = new Thread() {

            @Override
            public void run() {

                try {
                    {
                        sleep(100);
                        bundle = getIntent().getExtras();

                        ip = bundle.getString("ip");
                        //puerto = bundle.getString("puerto");
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
    }*/


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
    /*
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }// */
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }
*/

     /* NFC ACTIVITY
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
    }*/

    // SACAR */
    /*public void getFahrenheit(String celsius) {
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
    }*/



    /*private class AsyncCallWS extends AsyncTask<String, Void, Void> {
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

    }*/


}