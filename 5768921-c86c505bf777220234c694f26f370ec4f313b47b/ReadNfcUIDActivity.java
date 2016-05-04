// Needed in AndroidManifest:
<!-- Permission for using NFC hardware -->
<uses-permission android:name="android.permission.NFC"/>
<!-- Forcing device to have NFC hardware -->
<uses-feature android:name="android.hardware.nfc" android:required="true"/>
<!-- Registering app for receiving NFC's TAG_DISCOVERED intent -->
<intent-filter>
  <action android:name="android.nfc.action.TAG_DISCOVERED"/>
  <category android:name="android.intent.category.DEFAULT"/>
</intent-filter>




// handling ACTION_TAG_DISCOVERED action from intent:
@Override
protected void onResume() {
	super.onResume();
	if (getIntent().getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
		((TextView)findViewById(R.id.text)).setText(
				"NFC Tag\n" + 
				this.ByteArrayToHexString(getIntent().getByteArrayExtra(NfcAdapter.EXTRA_ID)));
	}
}

// Converting byte[] to hex string:
private String ByteArrayToHexString(byte [] inarray) {
    int i, j, in;
    String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
    String out= "";

    for(j = 0 ; j < inarray.length ; ++j) 
        {
        in = (int) inarray[j] & 0xff;
        i = (in >> 4) & 0x0f;
        out += hex[i];
        i = in & 0x0f;
        out += hex[i];
        }
    return out;
}