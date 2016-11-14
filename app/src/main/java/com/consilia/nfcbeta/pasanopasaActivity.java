package com.consilia.nfcbeta;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class pasanopasaActivity extends AppCompatActivity {
    TextView    dato,datop,tresultado,Accesos,informacion;
    String      stringsoap;
    String      idStadium;
    String      idSocio/*,idInvitado*/;
    String      Puerta;
    //String      documento,DocInvitado;
    String      Tarjeta="";
    Button      buttonfoto;
    Button      bvolver;
    Context     context;
    Bitmap      decodedByte = null;
    ImageView   imageView;
    Bundle      bundle;
    String      numSocio;
    String      tipodoc ="DNI";
    String      UltimaActivity;
    String      idTipo;
    String      datomanual;
    String      TipoSocio;
    int         NumeroAconvertir;
    int signo = 0;
    long Resultado = 0;
    String texto = "";
    String NFC = "";

    private GoogleApiClient client;

    public void setUltimaActivity(String val){
        this.UltimaActivity = val;

    }

    public String getUltimaActivity(){
        return this.UltimaActivity;

    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasanopasa);

        bvolver   =     (Button) findViewById(R.id.bvolver);
        buttonfoto =    (Button) findViewById(R.id.buttonfoto);
        bundle =        new Bundle();
        bundle =        getIntent().getExtras();
        bundle.putString("IP",bundle.getString("IP"));
        bundle.putString("port",bundle.getString("port"));
        imageView =     (ImageView) findViewById(R.id.imageView);
        tresultado =    (TextView) findViewById(R.id.tresultado);
        Accesos =       (TextView) findViewById(R.id.Puertas);
        informacion =   (TextView) findViewById(R.id.informacion);
        dato =          (TextView) findViewById(R.id.dato);
        datop=          (TextView) findViewById(R.id.datop);
        idSocio =       String.valueOf(bundle.getInt("idSocio"));
        idStadium =     String.valueOf(bundle.getInt("idStadium"));
        Puerta =        bundle.getString("Puerta");
        Tarjeta =       bundle.getString("NFCTAG");
        datomanual  =   bundle.getString("manual");
        NumeroAconvertir=bundle.getInt("NumeroAconvertir");

        // Seteo variable ultima medante metodo
        this.setUltimaActivity(bundle.getString("lastActivity"));

        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if (!connected) Toast.makeText(getBaseContext(), "Falla la coneccion a internet!!!!", Toast.LENGTH_LONG).show();

        if (null != idStadium||idStadium.equals("")){

            assert Puerta != null;
            if (!Puerta.equals("")){

                assert UltimaActivity != null;
                //getSoap("buscarinvitado");
                if (this.getUltimaActivity().equals("qr")){
                     {
                         if (datomanual!=null){
                             switch (datomanual){
                                 case "idSocio":     {
                                     idSocio=String.valueOf(NumeroAconvertir);
                                     getSoap("getestado");
                                 } break;
                                 case "idInvitado":{
                                     idSocio=String.valueOf(NumeroAconvertir);
                                     getSoap("getestadoinvitado");
                                 } break;
                                 case "DocInvitado":{

                                     getSoap("buscarinvitado");
                                 } break;
                                 case "DocSocio":    {
                                     getSoap("buscar");
                                 }break;
                             }
                         }
                    }
                }
                if (this.getUltimaActivity().equals("nfc")){
                     {
                        if (datomanual!=null){
                            switch (datomanual){
                                case "idSocio":     {
                                    idSocio=String.valueOf(NumeroAconvertir);
                                    getSoap("getestado");
                                } break;
                                case "idInvitado":{
                                    idSocio=String.valueOf(NumeroAconvertir);
                                    getSoap("getestadoinvitado");
                                } break;
                                case "DocInvitado":{

                                    getSoap("buscarinvitado");
                                } break;
                                case "DocSocio":    {
                                    getSoap("buscar");
                                }break;
                            }
                        }
                       if (Tarjeta!=null) getSoap("getcarnet");
                    }
                }
            }   else Toast.makeText(getBaseContext(), "Falla al encontrar Puerta", Toast.LENGTH_LONG).show();
        } else Toast.makeText(getBaseContext(), "Falla al encontrar idStadio", Toast.LENGTH_LONG).show();

        buttonfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });//

        bvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assert UltimaActivity != null;
                if (UltimaActivity.equals("qr")){
                    Intent intent = new Intent(pasanopasaActivity.this, QRBarcodeActivity.class);
                    bundle.putString("IP",bundle.getString("IP"));
                    bundle.putString("port",bundle.getString("port"));
                    bundle.remove("NFCTAG");
                    bundle.remove("idSocio");
                    bundle.remove("manual");
                    bundle.remove("NumeroAconvertir");
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (UltimaActivity.equals("nfc")){
                    Intent intent = new Intent(pasanopasaActivity.this, NfcActivity.class);

                    if (TipoSocio!=null)
                     {
                        if (Tarjeta != null){
                            bundle.putInt("TAB", 0);
                        } else if (TipoSocio.equals("socio")) {
                            bundle.putInt("TAB", 1);
                        }else {
                            bundle.putInt("TAB", 2);
                        }
                    }
                    bundle.remove("NFCTAG");
                    bundle.remove("NFCINVITADO");
                    bundle.remove("idSocio");
                    bundle.remove("manual");
                    bundle.remove("NumeroAconvertir");
                    bundle.putString("IP",bundle.getString("IP"));
                    bundle.putString("port",bundle.getString("port"));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
           // Toast.makeText(getBaseContext(), "Volviendo al menu", Toast.LENGTH_LONG).show();
            bundle.putInt("idStadium",Integer.valueOf(idStadium));
            bundle.putString("Puerta", (Puerta));
            Intent intent = new Intent(pasanopasaActivity.this, MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
    private Handler handler = new Handler(new Handler.Callback() {

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public boolean handleMessage(Message msg) {
            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);

            // Double beeps:     toneG.startTone(ToneGenerator.TONE_PROP_ACK);
            // Double beeps:     toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
            // Sounds all wrong: toneG.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE);
            // Simple beep:      toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
            switch (msg.what) {

                case 0: tresultado.setText(stringsoap); break;

                case 1:{
                    int timeout = stringsoap.indexOf("timeout");
                    if (timeout>0){
                        Toast.makeText(getBaseContext(), "Falla al encontrar Puerta", Toast.LENGTH_LONG).show();
                        dato.setText("SE INTERRUMPIO LA CONEXION");
                        informacion.setText("Intente nuevamente");
                        break;
                    }
                    try {
                        imageView.setImageBitmap(/*getRoundedShape*/(decodedByte));
                    } catch (Exception q) {
                        q.printStackTrace();
                    }
                    break;}

                case 2:{
                    int timeout = stringsoap.indexOf("timeout");
                    if (timeout>0){
                        Toast.makeText(getBaseContext(), "Falla al encontrar Puerta", Toast.LENGTH_LONG).show();
                        dato.setText("SE INTERRUMPIO LA CONEXION");
                        informacion.setText("Intente nuevamente");
                        break;
                    }
                    RelativeLayout layout = (RelativeLayout) findViewById(R.id.pasanopasa);

                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        assert layout != null;
                        layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                    } else {
                        assert layout != null;
                        layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                    }


                    int noexiste= stringsoap.indexOf("no existe");
                    if (0<noexiste){
                        dato.setText(stringsoap);
                    }else{

                        dato.setText("Tarjeta erronea");
                    }


                    toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);

                }break;

                case 3: {
                    RelativeLayout layout = (RelativeLayout) findViewById(R.id.pasanopasa);

                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    String puertas;
                    int timeout = stringsoap.indexOf("timeout");
                    if (timeout>0){
                        stringsoap="0";
                        Toast.makeText(getBaseContext(), "Falla al encontrar Puerta", Toast.LENGTH_LONG).show();
                        dato.setText("SE INTERRUMPIO LA CONEXION");
                        informacion.setText("Intente nuevamente");
                        break;
                    }
                    if (!stringsoap.equals("0"))
                    {
                        int numpuertas = stringsoap.indexOf("Puertas:");
                        if (numpuertas>0){
                            puertas=stringsoap.substring(numpuertas+8);

                            if (puertas.length()>10){
                                Accesos.setTextSize(20);
                            }else if (puertas.length()>30){
                                Accesos.setTextSize(15);
                            }
                            Accesos.setText("Accesos:"+puertas.replace("Puerta","").replace("PUERTA",""));
                        }

                        //compartido
                        int nom= stringsoap.indexOf('\n');
                        if (nom>0){
                            String Nombre = stringsoap.substring(0,nom);
                            if (dato.length()>15){
                                dato.setTextSize(24);
                            }
                            dato.setText(Nombre);
                        }

                        // invitado
                        int IdInvitado = stringsoap.indexOf("Invitado Nº: ");
                        int IdCat      = stringsoap.indexOf("Categoria: ");
                        if (IdInvitado>0){
                            String fintrama = stringsoap.substring(IdCat);
                            String info = stringsoap.substring(IdInvitado, IdCat+fintrama.indexOf('\n'));
                            if (informacion.length()>20){
                                informacion.setTextSize(18);
                            }
                            informacion.setText(info);
                        }

                        //socio
                        int socio = stringsoap.indexOf("Estado del Socio: ");
                        if (socio>0){
                            String info = stringsoap.substring(stringsoap.indexOf('\n')+1, socio-1);
                            if (informacion.length()>20){
                                informacion.setTextSize(22);
                            }
                            informacion.setText(info);
                        }

                        //SOCIO
                        int ucpfinder = stringsoap.indexOf("Ultima Cuota Paga: ");
                        if (ucpfinder>0){
                            String UCP = stringsoap.substring(stringsoap.indexOf("Ultima Cuota Paga: ")+19);
                            UCP = UCP.substring(0,UCP.indexOf("#"));

                            tresultado.setText("UCP: "+UCP);
                        }

                        int EstadoAcceso = stringsoap.indexOf("EstadoAcceso ");
                        if (EstadoAcceso>0){
                            String info = stringsoap.substring(EstadoAcceso);
                            info = info.substring("EstadoAcceso ".length(),info.indexOf('\n'));

                            datop.setText(info);
                            if (datop.length()>15){
                                datop.setTextSize(20);
                            }
                        }

                        int TV = stringsoap.indexOf("TicketVirtual");
                        if (TV>0){
                            String info = stringsoap.substring(TV);
                            info = info.substring(0,info.indexOf('\n'));

                            tresultado.setText(tresultado.getText().toString()+'\n'+info);
                            if (tresultado.length()>25){
                                tresultado.setTextSize(20);
                            }
                        }

                        int callback =stringsoap.indexOf("IdEstado");
                        if (stringsoap.indexOf("IdEstado")>0){
                            idTipo=stringsoap.substring(callback+8,callback+8+4).replace("\n","").replace(" ","").replace(":","");
                        }


                        if (idTipo==null){
                            idTipo="0";
                            Toast.makeText(getBaseContext(), "Falló de conexión", Toast.LENGTH_LONG).show();
                        }
                        switch (Integer.valueOf(idTipo))
                        {

                            case 0: {
                                //no puede pasar

//                                datop.setText("Fue Deshabilitado");
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {
                                    assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }
                                break;
                            }
                            case 1: {

                                {
                                    int puerta = stringsoap.indexOf("Puertas:");
                                    if (puerta>0){
                                        //String info = stringsoap.substring(puerta+"Puertas:".length());
                                        puertas=stringsoap.substring(numpuertas+8);
                                        puertas=puertas.replace("Puerta","").replace("PUERTA","").replace("puerta","").replace("S","").replace("s","").replace(" ","");
                                        String estadios[] =puertas.split(",");
                                        puerta=0;
                                        for (String estadio : estadios) {
                                            if (estadio.equals(Puerta.replace(" ",""))) {
                                                puerta = 1;
                                            }
                                        }
                                        if (puerta==1){
                                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                                assert layout != null;
                                                layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundok) );
                                            } else {
                                                assert layout != null;
                                                layout.setBackground( getResources().getDrawable(R.drawable.backgroundok));
                                            }

//                                            datop.setText("OK");
                                            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                                        }else {

                                            datop.setText("Puerta equivocada");
                                            toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                                assert layout != null;
                                                layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                            } else {
                                                assert layout != null;
                                                layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                            }
                                        }
                                    }else {
//                                        datop.setText("No hay Puertas Asignadas");
                                        datop.setTextSize(20);
                                      //  tresultado.setTextSize(24);
                                        toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                            assert layout != null;
                                            layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                        } else {
                                            assert layout != null;
                                            layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                        }
                                    }
                                }
                                // puede pasar

                                break;
                            }
                            case 2: {
                                // puede pasar
                                    int puerta = stringsoap.indexOf("Puertas:");
                                    if (puerta>0){
                                        //String info = stringsoap.substring(puerta+"Puertas:".length());
                                        puertas=stringsoap.substring(numpuertas+8);
                                        puertas=puertas.replace("Puerta","").replace("PUERTA","").replace(" ","");
                                        String estadios[] =puertas.split(",");
                                        puerta=0;
                                        for (String estadio : estadios) {
                                            if (estadio.equals(Puerta.replace(" ",""))) {
                                                puerta = 1;
                                            }
                                        }

                                        if ( puerta == 1){
                                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                                assert layout != null;
                                                layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundok) );
                                            } else {
                                                assert layout != null;
                                                layout.setBackground( getResources().getDrawable(R.drawable.backgroundok));
                                            }

//                                            datop.setText("Habilit. manual");
                                            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                                        }else {

                                            datop.setText("Puerta equivocada");
                                            toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                                assert layout != null;
                                                layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                            } else {
                                                assert layout != null;
                                                layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                            }
                                        }
                                    }else {
//                                        datop.setText("No hay Puertas Asignadas");
                                        toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                            assert layout != null;
                                            layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                        } else {
                                            assert layout != null;
                                            layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                        }
                                }



                                break;
                            }
                            case 3: {
                                // no puede pasar
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {
                                    assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }


//                                datop.setText("Ya ingreso al estadio");
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);

                                break;
                            }
                            case 4: {

                                //no puede pasar
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {
                                    assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }


//                                datop.setText("No esta activo");
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);


                                break;
                            }
                            case 5: {


                                // no pasa
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {
                                    assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }

//                                datop.setText("Cuota Vencida");
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);


                                break;
                            }
                            case 6: {
                                // Paso por perimetral
                                int puerta = stringsoap.indexOf("Puertas:");
                                if (puerta>0){
                                    //String info = stringsoap.substring(puerta+"Puertas:".length());
                                    puertas=stringsoap.substring(numpuertas+8);
                                    puertas=puertas.replace("Puerta","").replace("PUERTA","").replace(" ","");
                                    String estadios[] =puertas.split(",");
                                    puerta=0;
                                    for (String estadio : estadios) {
                                        if (estadio.equals(Puerta.replace(" ",""))) {
                                            puerta = 1;
                                        }
                                    }

                                    if ( puerta == 1){
                                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                            assert layout != null;
                                            layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundok) );
                                        } else {
                                            assert layout != null;
                                            layout.setBackground( getResources().getDrawable(R.drawable.backgroundok));
                                        }

