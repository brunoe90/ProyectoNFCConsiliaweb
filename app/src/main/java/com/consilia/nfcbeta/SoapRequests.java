package com.consilia.nfcbeta;

import android.util.Base64;
import android.util.Log;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class SoapRequests {
    private static final boolean DEBUG_SOAP_REQUEST_RESPONSE = true;
    private static final String NAMESPACE =                     "http://192.168.0.1/WebService.asmx";
    private static final String SOAP_ACTION_getversion =        "http://controlplus.net/cwpwebservice/GetVersion";
    private static final String SOAP_ACTION_SearchSocioByDoc =  "http://controlplus.net/cwpwebservice/SearchSocioByDoc";
    private static final String SOAP_ACTION_SearchSocio =       "http://controlplus.net/cwpwebservice/SearchSocio";
    private static final String SOAP_ACTION_getfoto =           "http://controlplus.net/cwpwebservice/GetFotoSocio";
    private static final String SOAP_ACTION_getfoto_Invitado =           "http://controlplus.net/cwpwebservice/GetFotoInvitado";
    private static final String SOAP_ACTION_searchcarnet =           "http://controlplus.net/cwpwebservice/SearchCarnet";
    private static final String SOAP_ACTION_SearchInvitado =           "http://controlplus.net/cwpwebservice/SearchInvitado";

    private static String SESSION_ID;

    private void testHttpResponse(HttpTransportSE ht) {
        ht.debug = DEBUG_SOAP_REQUEST_RESPONSE;
        if (DEBUG_SOAP_REQUEST_RESPONSE) {
            Log.v("SOAP RETURN", "Request XML:\n" + ht.requestDump);
            Log.v("SOAP RETURN", "\n\n\nResponse XML:\n" + ht.responseDump);
        }
    }

    String getversion() {

        String data = null;

        String methodname = "GetVersion";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        //request.addProperty("symbol", Value);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht =  getHttpTransportSE( "http://192.168.0.1/WebService.asmx");
        try {
            ht.call(SOAP_ACTION_getversion, envelope);

            testHttpResponse(ht);
            SoapPrimitive resultsString = (SoapPrimitive)envelope.getResponse();

            List<HeaderProperty> COOKIE_HEADER = (List<HeaderProperty>) ht.getServiceConnection().getResponseProperties();

            for (int i = 0; i < COOKIE_HEADER.size(); i++) {
                String key = COOKIE_HEADER.get(i).getKey();
                String value = COOKIE_HEADER.get(i).getValue();

                if (key != null && key.equalsIgnoreCase("set-cookie")) {
                    SoapRequests.SESSION_ID = value.trim();
                    Log.v("SOAP RETURN", "Cookie :" + SoapRequests.SESSION_ID);
                    break;
                }
            }
            data = resultsString.toString();

        } catch (Exception q) {
            q.printStackTrace();
        }
        return data;
    }

    String getcaret(String idEstadio, String idSerie) {

        String data = null  ;

        String methodname = "SearchCarnet";
        SoapObject request = new SoapObject("http://controlplus.net/cwpwebservice",methodname);// new SoapObject(NAMESPACE, methodname);

        request.addProperty("idEstadio", idEstadio);
        request.addProperty("idSerie",idSerie);

        SoapSerializationEnvelope envelope;
        envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht =  getHttpTransportSE( "http://192.168.0.1/WebService.asmx");
        try {

            ht.call(SOAP_ACTION_searchcarnet,  envelope);

            testHttpResponse(ht);
            SoapObject resultsString = (SoapObject) envelope.getResponse();

            List<HeaderProperty> COOKIE_HEADER = (List<HeaderProperty>) ht.getServiceConnection().getResponseProperties();

            for (int i = 0; i < COOKIE_HEADER.size(); i++) {
                String key = COOKIE_HEADER.get(i).getKey();
                String value = COOKIE_HEADER.get(i).getValue();

                if (key != null && key.equalsIgnoreCase("set-cookie")) {
                    SoapRequests.SESSION_ID = value.trim();
                    Log.v("SOAP RETURN", "Cookie :" + SoapRequests.SESSION_ID);
                    break;
                }
            }
          //  data = (resultsString.toString());

            data=   "Tipo: "+       resultsString.getPrimitivePropertySafelyAsString("IdTipo") +","+'\n'+
                   /* "numero: "+*/     resultsString.getPrimitivePropertySafelyAsString("IdNumero");
        } catch (Exception q) {
            q.printStackTrace();
            data ="0";
        }
        return data;
    }

     String getsociobydoc(String idStadium, String documento, String idTipoDoc) {

        String data = null  ;

        String methodname = "SearchSocioByDoc";
        SoapObject request = new SoapObject("http://controlplus.net/cwpwebservice",methodname);// new SoapObject(NAMESPACE, methodname);

        request.addProperty("idStadium", idStadium);
        request.addProperty("idTipoDoc",idTipoDoc);
        request.addProperty("documento",documento);

        SoapSerializationEnvelope envelope;
        envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht =  getHttpTransportSE( "http://192.168.0.1/WebService.asmx");
        try {

            ht.call(SOAP_ACTION_SearchSocioByDoc,  envelope);

            testHttpResponse(ht);
            SoapPrimitive resultsString = (SoapPrimitive) envelope.getResponse();

            List<HeaderProperty> COOKIE_HEADER = (List<HeaderProperty>) ht.getServiceConnection().getResponseProperties();

            for (int i = 0; i < COOKIE_HEADER.size(); i++) {
                String key = COOKIE_HEADER.get(i).getKey();
                String value = COOKIE_HEADER.get(i).getValue();

                if (key != null && key.equalsIgnoreCase("set-cookie")) {
                    SoapRequests.SESSION_ID = value.trim();
                    Log.v("SOAP RETURN", "Cookie :" + SoapRequests.SESSION_ID);
                    break;
                }
            }
            data = (resultsString.toString());

        } catch (Exception q) {
            q.printStackTrace();
        }
        return data;
    }

    String getsocio(String idStadium, String numSocio, String documento) {

        String data;
       // idStadium="2";
        String methodname = "SearchSocio";
        SoapObject request = new SoapObject("http://controlplus.net/cwpwebservice",methodname);// new SoapObject(NAMESPACE, methodname);

        request.addProperty("idStadium", idStadium);
        request.addProperty("numSocio",numSocio);
        request.addProperty("documento",null);
        request.addProperty("traerFoto","0");
        SoapSerializationEnvelope envelope;


        envelope = getSoapSerializationEnvelope(request);
        envelope.skipNullProperties=true;
        //envelope.dotNet = false;
        HttpTransportSE ht =  getHttpTransportSE( "http://192.168.0.1/WebService.asmx");
        try {

            ht.call(SOAP_ACTION_SearchSocio,  envelope);

            testHttpResponse(ht);
            SoapObject response=(SoapObject)envelope.getResponse();

            List<HeaderProperty> COOKIE_HEADER = (List<HeaderProperty>) ht.getServiceConnection().getResponseProperties();

            for (int i = 0; i < COOKIE_HEADER.size(); i++) {
                String key = COOKIE_HEADER.get(i).getKey();
                String value = COOKIE_HEADER.get(i).getValue();

                if (key != null && key.equalsIgnoreCase("set-cookie")) {
                    SoapRequests.SESSION_ID = value.trim();
                    Log.v("SOAP RETURN", "Cookie :" + SoapRequests.SESSION_ID);
                    break;
                }

            }
            response.getPropertySafelyAsString("EstadoAcceso");
            if (null== response.getPropertySafelyAsString("EstadoAcceso")){
                return "0";
            }
//            String acceso =response.getPropertySafelyAsString("EstadoAcceso").replace("anyType{","").replace("; }", "").replace("null","No tiene");
//
//            String parseo[] = acceso.split(";");

//            String condiciondeacceso= "";
     //       String algo = response.getProperty("EstadoAcceso").toString();
            SoapObject responsee = (SoapObject) response.getProperty("EstadoAcceso");
            String puertas = responsee.getPropertySafelyAsString("Puertas");
            String IdEstado = responsee.getPropertySafelyAsString("IdEstado");
            String UCP =(response.getPrimitivePropertySafelyAsString("UltimoPago"));
            if (puertas==null ||puertas=="" ){
                puertas="Sin Accesos asignados";
            }
            if (UCP==null ||UCP=="" ){
                UCP="No Asigna";
            }else {
                UCP=GetStringCuotaPaga(response.getPrimitivePropertySafelyAsString("UltimoPago").substring(0,  7));
            }

            data =  response.getPrimitivePropertySafelyAsString("Nombre")+ '\n'+
                    "Socio Número: "+response.getPrimitivePropertySafelyAsString("NumSocio")+'\n'+
                    "Documento: "+          response.getPrimitivePropertySafelyAsString("Documento")+'\n'+
                    //"Num. Socio: "+         response.getPrimitivePropertySafelyAsString("NumSocio")+'\n'+
                    "Categoria: "+          response.getPrimitivePropertySafelyAsString("Categoria") +'\n'+
                   // "Precio Categoria: "+   response.getPrimitivePropertySafelyAsString("PrecioCategoria")+'\n'+

                   // "Tipo Socio: "+         response.getPrimitivePropertySafelyAsString("TipoSocio")+'\n'+


                    "Estado del Socio: "+             response.getPrimitivePropertySafelyAsString("Estado")+'\n'+
                    "Ultima Cuota Paga: " + UCP +"#"+'\n'+" "+
                    "IdEstado: "+ IdEstado+'\n'+
                    "Puertas:" +puertas;


        } catch (Exception q) {
            q.printStackTrace();
            data = "0";
        }
        return data;
    }

    private String GetStringCuotaPaga(String fechaCuotaPaga){

        SimpleDateFormat formatoOriginal    = new SimpleDateFormat("yyyy-MM", new Locale("us","US"));
        SimpleDateFormat formatoDestino     = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES") );
        String nuevaFecha = null;           // Almacena el string de salida
        Date dat;                           // Almacena la fecha en formato puro

        try{

            // Recuperamos la fecha
            dat = formatoOriginal.parse( fechaCuotaPaga );

            // Parseamos la fecha y devolvemos el string
            nuevaFecha = formatoDestino.format( dat );
        }
         catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        assert nuevaFecha != null;
        nuevaFecha = nuevaFecha.substring(0, 1).toUpperCase() + nuevaFecha.substring(1);
        return nuevaFecha;
    }

     String SearchInvitado(String idStadium, String numInvitado) {

        String data = null  ;

        String methodname = "SearchInvitado";
        SoapObject request = new SoapObject("http://controlplus.net/cwpwebservice",methodname);// new SoapObject(NAMESPACE, methodname);

        request.addProperty("idStadium", idStadium);
        request.addProperty("numInvitado",numInvitado);
        request.addProperty("documento",null);
        request.addProperty("traerFoto","0");
        SoapSerializationEnvelope envelope;
        envelope = getSoapSerializationEnvelope(request);
        envelope.skipNullProperties=true;

        HttpTransportSE ht =  getHttpTransportSE( "http://192.168.0.1/WebService.asmx");
        try {

            ht.call(SOAP_ACTION_SearchInvitado,  envelope);

            testHttpResponse(ht);
            SoapObject resultsString = (SoapObject) envelope.getResponse();

            List<HeaderProperty> COOKIE_HEADER = (List<HeaderProperty>) ht.getServiceConnection().getResponseProperties();

            for (int i = 0; i < COOKIE_HEADER.size(); i++) {
                String key = COOKIE_HEADER.get(i).getKey();
                String value = COOKIE_HEADER.get(i).getValue();

                if (key != null && key.equalsIgnoreCase("set-cookie")) {
                    SoapRequests.SESSION_ID = value.trim();
                    Log.v("SOAP RETURN", "Cookie :" + SoapRequests.SESSION_ID);
                    break;
                }
            }
            //  data = (resultsString.toString());

            data=  // "Tipo: "+       resultsString.getPrimitivePropertyAsString("IdTipo") +'\n'+
                   /* "numero: "+*/     resultsString.getPrimitivePropertyAsString("Message");
        } catch (Exception q) {
            q.printStackTrace();
        }
        return data;
    }

     byte[] getfotosocio(String idStadium, String numSocio) {

        byte data[] = new byte[0];
        String methodname = "GetFotoSocio";

        SoapObject request = new SoapObject("http://controlplus.net/cwpwebservice", methodname);
        request.addProperty("idStadium", idStadium);
        request.addProperty("numSocio", numSocio);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht =  getHttpTransportSE( "http://192.168.0.1/WebService.asmx");
        try {
            ht.call(SOAP_ACTION_getfoto, envelope);

            testHttpResponse(ht);
            SoapPrimitive resultsString = (SoapPrimitive) envelope.getResponse();

            List<HeaderProperty> COOKIE_HEADER = (List<HeaderProperty>) ht.getServiceConnection().getResponseProperties();

            for (int i = 0; i < COOKIE_HEADER.size(); i++) {
                String key = COOKIE_HEADER.get(i).getKey();
                String value = COOKIE_HEADER.get(i).getValue();

                if (key != null && key.equalsIgnoreCase("set-cookie")) {
                    SoapRequests.SESSION_ID = value.trim();
                    Log.v("SOAP RETURN", "Cookie :" + SoapRequests.SESSION_ID);
                    break;
                }
            }
            String decode =resultsString.toString();
            data = Base64.decode(decode.getBytes(), Base64.DEFAULT); //resultsString.toString();

        } catch (Exception q) {
            q.printStackTrace();
        }
        return data;
    }

     byte[] getfotoinvitado(String idStadium, String numSocio) {

        byte data[] = new byte[0];
        String methodname = "GetFotoInvitado";

        SoapObject request = new SoapObject("http://controlplus.net/cwpwebservice", methodname);
        request.addProperty("idStadium", idStadium);
        request.addProperty("numSocio", numSocio);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht =  getHttpTransportSE( "http://192.168.0.1/WebService.asmx");
        try {
            ht.call(SOAP_ACTION_getfoto_Invitado, envelope);

            testHttpResponse(ht);
            SoapPrimitive resultsString = (SoapPrimitive) envelope.getResponse();

            List<HeaderProperty> COOKIE_HEADER = (List<HeaderProperty>) ht.getServiceConnection().getResponseProperties();

            for (int i = 0; i < COOKIE_HEADER.size(); i++) {
                String key = COOKIE_HEADER.get(i).getKey();
                String value = COOKIE_HEADER.get(i).getValue();

                if (key != null && key.equalsIgnoreCase("set-cookie")) {
                    SoapRequests.SESSION_ID = value.trim();
                    Log.v("SOAP RETURN", "Cookie :" + SoapRequests.SESSION_ID);
                    break;
                }
            }
            String decode =resultsString.toString();
            data = Base64.decode(decode.getBytes(), Base64.DEFAULT); //resultsString.toString();

        } catch (Exception q) {
            q.printStackTrace();
        }
        return data;
    }

    private  SoapSerializationEnvelope getSoapSerializationEnvelope(SoapObject request) {
        SoapSerializationEnvelope envelope;
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC2003; // XSD no anda // ENC no anda //env tampoco // env2003 menos // ENC 2003 nope
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        //envelope.writeHeader();
        envelope.setAddAdornments(false);

        envelope.encodingStyle = SoapSerializationEnvelope.ENC2003;
        return envelope;
    }

    private HttpTransportSE getHttpTransportSE(String method   ) {
        HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY,method,4000);
        ht.debug = true;
        ht.setXmlVersionTag("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>");
        return ht;
    }

}

