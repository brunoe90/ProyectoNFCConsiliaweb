package com.consilia.nfcbeta;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


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


    public void setUltimaActivity(String val){
        this.UltimaActivity = val;

    }

    public String getUltimaActivity(){
        return this.UltimaActivity;

    }

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
        Puerta =        String.valueOf(bundle.getInt("Puerta"));
        Tarjeta =       bundle.getString("NFCTAG");
        datomanual  =   bundle.getString("manual");
        NumeroAconvertir=bundle.getInt("NumeroAconvertir");
        //UltimaActivity = bundle.getString("lastActivity");

        // Seteo variable ultima medante metodo
        this.setUltimaActivity(bundle.getString("lastActivity"));

        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if (!connected) Toast.makeText(getBaseContext(), "Falla la coneccion a internet!!!!", Toast.LENGTH_LONG).show();

        if (null != idStadium||idStadium.equals("")){

            if (null!=Puerta||Puerta.equals("")){

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

                     {
//                        if (!((Tarjeta.equals("") )||Tarjeta==null)){
//                            bundle.putInt("TAB", 0);
//                        } else bundle.putInt("TAB", 1);
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
           // Toast.makeText(getBaseContext(), "Volviendo al menu", Toast.LENGTH_LONG).show();
            bundle.putInt("idStadium",Integer.valueOf(idStadium));
            bundle.putInt("Puerta", Integer.valueOf(Puerta));
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
                    try {
                        imageView.setImageBitmap(/*getRoundedShape*/(decodedByte));
                    } catch (Exception q) {
                        q.printStackTrace();
                    }
                    break;}

                case 2:{
                    RelativeLayout layout = (RelativeLayout) findViewById(R.id.pasanopasa);

                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        assert layout != null;
                        layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                    } else {
                        assert layout != null;
                        layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                    }


                    dato.setText("Tarjeta erronea");
                    toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);

                }break;

                case 3: {
                    RelativeLayout layout = (RelativeLayout) findViewById(R.id.pasanopasa);

                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    String puertas;

                    if (!stringsoap.equals("0"))
                    {
                        int numpuertas = stringsoap.indexOf("Puertas:");
                        if (numpuertas>0){
                            puertas=stringsoap.substring(numpuertas+8);

                            if (puertas.length()>10){
                                Accesos.setTextSize(15);
                            }
                            Accesos.setText("Accesos:"+puertas.replace("Puerta","").replace("PUERTA",""));
                        }

                        //compartido
                        String Nombre = stringsoap.substring(0,stringsoap.indexOf('\n'));
                        if (dato.length()>15){
                            dato.setTextSize(24);
                        }
                        dato.setText(Nombre);




                        //invitado
//                        int mensaje = stringsoap.indexOf("Mensaje: ");
//                        if (mensaje>0){
//                            String info = stringsoap.substring(mensaje+"Mensaje: ".length()+1);
//                            info = info.substring(0,info.indexOf('\n'));
//                            if (informacion.length()>20){
//                                informacion.setTextSize(18);
//                            }
//                            informacion.setText("Mensaje: "+info);
//                        }

                        // invitado
                        int IdInvitado = stringsoap.indexOf("Num. de Invitado: ");
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
                                informacion.setTextSize(18);
                            }
                            informacion.setText(info);
                        }



                        //INVITADO
                        int CC = stringsoap.indexOf("Cuota Control: ");
                        if (CC>0){
                            String UCP = stringsoap.substring(stringsoap.indexOf("Cuota Control: ")+15);
                            UCP = UCP.substring(0,UCP.indexOf('\n'));

                            tresultado.setText("Cuota Control: "+'\n'+'\n'+UCP);
                        }


                        //SOCIO
                        int ucpfinder = stringsoap.indexOf("Ultima Cuota Paga: ");
                        if (ucpfinder>0){
                            String UCP = stringsoap.substring(stringsoap.indexOf("Ultima Cuota Paga: ")+19);
                            UCP = UCP.substring(0,UCP.indexOf("#"));

                            tresultado.setText("Última Cuota Paga: "+'\n'+UCP);
                        }



                        int callback =stringsoap.indexOf("IdEstado");
                        if (stringsoap.indexOf("IdEstado")>0){
                            idTipo=stringsoap.substring(callback+8,callback+8+4).replace("\n","").replace(" ","").replace(":","");
                        }


                        switch (Integer.valueOf(idTipo)) {

                            case 0: {
                                //no puede pasar

                                datop.setText("Fue Deshabilitado");
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
                                        String info = stringsoap.substring(puerta+"Puertas:".length());


                                        if (info.indexOf(Puerta)>0){
                                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                                assert layout != null;
                                                layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundok) );
                                            } else {
                                                assert layout != null;
                                                layout.setBackground( getResources().getDrawable(R.drawable.backgroundok));
                                            }

                                            datop.setText("OK");
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
                                        datop.setText("No hay Puertas Asignadas");
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
                                        String info = stringsoap.substring(puerta+"Puertas:".length()+1);


                                        if (info.indexOf(Puerta)>0){
                                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                                assert layout != null;
                                                layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundok) );
                                            } else {
                                                assert layout != null;
                                                layout.setBackground( getResources().getDrawable(R.drawable.backgroundok));
                                            }

                                            datop.setText("Habilit. manual");
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
                                        datop.setText("No hay Puertas Asignadas");
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


                                datop.setText("Ya ingreso al estadio");
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


                                datop.setText("No esta activo");
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


                                datop.setText("Cuota Vencida");
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);


                                break;
                            }
                            case 6: {
                                // no pasa

                                datop.setText("Paso por Perimetral");
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {
                                    assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }


                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);

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


                                datop.setText("Abono vencido");
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);


                                break;
                            } case 8: {
                                // no pasa
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                                datop.setText("Canjeo por ticket");
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


                                datop.setText("Credencial Vencida");
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


                                datop.setText("Copia no Habilitada");

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


                                datop.setText("No tiene carnet");
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


                                datop.setText("Sector incorrecto");

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


                                datop.setText("Sector Completo");
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


                                datop.setText("Tarjeta Defectuosa");
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);

                                break;
                            }case 17: {
                                // no pasa
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }



                                datop.setText("Difiere N. de Serie");
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);

                                break;
                            }case 18: {
                                // no pasa
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }



                                datop.setText("Pasadas Excedidas");
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);

                                break;
                            }case 19: {
                                // no pasa

                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }


                                datop.setText("Actividad Vencida");

                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                                break;
                            }case 20: {
                                // no pasa

                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {assert layout != null;
                                    layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.backgroundnope) );
                                } else {assert layout != null;
                                    layout.setBackground( getResources().getDrawable(R.drawable.backgroundnope));
                                }


                                datop.setText("Fuera de Horario");

                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2);
                                break;
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
        canvas.drawBitmap(scaleBitmapImage,
            new Rect(0, 0, scaleBitmapImage.getWidth(),
            scaleBitmapImage.getHeight()),
            new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    private void getSoap( final String method) {
        new Thread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                SoapRequests ex = new SoapRequests();
                context = getApplicationContext();
                switch (method) {
                    case "getversion":{
                        stringsoap =    ex.getversion(bundle.getString("IP"),bundle.getString("port"));
                        handler.sendEmptyMessage(0);
                        break;}

                    case "GetFotoSocio":{
                        byte[] foto =   ex.getfotosocio((idStadium),String.valueOf(idSocio ),bundle.getString("IP"),bundle.getString("port"));
                        decodedByte =   BitmapFactory.decodeByteArray(foto, 0, foto.length);
                        handler.sendEmptyMessage(1);
                        TipoSocio="socio";
                        break;}
                    case "GetFotoInvitado":{
                        TipoSocio="invitado";
                        byte[] foto =   ex.getfotoinvitado((idStadium),String.valueOf(idSocio ),bundle.getString("IP"),bundle.getString("port"));
                        decodedByte =   BitmapFactory.decodeByteArray(foto, 0, foto.length);
                        handler.sendEmptyMessage(1);
                        break;}


                    case "buscar":{
                        TipoSocio="socio";
                        numSocio =      ex.getsociobydoc((idStadium), String.valueOf(NumeroAconvertir ), tipodoc,bundle.getString("IP"),bundle.getString("port")); //type="s:int" devuelve entero ////////// ENTRA unsigned byte idStadium -- string idTipoDoc -- long documento
                        stringsoap =    numSocio;
                        idSocio=        numSocio;
                        getSoap("getestado");
                        //idTipo="8";
                        //handler.sendEmptyMessage(2);
                        break;}

                    case "getestado":{
                        TipoSocio="socio";
                        numSocio =      ex.getsocio((idStadium),String.valueOf(idSocio ),String.valueOf(idSocio ) ,bundle.getString("IP"),bundle.getString("port")); //type="s:int" devuelve entero ////////// ENTRA unsigned byte idStadium -- string idTipoDoc -- long documento
                        stringsoap =    numSocio;

                        getSoap("GetFotoSocio");
                        handler.sendEmptyMessage(3);
                        break;}

                    case "getestadoinvitado":{
                        numSocio =      ex.SearchInvitado(String.valueOf(idStadium),String.valueOf(idSocio ) ,bundle.getString("IP"),bundle.getString("port")); //type="s:int" devuelve entero ////////// ENTRA unsigned byte idStadium -- string idTipoDoc -- long documento
                        stringsoap =    numSocio;
                        TipoSocio="invitado";
                        getSoap("GetFotoInvitado");
                        handler.sendEmptyMessage(3);
                        break;
                    }

                    case "buscarinvitado":{
                        TipoSocio="invitado";
                        numSocio =      ex.getinvitadobydoc((idStadium), String.valueOf(NumeroAconvertir ), tipodoc,bundle.getString("IP"),bundle.getString("port")); //type="s:int" devuelve entero ////////// ENTRA unsigned byte idStadium -- string idTipoDoc -- long documento
                        stringsoap =    numSocio;
                        idSocio=        numSocio;
                        //idTipo="3";
                        getSoap("getinvitado");
                        //handler.sendEmptyMessage(2);
                        break;}

                    /*

                    else if (!idInvitado.equals("0")){
                            getSoap("getestadoinvitado");
                        } else if (!DocInvitado.equals("0")){
                            getSoap("buscarinvitado");
                        }
                     */
                    case "getcarnet": {
                        stringsoap =    ex.getcaret(idStadium,bundle.getString("NFCTAG"),bundle.getString("IP"),bundle.getString("port"));

                        if (stringsoap.equals("0")){
                            idTipo="0";
                            idSocio="0";
                            handler.sendEmptyMessage(3);
                        }else{
                            idSocio=        stringsoap.substring(stringsoap.indexOf(",")+2);
                            idTipo=    stringsoap.substring(stringsoap.indexOf(":")+2,stringsoap.indexOf(","));
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
                                // getSoap("GetFotoSocio");
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
                        stringsoap =    ex.SearchInvitado(idStadium,idSocio,bundle.getString("IP"),bundle.getString("port"));
                        idSocio=        stringsoap;
                        if (stringsoap==null){
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
