package com.consilia.nfcbeta;

import android.util.Log;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.Proxy;
import java.util.List;


public class SoapRequests {
    private static final boolean DEBUG_SOAP_REQUEST_RESPONSE = true;
    private static final String NAMESPACE = "http://192.168.0.1/WebService.asmx";
    private static final String SOAP_ACTION_getversion = "http://controlplus.net/cwpwebservice/GetVersion";
    private static final String SOAP_ACTION_SearchSocioByDoc = "http://controlplus.net/cwpwebservice/SearchSocioByDoc";
    private static final String SOAP_ACTION_getfoto = "http://controlplus.net/cwpwebservice/GetFotoSocio";
    private static String SESSION_ID;

    private void testHttpResponse(HttpTransportSE ht) {
        ht.debug = DEBUG_SOAP_REQUEST_RESPONSE;
        if (DEBUG_SOAP_REQUEST_RESPONSE) {
            Log.v("SOAP RETURN", "Request XML:\n" + ht.requestDump);
            Log.v("SOAP RETURN", "\n\n\nResponse XML:\n" + ht.responseDump);
        }
    }

    public String getversion() {
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


    public int getsociobydoc(int idStatium, int documento, String idTipoDoc) {
        int data = 0   ;
        String methodname = "SearchSocioByDoc";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("idStatium", idStatium);
        request.addProperty("documento", documento);
        request.addProperty("idTipoDoc", idTipoDoc);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht =  getHttpTransportSE( "http://192.168.0.1/WebService.asmx");
        try {
            ht.call(SOAP_ACTION_SearchSocioByDoc, envelope);

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
            data = Integer.getInteger(resultsString.toString());

        } catch (Exception q) {
            q.printStackTrace();
        }
        return data;
    }



    public String getfotosocio(int idStatium, int numSocio) {


        /*
        <s:element name="GetFotoSocio">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="idStadium" type="s:unsignedByte" />
            <s:element minOccurs="1" maxOccurs="1" name="numSocio" type="s:int" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetFotoSocioResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="GetFotoSocioResult" type="s:base64Binary" />
          </s:sequence>
        </s:complexType>
      </s:element>
         */

        String data = null;
        String methodname = "GetFotoSocio";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("idStatium", idStatium);
        request.addProperty("numSocio", numSocio);


        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht =  getHttpTransportSE(methodname);
        try {
            ht.call(SOAP_ACTION_getfoto, envelope);

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

    private  SoapSerializationEnvelope getSoapSerializationEnvelope(SoapObject request) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);
        return envelope;
    }




    private HttpTransportSE getHttpTransportSE(String method   ) {
        HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY,method,60000);
        ht.debug = true;
        ht.setXmlVersionTag("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>");
        return ht;
    }

    /*
    private  List<HeaderProperty> getHeader() {
        List<HeaderProperty> header = new ArrayList<HeaderProperty>();
        HeaderProperty headerPropertyObj = new HeaderProperty("cookie", SoapRequests.SESSION_ID);
        header.add(headerPropertyObj);
        return header;
    }*/



}




/*
class GoodsObject implements KvmSerializable {

    @Override
    public Object getProperty(int i) {
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 0;
    }

    @Override
    public void setProperty(int i, Object o) {

    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

    }
}*/
