package com.consilia.nfcbeta;

import android.util.Log;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.AttributeInfo;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.annotation.ElementType;
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


    public int getsociobydoc(int idStatium, long documento, String idTipoDoc) {

        int data = 0   ;
        byte idStadiumq = 2;
        String methodname = "SearchSocioByDoc";

        SoapObject request = new SoapObject("http://controlplus.net/cwpwebservice",methodname);// new SoapObject(NAMESPACE, methodname);

        PropertyInfo pi=new PropertyInfo();

     //   pi = new PropertyInfo();
        request.addProperty("idStatium","2");
        request.addProperty("idTipoDoc","DNI");
        request.addProperty("documento","23974462");

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        envelope.bodyOut = request;
        envelope.encodingStyle = SoapSerializationEnvelope.ENC2003;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
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
        SoapSerializationEnvelope envelope;
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
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
        HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY,method,60000);
        ht.debug = true;
        ht.setXmlVersionTag("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>");
        return ht;
    }

}

class MarshalLong implements Marshal {
        public Object readInstance(XmlPullParser parser, String namespace,
                                   String name, PropertyInfo expected) throws IOException,
                XmlPullParserException {

            return Long.parseLong(parser.nextText());
        }

        public void register(SoapSerializationEnvelope cm) {

            cm.addMapping(cm.xsd,null, Long.class, this);

        }

        public void writeInstance(XmlSerializer writer, Object obj)
                throws IOException {
            writer.text(obj.toString());
        }
    }

class MarshalByte implements Marshal {
    public Object readInstance(XmlPullParser parser, String namespace,
                               String name, PropertyInfo expected) throws IOException,
            XmlPullParserException {

        return Byte.parseByte(parser.nextText());
    }

    public void register(SoapSerializationEnvelope cm) {
        cm.addMapping(cm.xsd, null, Byte.class, this);

    }



    public void writeInstance(XmlSerializer writer, Object obj)
            throws IOException {
        writer.text(obj.toString());
    }
}
class MarshalString implements Marshal {
    public Object readInstance(XmlPullParser parser, String namespace,
                               String name, PropertyInfo expected) throws IOException,
            XmlPullParserException {

        return String.valueOf(parser.nextText());
    }

    public void register(SoapSerializationEnvelope cm) {
        cm.addMapping(cm.xsd, "string", String.class, this);

    }
    public void writeInstance(XmlSerializer writer, Object obj)
            throws IOException {
        writer.text(obj.toString());
    }
}

class xmlparser implements XmlPullParser {


    @Override
    public void setFeature(String name, boolean state) throws XmlPullParserException {

    }

    @Override
    public boolean getFeature(String name) {
        return false;
    }

    @Override
    public void setProperty(String name, Object value) throws XmlPullParserException {

    }

    @Override
    public Object getProperty(String name) {
        return null;
    }

    @Override
    public void setInput(Reader in) throws XmlPullParserException {

    }

    @Override
    public void setInput(InputStream inputStream, String inputEncoding) throws XmlPullParserException {

    }

    @Override
    public String getInputEncoding() {
        return null;
    }

    @Override
    public void defineEntityReplacementText(String entityName, String replacementText) throws XmlPullParserException {

    }

    @Override
    public int getNamespaceCount(int depth) throws XmlPullParserException {
        return 0;
    }

    @Override
    public String getNamespacePrefix(int pos) throws XmlPullParserException {
        return null;
    }

    @Override
    public String getNamespaceUri(int pos) throws XmlPullParserException {
        return null;
    }

    @Override
    public String getNamespace(String prefix) {
        return null;
    }

    @Override
    public int getDepth() {
        return 0;
    }

    @Override
    public String getPositionDescription() {
        return null;
    }

    @Override
    public int getLineNumber() {
        return 0;
    }

    @Override
    public int getColumnNumber() {
        return 0;
    }

    @Override
    public boolean isWhitespace() throws XmlPullParserException {
        return false;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public char[] getTextCharacters(int[] holderForStartAndLength) {
        return new char[0];
    }

    @Override
    public String getNamespace() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public boolean isEmptyElementTag() throws XmlPullParserException {
        return false;
    }

    @Override
    public int getAttributeCount() {
        return 0;
    }

    @Override
    public String getAttributeNamespace(int index) {
        return null;
    }

    @Override
    public String getAttributeName(int index) {
        return null;
    }

    @Override
    public String getAttributePrefix(int index) {
        return null;
    }

    @Override
    public String getAttributeType(int index) {
        return null;
    }

    @Override
    public boolean isAttributeDefault(int index) {
        return false;
    }

    @Override
    public String getAttributeValue(int index) {
        return null;
    }

    @Override
    public String getAttributeValue(String namespace, String name) {
        return null;
    }

    @Override
    public int getEventType() throws XmlPullParserException {
        return 0;
    }

    @Override
    public int next() throws XmlPullParserException, IOException {
        return 0;
    }

    @Override
    public int nextToken() throws XmlPullParserException, IOException {
        return 0;
    }

    @Override
    public void require(int type, String namespace, String name) throws XmlPullParserException, IOException {

    }

    @Override
    public String nextText() throws XmlPullParserException, IOException {
        return null;
    }

    @Override
    public int nextTag() throws XmlPullParserException, IOException {
        return 0;
    }
}
/*class MarshalInt64 implements Marshal {
    public Object readInstance(XmlPullParser parser, String namespace,
                               String name, PropertyInfo expected) throws IOException,
            XmlPullParserException {

        return Double.parseDouble(parser.nextText());
    }

    public void register(SoapSerializationEnvelope cm) {
        cm.addMapping(cm.xsd, "Int64", Double.class, this);

    }

    public void writeInstance(XmlSerializer writer, Object obj)
            throws IOException {
        writer.text(obj.toString());
    }
}*/