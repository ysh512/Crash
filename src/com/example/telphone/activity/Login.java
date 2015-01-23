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

import com.example.telphone.TelApplication;
import com.example.telphone.R;
import com.example.telphone.tool.Variable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends BaseActivity implements OnClickListener{
	
	private static final String TAG= "Login";
	
	//---------------------UI-----------------------
	
	//login url : http://121.40.100.250:99/CallReqRet.php?UserID=%s&CallTo=auth
	private Button btn_login;
	private Button btn_recharge;
	private EditText et_account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		
		initView();
		
		TelApplication.add(this);
	}

	private void initView() {
		this.setLinearView(R.layout.login);
		btn_login = (Button)findViewById(R.id.btn_login);
		btn_recharge = (Button)findViewById(R.id.btn_recharge);
		
		btn_login.setOnClickListener(this);
		btn_recharge.setOnClickListener(this);
		et_account = (EditText)findViewById(R.id.editText1);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId())
		{
		case R.id.btn_login:
			LoginTask lt = new LoginTask();
			lt.execute();
			break;
		case R.id.btn_recharge:
			Intent it = new Intent(this,Recharge.class);
			startActivity(it);
			break;
		default:
			break;
		}
	}
	
	class LoginTask extends AsyncTask<Integer, Integer, String>
	{
		boolean login = false;
		
		ProgressDialog dialog ;
		
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(Login.this, "登录", "正在登陆...");
//			dialog.show();
		}

		@Override
		protected String doInBackground(Integer... params) {
			
			HttpClient client = new DefaultHttpClient();  
	        HttpGet get = new HttpGet("http://121.40.100.250:99/CallReqRet.php?UserID="+et_account.getText().toString()+"&CallTo=auth");  
	        HttpResponse response= null;
	        BufferedReader in = null;  
			try {
				response = client.execute(get);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					login = true;
					String result = EntityUtils.toString(response.getEntity());

					Log.d(TAG, result);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         
			
			
			return null;
		}
		
		protected void onPostExecute(String result) 
		{
			dialog.dismiss();
			if(login)
			{
				
//				Intent it = new Intent(Login.this,Container.class);
//				startActivity(it);
				//实例化SharedPreferences对象（第一步） 
				SharedPreferences mySharedPreferences= getSharedPreferences(Variable.SHARE_PRE_NAME, Activity.MODE_PRIVATE); 
				//实例化SharedPreferences.Editor对象（第二步） 
				SharedPreferences.Editor editor = mySharedPreferences.edit(); 
				//用putString的方法保存数据 
				editor.putString("login", "true"); 
				editor.putString("phone", et_account.getText().toString()); 
				//提交当前数据 
				editor.commit(); 
				finish();
				//使用toast信息提示框提示成功写入数据 

			}
			else
			{
				Toast.makeText(Login.this, "登录失败，请检查网络和用户名", 5).show();
			}
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		TelApplication app = ((TelApplication)getApplicationContext());
//        app.remove(this);
		TelApplication.remove(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		TelApplication.finishAll();
		this.finish();
	}
	
	
	
}
