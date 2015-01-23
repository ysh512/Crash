package com.example.telphone.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.telphone.QueryInfo;
import com.example.telphone.R;
import com.example.telphone.TelApplication;
import com.example.telphone.tool.Variable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Calling extends Activity{
	private static final String TAG = "Calling";

	public static Context context;
	public static TelephonyManager tm;
	
	private TextView tv_phone;
	private TextView tv_name;
	private String phone;
	private ImageView iv_ad;
	public static MediaPlayer mediaPlayer;
	
	private TextView tv_count;
	private Boolean m_running = true;
	
	private ImageView iv_loading;
	private Button btn_cancel;
	
	//http://121.40.100.250:99/CallReqRet.php?UserID=%s&CallTo=%s
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		context = this;
		TelApplication.setCallingRunning(true);
		
		initView();
		Intent it =this.getIntent();
		phone = it.getStringExtra("number");
		phone = phone.replace(" ","");
		phone = phone.replace("-", "");
		tv_phone.setText(phone);
		
		String name = it.getStringExtra("name");
		if(name == null || name=="Î´Öª" )
		{
			name = phone;
		}
		tv_name.setText(name);
		
		CallingTask ct =new CallingTask();
		ct.execute();
		
		Log.d(TAG,phone);
	}

	private void initView() {
		this.setContentView(R.layout.calling);
		tv_phone = (TextView)findViewById(R.id.tv_calling_number);
		iv_ad = (ImageView)findViewById(R.id.iv_calling_ad);
		iv_loading = (ImageView)findViewById(R.id.iv_c_loading);
		tv_name = (TextView)findViewById(R.id.tv_calling_name);
		btn_cancel = (Button)findViewById(R.id.btn_calling_cancel);
		
		btn_cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {

				Log.d(TAG, "back ground music");
				stopPlayBgMusic();
				Calling.this.finish();
			}
			
		});
		String fileName =QueryInfo.ALBUM_PATH+ Variable.CALLING_AD_PIC_NAME;
		if(new File(fileName).exists())
		{
			Bitmap bm = BitmapFactory.decodeFile(fileName, null);
			iv_ad.setImageBitmap(bm);
		}
		
		tv_count = (TextView)findViewById(R.id.tv_calling_count);
		
		new Handler(){
			int i =1;
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				tv_count.setText(i+"Ãë");
				i++;
				
				iv_loading.setImageResource(0);
				switch(i%5){
				case 2:
					iv_loading.setImageResource(R.drawable.d2);
					break;
				case 3:
					iv_loading.setImageResource(R.drawable.d3);
					break;
				case 4:
					iv_loading.setImageResource(R.drawable.d4);
					break;
				case 0:
					iv_loading.setImageResource(R.drawable.d5);
					break;
				case 1:
					iv_loading.setImageResource(R.drawable.d1);
				}
				if(m_running)
				{
					sendEmptyMessageDelayed(0, 1000);
				}
				
			}
			
		}.sendEmptyMessageDelayed(0, 1000);
		
	}

	
	class CallingTask extends AsyncTask<Integer, Integer, String>
	{

		protected void onPreExecute() { 
			
			playMusic();
		}
		
		protected String doInBackground(Integer... arg0) {
			
			SharedPreferences sharedPreferences= getSharedPreferences(Variable.SHARE_PRE_NAME, 
					Activity.MODE_PRIVATE); 
			String from = sharedPreferences.getString("phone","");
			
			HttpClient client = new DefaultHttpClient();  
	        HttpGet get = new HttpGet("http://121.40.100.250:99/CallReqRet.php?UserID="+from+"&CallTo="+phone);
	        HttpResponse response= null;
	        BufferedReader in = null;  
	        try {
				response = client.execute(get);
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				{
					
					String result = EntityUtils.toString(response.getEntity());
					Log.d(TAG,result);
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		
		protected void onPostExecute(String result) 
		{
//			playMusic();
		}
		
		
	}
	private void playMusic()
	{
//		AssetManager assestManager = Calling.this.getAssets();
		
		//			AssetManager assestManager = Calling.this.getAssets();
		//			AssetFileDescriptor fileDescriptor = assestManager.openFd("wait.mp3");
		//			mediaPlayer = new MediaPlayer();
		//			mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor());
		//			mediaPlayer.prepare();
					mediaPlayer = MediaPlayer.create(this, R.raw.wait);
					mediaPlayer.start();
		
		
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		
		
		if(mediaPlayer!=null && mediaPlayer.isPlaying())
		{
//			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			
		}
		m_running = false;
		TelApplication.setCallingRunning(false);
		Calling.context = null;
		super.onDestroy();
	}
	
	public static void stopPlayBgMusic()
	{
		if(null!= mediaPlayer)
		{
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
}
