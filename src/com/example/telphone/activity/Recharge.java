package com.example.telphone.activity;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.telphone.R;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
 * recharge
 */
public class Recharge extends BaseActivity implements OnClickListener{

	private static final String TAG="Recharge";
	//---------------------UI----------------------------
	private Button btn_recharge;
	private EditText et_phone;
	private EditText et_card;
	private EditText et_pass;
	
	//http://121.40.100.250:99/CallReqRet.php?UserID=%s&CallTo=regdeal&Cardpwd=
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		initView();
		this.showBackPic();
		
		
		
	}

	private void initView() {
		this.setLinearView(R.layout.recharge);
		tv_title.setText("官方充值卡");
		
		btn_recharge = (Button)findViewById(R.id.btn_r_charge);
		et_phone = (EditText)findViewById(R.id.et_telnumber);
		et_card = (EditText)findViewById(R.id.et_cardnumber);
		et_pass = (EditText)findViewById(R.id.et_cardpass);
		
		btn_recharge.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId())
		{
		case R.id.btn_r_charge:
			ChargeTask ct = new ChargeTask();
			ct.execute();
			break;
		default :
			break;
		}
	}
	
	class ChargeTask extends AsyncTask<Integer, Integer, String>
	{
		boolean ready = false;
		boolean charge = false;
		
		
		ProgressDialog dialog;

		@Override
		protected String doInBackground(Integer... arg0) {
			if(ready)
			{
				HttpClient client = new DefaultHttpClient();  
		        HttpGet get = new HttpGet("http://121.40.100.250:99/CallReqRet.php?UserID="
				+et_phone.getText().toString()+"&CallTo=regdeal&Cardpwd="+et_pass.getText().toString()+"&Wap=json");  
		        HttpResponse response= null;
				BufferedReader in = null;
				try {
					response = client.execute(get);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

						String result = EntityUtils.toString(response
								.getEntity());

						if (result.contains("1") || result.contains("OK")) {
							charge = true;
						} else {

						}
						Log.d(TAG, result);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;
		}
		protected void onPostExecute(String result) 
		{
			dialog.dismiss();
			if(charge)
			{
				Toast.makeText(Recharge.this, "充值成功", 5).show();
			}
			else
			{
				Toast.makeText(Recharge.this, "充值失败", 5).show();
			}
		}
		protected void onPreExecute() {  
			if(et_phone.getText()==null || et_card.getText()==null)
			{
				Toast.makeText(Recharge.this, "请填写手机号、卡号", 8).show();
				
			}
			else
			{
				ready = true;
				dialog = ProgressDialog.show(Recharge.this, "充值", "正在充值，请稍后...");
				dialog.show();
				
				
			}
	    }  

	}
	
}
