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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.example.telphone.R;
import com.example.telphone.tool.Variable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class QueryBalance extends BaseActivity{

	private static final String TAG="QueryBalance";
	
	private TextView tv_balance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		QueryTask qt = new QueryTask();
		qt.execute();
	}

	private void initView() {

		tv_title.setText(R.string.query_balance);
		this.setLinearView(R.layout.balance);
		tv_balance = (TextView)findViewById(R.id.tv_balance);
		
		
	}
	
	class QueryTask extends AsyncTask<Integer, Integer, String>
	{
		ProgressDialog dialog ;
		boolean query = false;

		@Override
		protected String doInBackground(Integer... arg0) {
			HttpClient client = new DefaultHttpClient();  
			//http://121.40.100.250:99/CallReqRet.php?UserID=13262878009&CallTo=balance&Wap=json
	        
			SharedPreferences sp = QueryBalance.this.getSharedPreferences(Variable.SHARE_PRE_NAME, Activity.MODE_PRIVATE);
			
			String phone = sp.getString("phone", "");
			HttpGet get = new HttpGet("http://121.40.100.250:99/CallReqRet.php?UserID="
					+phone+"&CallTo=balance&Wap=json");  
			HttpResponse response = null;
			BufferedReader in = null;
			try {
				response = client.execute(get);
				String result = EntityUtils.toString(response.getEntity());
				JSONTokener json = new JSONTokener(result);
				JSONObject jo = (JSONObject) json.nextValue();
				String bal = jo.getString("yuer");
				query = true;
				Log.d(TAG, result);

				return bal;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String result) {
			  dialog.dismiss();
			  if(result!=null)
			  {
				  tv_balance.setText(result);
			  }
			  if(!query)
			  {
				  Toast.makeText(QueryBalance.this, "≤È—Ø ß∞‹£¨«ÎºÏ≤ÈÕ¯¬Á∫Û÷ÿ ‘", 5).show();
			  }
			  
		}

		protected void onPreExecute() {
			dialog = ProgressDialog.show(QueryBalance.this, "”‡∂Ó≤È—Ø", "’˝‘⁄≤È—Ø,«Î…‘∫Ú");
			
			
		}
		
	}

	
}