//                                            datop.setText("Habilit. manual");
                                        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                                    }else {

                                        datop.setText("Puerta equivocada");
                                        toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                            assert layout != null;
                                            layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                        } else {
                                            assert layout != null;
                                            layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                        }
                                    }
                                }else {
//                                        datop.setText("No hay Puertas Asignadas");
                                    toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                        assert layout != null;
                                        layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                    } else {
                                        assert layout != null;
                                        layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                    }
                                }


                                break;
                            } case 7: {
                                // no pasa
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {
                                    assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }


//                                datop.setText("Abono vencido");
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);


                                break;
                            } case 8: {
                                // no pasa
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
//                                datop.setText("Canjeo por ticket");
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {
                                    assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }


                                break;
                            } case 9: {
                                //// no pasa
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {
                                    assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }


//                                datop.setText("Credencial Vencida");
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);

                                break;
                            } case 10: {
                                // No pasa
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {
                                    assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }


//                                datop.setText("Copia no Habilitada");

                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                                break;
                            } case 11: {
                                // no pasa

                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {
                                    assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }


//                                datop.setText("No tiene carnet");
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);

                                break;
                            } case 14: {
                                // no pasa
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {
                                    assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }


//                                datop.setText("Sector incorrecto");

                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                                break;
                            } case 15: {
                                // no pasa
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }


