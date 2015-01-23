package com.example.telphone.activity;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.telphone.R;
import com.example.telphone.tool.Variable;

public class Lottery extends Activity{

	private static final String TAG="Lottery";
//	private WebView wv;d
	private TextView tv_msg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		
		QueryTask qt = new QueryTask();
		qt.execute();
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.lottery);
		tv_msg =(TextView)findViewById(R.id.tv_lottery);
//		wv = (WebView)findViewById(R.id.webView1);
		
//		Intent it = this.getIntent();
//		String url = it.getStringExtra("url");
//		wv.loadUrl(url);
//		wv.setWebViewClient(new WebViewClientDemo());
	}

	private class WebViewClientDemo extends WebViewClient {
	    @Override
	    // 在WebView中而不是默认浏览器中显示页面
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }
	}
	class QueryTask extends AsyncTask<Integer, Integer, String>
	{

		@Override
		protected String doInBackground(Integer... arg0) {
			
			HttpClient client = new DefaultHttpClient();  
			//http://121.40.100.250:99/CallReqRet.php?UserID=13262878009&CallTo=balance&Wap=json
	        
			SharedPreferences sp = Lottery.this.getSharedPreferences(Variable.SHARE_PRE_NAME, Activity.MODE_PRIVATE);
			
			String phone = sp.getString("phone", "");
			HttpGet get = new HttpGet("http://121.40.100.250:99/CallReqRet.php?UserID="
					+phone+"&CallTo=choujiang&Wap=json");  
	        HttpResponse response= null;
	        BufferedReader in = null;  
			try {
				response = client.execute(get);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	        	try {
					String result = EntityUtils.toString(response.getEntity());
					JSONTokener json = new JSONTokener(result);
					 JSONObject jo = (JSONObject) json.nextValue();
					 String bal = jo.getString("msg");
					 Log.d(TAG,result);
					 
					 return bal;
					
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			tv_msg.setText(result);
		}
		
		
	}
}
