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
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;



class SoapRequests {
    private static final boolean DEBUG_SOAP_REQUEST_RESPONSE = true;
    private static final String NAMESPACE =                     "http://controlplus.net/WebService.asmx";
    private static final String SOAP_ACTION_getversion =        "http://controlplus.net/cwpwebservice/GetVersion";
    private static final String SOAP_ACTION_SearchSocioByDoc =  "http://controlplus.net/cwpwebservice/SearchSocioByDoc";
    private static final String SOAP_ACTION_SearchInvitadoByDoc =  "http://controlplus.net/cwpwebservice/SearchInvitadoByDoc";
    private static final String SOAP_ACTION_SearchSocio =       "http://controlplus.net/cwpwebservice/SearchSocio";
    private static final String SOAP_ACTION_getfoto =           "http://controlplus.net/cwpwebservice/GetFotoSocio";
    private static final String SOAP_ACTION_getfoto_Invitado =  "http://controlplus.net/cwpwebservice/GetFotoInvitado";
    private static final String SOAP_ACTION_searchcarnet =      "http://controlplus.net/cwpwebservice/SearchCarnet";
    private static final String SOAP_ACTION_SearchInvitado =    "http://controlplus.net/cwpwebservice/SearchInvitado";
    private static final String SOAP_ACTION_GetStadiums =    "http://controlplus.net/cwpwebservice/GetStadiums";

    private static String SESSION_ID;

    private void testHttpResponse(HttpTransportSE ht) {
        ht.debug = DEBUG_SOAP_REQUEST_RESPONSE;
        if (DEBUG_SOAP_REQUEST_RESPONSE) {
            Log.v("SOAP RETURN", "Request XML:\n" + ht.requestDump);
            Log.v("SOAP RETURN", "\n\n\nResponse XML:\n" + ht.responseDump);
        }
    }