//                                datop.setText("Sector Completo");
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);

                                break;
                            } case 16: {
                                // no pasa

                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {
                                    assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }

//                                datop.setText("Tarjeta Defectuosa");
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);

                                break;
                            }case 17: {
                                // no pasa
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }

//                                datop.setText("Difiere N. de Serie");
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);

                                break;
                            }case 18: {
                                // no pasa
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }

//                                datop.setText("Pasadas Excedidas");
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);

                                break;
                            }case 19: {
                                // no pasa

                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }


//                                datop.setText("Actividad Vencida");

                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                                break;
                            }case 20: {
                                // no pasa

                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }


//                                datop.setText("Fuera de Horario");

                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                                break;
                            }

                            default:{

                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                            }


                        }
                    }
                    break;}
                case 4: {
                    tresultado.setText(stringsoap);
                   // getSoap("getestado");
                    break;
                }
                case 5: {
                    tresultado.setText(stringsoap);

                    handler.sendEmptyMessage(3);
                    break;
                }

                default: Toast.makeText(getBaseContext(), "Fallo comando", Toast.LENGTH_LONG).show(); break;
            }
            return false;
        }
    });


    @Override
    protected void onResume() {
        super.onResume();
        // creating pending intent:
        Context context = this;
        NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            // creating intent receiver for NFC events:
            IntentFilter filter = new IntentFilter();
            filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
            filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
            filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
            // enabling foreground dispatch for getting intent from NFC event:
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);

        }/* else{
                Toast.makeText(getBaseContext(), "Prender el NFC desde el menu!!!!", Toast.LENGTH_LONG).show();
        }*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onNewIntent(Intent intent) {

        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            {
                texto = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));

                {
                    // ((TextView) findViewById(R.id.text)).setText("NFC Tag " + texto);
                    //bundle.putString("NFCINVITADO",texto);////
                    NFC = texto;
                    bundle.putString("NFCTAG",NFC);

                    try {
                        imageView.setImageResource(R.drawable.nofoto);
                    } catch (Exception q) {
                        q.printStackTrace();
                    }

                    tresultado.setText("");
                    Accesos.setText("");
                    informacion.setText("");
                    dato.setText("CARGANDO");
                    datop.setText("");

                    RelativeLayout layout = (RelativeLayout) findViewById(R.id.pasanopasa);

                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        assert layout != null;
                        layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.escritorio) );
                    } else {
                        assert layout != null;
                        layout.setBackground( getResources().getDrawable(R.drawable.escritorio));
                    }

                    getSoap("getcarnet");
                }
            }
            Log.d("asd", NfcAdapter.EXTRA_ID);
            //Log.d("asd", ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));

        }
    }

    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";
        signo = 0;
        byte buffer[] = new byte[inarray.length];
        // buffer=inarray;

        for (j = inarray.length; j > 0; j--) {

            buffer[buffer.length - j] = inarray[j - 1];
        }

        if (buffer[buffer.length - 1] < 0) {
            signo = 1;

            for (j = inarray.length; j > 0; j--) {

                buffer[buffer.length - j] = (byte) ~buffer[buffer.length - j];
            }
            buffer[buffer.length - 1] = (byte) ((byte) 0x7f & buffer[buffer.length - 1]);
            buffer[0]++;
        }

        for (j = buffer.length; j > 0; j--) {

            in = (int) buffer[j - 1] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }


        if (signo == 1) {

            Resultado = -Long.parseLong(out, 16);

        } else Resultado = Long.parseLong(out, 16);


        return String.valueOf(Resultado);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Nfc Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    /*public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
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
        canvas.drawBitmap(scaleBitmapImage,
            new Rect(0, 0, scaleBitmapImage.getWidth(),
            scaleBitmapImage.getHeight()),
            new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }*/

    private void getSoap( final String method) {
        new Thread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                SoapRequests ex = new SoapRequests();
                context = getApplicationContext();
                switch (method) {
                    case "getversion":{
                        stringsoap =    ex.getversion(bundle.getString("IP"));
                        handler.sendEmptyMessage(0);
                        break;}

                    case "GetFotoSocio":{

                        byte[] foto =   ex.getfotosocio((idStadium),String.valueOf(idSocio ),bundle.getString("IP"));
                        decodedByte =   BitmapFactory.decodeByteArray(foto, 0, foto.length);
                        handler.sendEmptyMessage(1);
                        TipoSocio="socio";
                        break;}
                    case "GetFotoInvitado":{
                        TipoSocio="invitado";
                        byte[] foto =   ex.getfotoinvitado((idStadium),String.valueOf(idSocio ),bundle.getString("IP"));
                        decodedByte =   BitmapFactory.decodeByteArray(foto, 0, foto.length);
                        handler.sendEmptyMessage(1);
                        break;}


                    case "buscar":{
                        TipoSocio="socio";
                        numSocio =      ex.getsociobydoc((idStadium), String.valueOf(NumeroAconvertir ), tipodoc,bundle.getString("IP")); //type="s:int" devuelve entero ////////// ENTRA unsigned byte idStadium -- string idTipoDoc -- long documento
                        stringsoap =    numSocio;
                        idSocio=        numSocio;
                        getSoap("getestado");
                        //idTipo="8";
                        //handler.sendEmptyMessage(2);
                        break;}

                    case "getestado":{
                        TipoSocio="socio";
                        numSocio =      ex.getsocio((idStadium),String.valueOf(idSocio ),String.valueOf(idSocio ) ,bundle.getString("IP")); //type="s:int" devuelve entero ////////// ENTRA unsigned byte idStadium -- string idTipoDoc -- long documento
                        stringsoap =    numSocio;

                        int noexiste= stringsoap.indexOf("no existe");
                        if (0<noexiste){
                            handler.sendEmptyMessage(2);
                        }
                        else{
                            getSoap("GetFotoSocio");
                            handler.sendEmptyMessage(3);
                        }

                        break;}

                    case "getestadoinvitado":{
                        numSocio =      ex.SearchInvitado(String.valueOf(idStadium),String.valueOf(idSocio ) ,bundle.getString("IP")); //type="s:int" devuelve entero ////////// ENTRA unsigned byte idStadium -- string idTipoDoc -- long documento
                        stringsoap =    numSocio;
                        TipoSocio="invitado";
                        int noexiste= stringsoap.indexOf("no existe");
                        if (0<noexiste){
                            handler.sendEmptyMessage(2);
                        }else{
                            getSoap("GetFotoSocio");
                            handler.sendEmptyMessage(3);
                        }
                        break;
                    }

                    case "buscarinvitado":{
                        TipoSocio="invitado";
                        numSocio =      ex.getinvitadobydoc((idStadium), String.valueOf(NumeroAconvertir ), tipodoc,bundle.getString("IP")); //type="s:int" devuelve entero ////////// ENTRA unsigned byte idStadium -- string idTipoDoc -- long documento
                        stringsoap =    numSocio;
                        idSocio=        numSocio;
                        //idTipo="3";
                        getSoap("getestadoinvitado");
                        //handler.sendEmptyMessage(2);
                        break;}

                    case "getcarnet": {
                        stringsoap =    ex.getcaret(idStadium,bundle.getString("NFCTAG"),bundle.getString("IP"));

                        switch (stringsoap) {
                            case "0":
                                idTipo = "0";
                                idSocio = "0";
                                handler.sendEmptyMessage(3);
                                break;
                            case "timeout":
                                idSocio="0";
                                dato.setText("SE INTERRUMPIO LA CONEXION");
                                informacion.setText("Intente nuevamente");
                                break;
                            default:
                                idSocio = stringsoap.substring(stringsoap.indexOf(",") + 2);
                                idTipo = stringsoap.substring(stringsoap.indexOf(":") + 2, stringsoap.indexOf(","));
                                break;
                        }

                        if (stringsoap==null){
                            idSocio="0";
                        } else {
                            idSocio = stringsoap.substring(stringsoap.indexOf('\n')+1).replace(" ","");
                        }

                        switch (idTipo) {
                            case "8":
                                //  handler.sendEmptyMessage(4);
                                getSoap("getestado");

                                break;
                            case "3":

                                getSoap("getestadoinvitado");

                                //stringsoap =    ex.SearchInvitado(idStadium,idSocio,bundle.getString("IP"),bundle.getString("port"));
                                //handler.sendEmptyMessage(3);
                                break;
                            default:

                                idTipo = "50";
                                handler.sendEmptyMessage(2);
                                break;
                        }

                        break;}
                    case "getinvitado":{
                        stringsoap =    ex.SearchInvitado(idStadium,idSocio,bundle.getString("IP"));
                       // idSocio=        stringsoap;
                        if (stringsoap==null){
                            idSocio="0";
                        }else if (stringsoap.equals("timeout")) {
                            dato.setText("SE INTERRUMPIO LA CONEXION");
                            informacion.setText("Intente nuevamente");
                            idSocio="0";
                        }
                        TipoSocio="invitado";
                        getSoap("GetFotoInvitado");
                        handler.sendEmptyMessage(3);
                        break;
                    }
                }
            }
        }).start();
    }
}
