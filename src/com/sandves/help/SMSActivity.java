package com.sandves.help;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMSActivity extends Activity {
	
	private EditText mInputNumber;
	private EditText mInputText;
	private Button mSend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms);
		
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);  
		
		mInputNumber = (EditText) findViewById(R.id.smsnumber);
		mInputText = (EditText) findViewById(R.id.smstext);
		mSend = (Button) findViewById(R.id.sendsms); 
		
		Intent intent = getIntent();
		String number = intent.getStringExtra("sms_number");
		String text = intent.getStringExtra("sms_body");
		
		mInputNumber.setText(number);
		mInputText.setText(text);
		
		mSend.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				SmsManager smsManager = SmsManager.getDefault();
			    String smsNumber = mInputNumber.getText().toString();
			    String smsText = mInputText.getText().toString();
			    try {
					smsManager.sendTextMessage(smsNumber, null, smsText, null, null);
				} catch (IllegalArgumentException e) {
					Toast.makeText(getApplicationContext(), getString(R.string.sms_exception), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			    Toast.makeText(getApplicationContext(), getString(R.string.sms_success), Toast.LENGTH_LONG).show();
			    finish();
			}});
	}
}