    String getversion(String IP) {

        String data = null;

        String methodname = "GetVersion";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        //request.addProperty("symbol", Value);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht =  getHttpTransportSE( IP);
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
            Log.e("LogsAndroid", "Mensaje de error- datos version");

        }
        return data;
    }

    String GetStadiums(String IP) {

        String data = null;

        String methodname = "GetStadiums";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        //request.addProperty("symbol", Value);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht =  getHttpTransportSE( IP);
        try {
            ht.call(SOAP_ACTION_GetStadiums, envelope);

            testHttpResponse(ht);
            SoapObject resultsString = (SoapObject)envelope.getResponse();

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
            data = resultsString.toString().replace("Id","").replace("Stadium=","").replace("StadiumData=","").replace("anyType{","").replace("}","").replace("; ; ",";").replace("IdStadium=","").replaceAll("EventoAbonos="+"\\d"+";","").replace("EventoAbonos="+"null"+";","").replaceAll("EventoAbonos="+"\\d"+"\\d"+"\\d"+";","").replaceAll("EventoAbonos="+"\\d"+"\\d"+";","").replace("; ",";");

        } catch (Exception q) {
            q.printStackTrace();
            Log.e("LogsAndroid", "Mensaje de error- getestadios");

        }
        return data;
    }

    String getcaret(String idEstadio, String idSerie, String IP) {

        String data;

        String methodname = "SearchCarnet";
        SoapObject request = new SoapObject("http://controlplus.net/cwpwebservice",methodname);// new SoapObject(NAMESPACE, methodname);

        request.addProperty("idEstadio", idEstadio);
        request.addProperty("idSerie",idSerie);

        SoapSerializationEnvelope envelope;
        envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht =  getHttpTransportSE( IP);
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
        } catch (SocketTimeoutException bug) {
            data="timeout";
        }catch (Exception q) {
            q.printStackTrace();
            data ="0";
        }
        return data;
    }

    String getsociobydoc(String idStadium, String documento, String idTipoDoc, String IP) {

        String data = null  ;

        String methodname = "SearchSocioByDoc";
        SoapObject request = new SoapObject("http://controlplus.net/cwpwebservice",methodname);// new SoapObject(NAMESPACE, methodname);

        request.addProperty("idStadium", idStadium);
        request.addProperty("idTipoDoc",idTipoDoc);
        request.addProperty("documento",documento);

        SoapSerializationEnvelope envelope;
        envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht =  getHttpTransportSE( IP);
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

        }catch (SocketTimeoutException bug) {
            data="timeout";
        } catch (Exception q) {
            q.printStackTrace();
        }
        return data;
    }

    String getinvitadobydoc(String idStadium, String documento, String idTipoDoc, String IP) {

        String data = null  ;

        String methodname = "SearchInvitadoByDoc";
        SoapObject request = new SoapObject("http://controlplus.net/cwpwebservice",methodname);// new SoapObject(NAMESPACE, methodname);

        request.addProperty("idStadium", idStadium);
        request.addProperty("idTipoDoc",idTipoDoc);
        request.addProperty("documento",documento);

        SoapSerializationEnvelope envelope;
        envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht =  getHttpTransportSE( IP);
        try {

            ht.call(SOAP_ACTION_SearchInvitadoByDoc,  envelope);

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

        } catch (SocketTimeoutException bug) {
            data="timeout";
        }catch (Exception q) {
            q.printStackTrace();
            data ="0";
        }
        return data;
    }

    String getsocio(String idStadium, String numSocio, String documento, String IP) {

        String data = null;
       // idStadium="2";
        String methodname = "SearchSocio";
        SoapObject request = new SoapObject("http://controlplus.net/cwpwebservice",methodname);// new SoapObject(NAMESPACE, methodname);

        request.addProperty("idStadium", idStadium);
        request.addProperty("numSocio",numSocio);
        request.addProperty("documento",null);
        request.addProperty("traerFoto","0");
        SoapSerializationEnvelope envelope;


        envelope = getSoapSerializationEnvelope(request);
        //envelope.skipNullProperties=true;
        //envelope.dotNet = false;
        HttpTransportSE ht =  getHttpTransportSE( IP);
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
           String a = response.getPropertySafelyAsString("EstadoAcceso");
            if ( a.equals("")){
                return response.getPropertySafelyAsString("Message");
            }
            SoapObject responsee = (SoapObject) response.getPropertySafely("EstadoAcceso");
            String puertas = responsee.getPropertySafelyAsString("Puertas");
            String IdEstado = responsee.getPropertySafelyAsString("IdEstado");
            String UCP =(response.getPrimitivePropertySafelyAsString("UltimoPago"));
            String Estado = response.getPrimitivePropertySafelyAsString("Estado");
            String TicketVirtual= responsee.getPropertySafelyAsString("TicketVirtual");
            String ContadorSocio = response.getPrimitivePropertyAsString("ContadorSocio");
            String EstadoAcceso = responsee.getPropertySafelyAsString("Estado");

            if (EstadoAcceso==null ||EstadoAcceso.equals("")){
                EstadoAcceso="";
            }else{
                EstadoAcceso= "EstadoAcceso "+EstadoAcceso;
            }


            if (ContadorSocio==null ||ContadorSocio.equals("")){
                ContadorSocio="0";
            }



            if (TicketVirtual==null ||TicketVirtual.equals("") || TicketVirtual.equals("null")){
                TicketVirtual="TicketVirtual: No tiene";
            }else{
                TicketVirtual= "TicketVirtual: "+TicketVirtual;
            }

            if (Estado==null ||Estado.equals("") ){
                Estado="0";
            }
            if (puertas==null ||puertas.equals("") ){
                puertas="Sin Accesos asignados";
            }
            if (UCP==null ||UCP.equals("") ){
                UCP="No Registra";
            }else {
                UCP=GetStringCuotaPaga(response.getPrimitivePropertySafelyAsString("UltimoPago").substring(0,  7));
            }

            data =  response.getPrimitivePropertySafelyAsString("Nombre")+ '\n'+
                    "Socio N°: "+response.getPrimitivePropertySafelyAsString("NumSocio")+"-"+ContadorSocio+'\n'+
                    "Documento: "+          response.getPrimitivePropertySafelyAsString("Documento")+'\n'+
                    //"Num. Socio: "+         response.getPrimitivePropertySafelyAsString("NumSocio")+'\n'+
                    "Categoria: "+          response.getPrimitivePropertySafelyAsString("Categoria") +'\n'+
                   // "Precio Categoria: "+   response.getPrimitivePropertySafelyAsString("PrecioCategoria")+'\n'+

                   // "Tipo Socio: "+         response.getPrimitivePropertySafelyAsString("TipoSocio")+'\n'+


                    "Estado del Socio: "+             Estado+'\n'+
                    "Ultima Cuota Paga: " + UCP +"#"+'\n'+" "+
                    "IdEstado: "+ IdEstado+'\n'+
                    TicketVirtual+'\n'+
                    EstadoAcceso+'\n'+
                    "Puertas:" +puertas;


        }  catch (SocketTimeoutException bug) {
            data="timeout";
        }catch (Exception q) {
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

    String SearchInvitado(String idStadium, String numInvitado, String IP) {

        String data;

        String methodname = "SearchInvitado";
        SoapObject request = new SoapObject("http://controlplus.net/cwpwebservice",methodname);// new SoapObject(NAMESPACE, methodname);

        request.addProperty("idStadium", idStadium);
        request.addProperty("numInvitado",numInvitado);
        request.addProperty("documento",null);
        request.addProperty("traerFoto","0");
        SoapSerializationEnvelope envelope;
        envelope = getSoapSerializationEnvelope(request);
        envelope.skipNullProperties=true;

        HttpTransportSE ht =  getHttpTransportSE( IP);
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

            String a = resultsString.getPropertySafelyAsString("EstadoAcceso");
            if ( a.equals("")){
                return resultsString.getPropertySafelyAsString("Message");
            }
            SoapObject responsee = (SoapObject) resultsString.getProperty("EstadoAcceso");
            String Estado = responsee.getPrimitivePropertyAsString("Estado");
            String CuotaCtrl = responsee.getPrimitivePropertyAsString("CuotaCtrl");
            String nombre = resultsString.getPrimitivePropertyAsString("Nombre");
            String Documento = resultsString.getPrimitivePropertyAsString("Documento");
            String IdEstado = responsee.getPrimitivePropertyAsString("IdEstado");
            String Message = resultsString.getPrimitivePropertyAsString("Message");
            String IdInvitado = resultsString.getPrimitivePropertyAsString("IdInvitado");
            String Categoria = resultsString.getPrimitivePropertyAsString("Categoria");
            String TicketVirtual= responsee.getPropertySafelyAsString("TicketVirtual");
            String Puertas= responsee.getPropertySafelyAsString("Puertas");
            String Contador = resultsString.getPrimitivePropertyAsString("Contador");

            if (Contador==null ||Contador.equals("")){
                Contador="0";
            }



            if (TicketVirtual==null ||TicketVirtual.equals("") || TicketVirtual.equals("null")){
                TicketVirtual="TicketVirtual: No tiene";
            }else{
                TicketVirtual= "TicketVirtual: "+TicketVirtual;
            }



            if (nombre==null ||nombre.equals("") ){
                nombre="0";
            }
            if (Documento==null ||Documento.equals("") ){
                Documento="0";

            }else{
                Documento= "Documento: "+Documento;
            }
            if (Categoria==null ||Categoria.equals("") ){
                Categoria="0";

            }else{
                Categoria= "Categoria: "+Categoria;
            }
            if (CuotaCtrl==null ||CuotaCtrl.equals("") ){
                CuotaCtrl="0";

            }else if (CuotaCtrl.length()>7){
                CuotaCtrl=GetStringCuotaPaga(CuotaCtrl);
            }
            if (IdEstado==null ||IdEstado.equals("") ){
                IdEstado="0";
            }
            if (Estado==null ||Estado.equals("") ){
                Estado="0";
            }
            if (Puertas==null ){
                Puertas="";
            }
            if (Message==null ||Message.equals("") ){
                Message="0";
            }
            if (IdInvitado==null ||IdInvitado.equals("") ){
                IdInvitado="0";

            }else{
                IdInvitado = "Invitado Nº: "+ IdInvitado;
            }

            //  data = (resultsString.toString());

            data=   nombre+'\n'+
                    "IdEstado: "+ IdEstado+'\n'+
                    "EstadoAcceso "+ Estado + '\n'+
                    "Cuota Control: " +CuotaCtrl +'\n'+
                    IdInvitado+"-"+Contador+'\n'+
                    Documento+'\n'+
                    Categoria + '\n'+
                    resultsString.getPrimitivePropertyAsString("Message")+ '\n'+
                    TicketVirtual+'\n'+
                    "Puertas:" +Puertas;
        }catch (SocketTimeoutException bug) {
            data="timeout";
        } catch (Exception q) {
            q.printStackTrace();
            data = "0";
        }
        return data;
    }

    byte[] getfotosocio(String idStadium, String numSocio, String IP) {

        byte data[] = new byte[1];
        String methodname = "GetFotoSocio";

        SoapObject request = new SoapObject("http://controlplus.net/cwpwebservice", methodname);
        request.addProperty("idStadium", idStadium);
        request.addProperty("numSocio", numSocio);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht =  getHttpTransportSE( IP);
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
            Log.e("LogsAndroid", "Mensaje de error- foto");
            data[0]=0;
        }
        return data;
    }

    byte[] getfotoinvitado(String idStadium, String numInvitado, String IP) {

        byte data[] = new byte[1];
        String methodname = "GetFotoInvitado";

        SoapObject request = new SoapObject("http://controlplus.net/cwpwebservice", methodname);
        request.addProperty("idStadium", idStadium);
        request.addProperty("numInvitado", numInvitado);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht =  getHttpTransportSE( IP);
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

        } catch (SocketTimeoutException bug) {
            data[0]=0;
            Log.e("LogsAndroid", "Mensaje de error- timeout");

        }catch (Exception q) {
            q.printStackTrace();
            data[0]=0;
            Log.e("LogsAndroid", "Mensaje de error- error de datos");

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
        HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY,method,8000);
        ht.debug = true;

        ht.setXmlVersionTag("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>");
        return ht;
    }

}

