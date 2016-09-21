package com.consilia.nfcbeta;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class pasanopasaActivity extends AppCompatActivity {
    TextView    dato,tresultado;
    String      stringsoap;
    String      idStadium;
    String      idSocio;
    String      Puerta;
    String      documento;
    String      Tarjeta;
    String      Invitado;
    Button      buttonfoto;
    Button      bvolver;
    Context     context;
    Bitmap      decodedByte = null;
    ImageView   imageView;
    Bundle      bundle;
    String      numSocio;
    String      tipodoc ="DNI";
    String      UltimaActivity;
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasanopasa);

        bvolver   =     (Button) findViewById(R.id.bvolver);
        buttonfoto =    (Button) findViewById(R.id.buttonfoto);
        bundle =        new Bundle();
        bundle =        getIntent().getExtras();
        imageView =     (ImageView) findViewById(R.id.imageView);
        tresultado =    (TextView) findViewById(R.id.tresultado);
        dato =          (TextView) findViewById(R.id.dato);
        idSocio =       String.valueOf(bundle.getInt("idSocio"));
        idStadium =     String.valueOf(bundle.getInt("idStadium"));
        Puerta =        String.valueOf(bundle.getInt("Puerta"));
        documento =     String.valueOf(bundle.getInt("documento"));
        Tarjeta =       bundle.getString("NFCTAG");
        Invitado =      bundle.getString("NFCINVITADO");
        UltimaActivity = bundle.getString("lastActivity");
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if (!connected) Toast.makeText(getBaseContext(), "Falla la coneccion a internet!!!!", Toast.LENGTH_LONG).show();

        if (null != idStadium){

            if (null!=Puerta){

                assert UltimaActivity != null;
                if (UltimaActivity.equals("qr")){
                    if (!Objects.equals("0", idSocio)){
                        getSoap("getestado");
                    }else if (!Objects.equals("0", documento)){
                        getSoap("buscar");
                    }
                }
                if (UltimaActivity.equals("nfc")){
                    if (!Objects.equals("0", idSocio)){
                        getSoap("getestado");
                    }else if (null!= Tarjeta){
                        getSoap("getcarnet");
                    }else if (!Objects.equals("0", Invitado)){
                        getSoap("getinvitado");
                    }
                }
            }   else Toast.makeText(getBaseContext(), "Falla al encontrar Puerta", Toast.LENGTH_LONG).show();
        } else Toast.makeText(getBaseContext(), "Falla al encontrar idStadio", Toast.LENGTH_LONG).show();

        buttonfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != idStadium){
                    if (null!=Puerta){
                        getSoap("GetFotoSocio");
                    }   else Toast.makeText(getBaseContext(), "Falla al encontrar Puerta", Toast.LENGTH_LONG).show();
                } else Toast.makeText(getBaseContext(), "Falla al encontrar idStadio", Toast.LENGTH_LONG).show();
            }
        });//

        bvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assert UltimaActivity != null;
                if (UltimaActivity.equals("qr")){
                    Intent intent = new Intent(pasanopasaActivity.this, QRBarcodeActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (UltimaActivity.equals("nfc")){
                    Intent intent = new Intent(pasanopasaActivity.this, NfcActivity.class);

                    if (Invitado!=null){
                        bundle.putInt("TAB", 2);
                    } else if (Tarjeta!=null){
                        bundle.putInt("TAB", 0);
                    } else bundle.putInt("TAB", 1);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
            Toast.makeText(getBaseContext(), "Volviendo al menu", Toast.LENGTH_LONG).show();
            bundle.putInt("idStadium",Integer.valueOf(idStadium));
            bundle.putInt("Puerta", Integer.valueOf(Puerta));
            Intent intent = new Intent(pasanopasaActivity.this, MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);

            // Double beeps:     tg.startTone(ToneGenerator.TONE_PROP_ACK);
            // Double beeps:     tg.startTone(ToneGenerator.TONE_PROP_BEEP2);
            // Sounds all wrong: tg.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE);
            // Simple beep:      tg.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
            switch (msg.what) {

                case 0: tresultado.setText(stringsoap); break;

                case 1:{
                    try {
                        imageView.setImageBitmap(/*getRoundedShape*/(decodedByte));
                    } catch (Exception q) {
                        q.printStackTrace();
                    }
                    break;}

                case 2: tresultado.setText(stringsoap); getSoap("getestado");break;

                case 3: {
                    String i = "";
                    boolean pasa= false;
                    int a = stringsoap.indexOf("Puertas=");
                    int b = stringsoap.indexOf("Activo");
                    int c = stringsoap.indexOf("vencida");
                    if (c>0){
                        dato.setTextColor(Color.parseColor("#F44336")); tresultado.setTextColor(Color.parseColor("#F44336"));
                        tresultado.setText("Acceso no permitido");
                        //dato.setText("El ingresante tiene la cuota vencida");
                        i="El ingresante tiene la cuota vencida";
                        toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                    }else if (a<1) {
                        if (idSocio.equals("0")){
                            i="Usuario no encontrado!";
//                            dato.setTextColor(Color.parseColor("#F44336"));
                            tresultado.setTextColor(Color.parseColor("#F44336"));
                            tresultado.setText(i);
                            toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                        }else if (stringsoap.equals("0")) {
                            i="TARJETA NO VALIDA";
//                            dato.setTextColor(Color.parseColor("#F44336"));
                            tresultado.setTextColor(Color.parseColor("#F44336"));
                            tresultado.setText("Tarjeta no encontrada en base de datos");
                            toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                        }else{
                            i="NO ESTA DEFINIDA PUERTA";
//                            dato.setTextColor(Color.parseColor("#F44336")); tresultado.setTextColor(Color.parseColor("#F44336"));
                            tresultado.setText("No se encuentra la puerta indicada");
                            dato.setText(i);

                            toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                        }


                    }else if (b<1){
                        i="SOCIO NO ACTIVO";
//                        dato.setTextColor(Color.parseColor("#F44336"));
                        tresultado.setTextColor(Color.parseColor("#F44336"));
                        tresultado.setText(stringsoap.replace("=",":"));
                        buttonfoto.performClick();
                    }
                    else{
                        i= stringsoap.substring(a);
                        buttonfoto.performClick();
                       // i = i.substring(0, i.indexOf("Puerta"));
                        stringsoap = stringsoap.replace(i,"");
                       // i = i.replace(";","").replace("=","= ");
                        if (i.contains("Puerta")){

                            if (i.contains(Puerta)){
//                                dato.setTextColor(Color.parseColor("#4CAF50"));
                                tresultado.setTextColor(Color.parseColor("#4CAF50"));
                                Toast toast = Toast.makeText(context, "PASA", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP| Gravity.CENTER, 10, 90);
                                toast.show();
                                //ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                                pasa=true;


                            } else {
//                                dato.setTextColor(Color.parseColor("#F44336"));
                                tresultado.setTextColor(Color.parseColor("#F44336"));
                                Toast toast = Toast.makeText(context, "ACCESO NO PERMITIDO", Toast.LENGTH_LONG);
                             //   Toast.makeText(getBaseContext(), "ACCESO NO PERMITIDO", Toast.LENGTH_LONG).show();
                                toast.setGravity(Gravity.TOP| Gravity.CENTER, 10, 90);
                                toast.show();
                                //ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                            }
                        }
                        tresultado.setText(stringsoap.replace("=",":") );
                    }

                    i=i.replace("Puerta","").replace("s","").replace("=",": ").replace("  "," ");
                    if (pasa) i= "Puertas" + i;

                    dato.setText(i+".");
                    break;}

                case 4: {
                    tresultado.setText(stringsoap);
                    getSoap("getestado");
                    break;
                }
                case 5: {
                    tresultado.setText(stringsoap);
                    //getSoap("getestado");
                    break;
                }

                default: Toast.makeText(getBaseContext(), "Fallo comando", Toast.LENGTH_LONG).show(); break;
            }
            return false;
        }
    });
    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 200;
        int targetHeight = 200;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                            targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
            ((float) targetHeight - 1) / 2,
            (Math.min(((float) targetWidth),
            ((float) targetHeight)) / 2),
            Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
            new Rect(0, 0, sourceBitmap.getWidth(),
            sourceBitmap.getHeight()),
            new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    private void getSoap( final String method) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SoapRequests ex = new SoapRequests();
                context = getApplicationContext();
                switch (method) {
                    case "getversion":{
                        stringsoap =    ex.getversion();
                        handler.sendEmptyMessage(0);
                        break;}

                    case "GetFotoSocio":{
                        byte[] foto =   ex.getfotosocio((idStadium),String.valueOf(idSocio ));
                        decodedByte =   BitmapFactory.decodeByteArray(foto, 0, foto.length);
                        handler.sendEmptyMessage(1);
                        break;}

                    case "buscar":{

                        numSocio =      ex.getsociobydoc((idStadium), String.valueOf(idSocio ), tipodoc); //type="s:int" devuelve entero ////////// ENTRA unsigned byte idStadium -- string idTipoDoc -- long documento
                        stringsoap =    numSocio;
                        idSocio=        numSocio;
                        handler.sendEmptyMessage(2);
                        break;}

                    case "getestado":{

                        numSocio =      ex.getsocio((idStadium),String.valueOf(idSocio ),String.valueOf(idSocio ) ); //type="s:int" devuelve entero ////////// ENTRA unsigned byte idStadium -- string idTipoDoc -- long documento
                        stringsoap =    numSocio;
                        handler.sendEmptyMessage(3);
                        break;}

                    case "getcarnet":{
                        stringsoap =    ex.getcaret(idStadium,bundle.getString("NFCTAG"));
                        idSocio=        stringsoap;
                        if (stringsoap==null){
                            idSocio="0";
                        }

                        handler.sendEmptyMessage(4);
                        break;}
                    case "getinvitado":{
                        stringsoap =    ex.SearchInvitado(idStadium,bundle.getString("NFCINVITADO"));
                        idSocio=        stringsoap;
                        if (stringsoap==null){
                            idSocio="0";
                        }

                        handler.sendEmptyMessage(5);


                        break;
                    }
                }
            }
        }).start();
    }
}
