package com.example.telphone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.example.telphone.tool.Variable;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.util.Log;

public class QueryInfo implements Runnable{

	//http://121.40.100.250:88/CallReqRet.php?UserID=%s&CallTo=dialpan&Wap=json
	
	 public final static String ALBUM_PATH  
     = Environment.getExternalStorageDirectory() + "/lingxidownload/";
	private String phone;
	private Context context;
	private SharedPreferences sp;
	public QueryInfo(Context context,String phone)
	{
		this.context = context;
		this.phone = phone;
	}
	
	@Override
	public void run() {

		
		sp = context.getSharedPreferences(Variable.SHARE_PRE_NAME,
				Activity.MODE_PRIVATE);
		
		String ad_pic_url = "http://121.40.100.250:99/CallReqRet.php?UserID="
				+ phone + "&CallTo=dialpan&Wap=json";

		String result = getQueryResult(ad_pic_url);
		
		try {
			// String result = EntityUtils.toString(response.getEntity());
			JSONTokener json = new JSONTokener(result);
			JSONObject jo = (JSONObject) json.nextValue();
			int i = 0;

			// ad10-ad19 tel pad ad picture£»
			while (jo.has(String.valueOf(i))) {
				JSONObject sub = (JSONObject) jo.get(String.valueOf(i));
				String imageUrl = sub.getString("imgurl");
				String date = sub.getString("date");
				String dateSaved = sp.getString("ad1" + String.valueOf(i),
						"1970-1-1");
				if (compareDate(dateSaved, date) ) {
					String fileName = ALBUM_PATH+"ad1"+String.valueOf(i)+".jpg";
					
					downAdPic(fileName, imageUrl);

				}
				Editor e = sp.edit();
				e.putString("ad1" + String.valueOf(i), date);
				e.commit();
				i++;
			}

			while (sp.contains("ad1" + String.valueOf(i))) {
				sp.edit().remove("ad1" + String.valueOf(i));
				sp.edit().commit();
				try
				{
					File f = new File(ALBUM_PATH+"ad1"+String.valueOf(i)+".jpg");
					if(f.exists())
					{
						f.delete();
					}
				}catch (Exception e)
				{
					e.printStackTrace();
				}
				i++;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		// }

//		updateAdString();
		
		updateCallingAdPic();
		
//		updateMenuAdPic();
		
	}
	
	
	private void updateMenuAdPic() {

		String picUrl = "http://121.40.100.250:99/CallReqRet.php?UserID"+phone+"&CallTo=more&Wap=json";
		String result = getQueryResult(picUrl);
		
		
		
		try {
			JSONTokener json = new JSONTokener(result);
			JSONObject jo = (JSONObject) json.nextValue();
			
			int i =0;
			while (jo.has(String.valueOf(i))) {
				JSONObject sub = (JSONObject) jo.get(String.valueOf(i));
				String imageUrl = sub.getString("imgurl");
				String date = sub.getString("date");
				String dateSaved = sp.getString("ad3" + String.valueOf(i),
						"1970-1-1");
				if (compareDate(dateSaved, date) ) {
					String fileName = ALBUM_PATH+"ad3"+String.valueOf(i)+".jpg";
					
					downAdPic(fileName, imageUrl);
					Editor e = sp.edit();
					e.putString("ad3" + String.valueOf(i), date);
					e.commit();
				}
				i++;
			}

			while (sp.contains("ad3" + String.valueOf(i))) {
				sp.edit().remove("ad3" + String.valueOf(i));
				try
				{
					File f = new File(ALBUM_PATH+"ad3"+String.valueOf(i)+".jpg");
					if(f.exists())
					{
						f.delete();
					}
				}catch (Exception e)
				{
					e.printStackTrace();
				}
				i++;
			}

			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void updateCallingAdPic() {

		String picUrl = "http://121.40.100.250:99/CallReqRet.php?UserID="+phone+"&CallTo=dialadv&Wap=json";
		String result = getQueryResult(picUrl);
		
		
		try {
			JSONTokener json = new JSONTokener(result);
			JSONObject jo = (JSONObject) json.nextValue();
			
			String date = jo.getString("date");
			String dateSaved = sp.getString(Variable.DATE_CALLING_DSIPLAY,"1970-1-1");
			
			if (this.compareDate(dateSaved,date))
			{
				String url = jo.getString("imgurl");
				String fileName=this.ALBUM_PATH+Variable.CALLING_AD_PIC_NAME;
				this.downAdPic(fileName, url);
				Editor e = sp.edit();
				e.putString(Variable.DATE_CALLING_DSIPLAY, date);
				e.commit();
				
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void updateAdString() {
//		String str = sp.getString(Variable.SHARE_PRE_NAME, "");
//		if(str == null)
//		{
//			
//		}
		//http://121.40.100.250:99/CallReqRet.php?UserID=13262878009&CallTo=marquee&Wap=json
		String queryUrl = "http://121.40.100.250:99/CallReqRet.php?UserID="+phone+"&CallTo=marquee&Wap=json";
		String result = getQueryResult(queryUrl);
		if(null!=result)
		{
			
			try {
				JSONTokener json = new JSONTokener(result);
				JSONObject jo = (JSONObject) json.nextValue();
				String msg = jo.getString("title");
				Editor e = sp.edit();
				e.putString(Variable.FILED_MARQUEEN_STR, msg);
				e.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String getQueryResult(String getUrl) {
		HttpClient client = new DefaultHttpClient();  
        HttpGet get = new HttpGet(getUrl);  
        HttpResponse response= null;
        BufferedReader in = null;  
		try {
			response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
				String result = EntityUtils.toString(response.getEntity());
				
				return result;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		
		return null;
	}

	private void downAdPic(String fileName,String path) {
		URL url;
		File dir = new File(ALBUM_PATH);
		if(!dir.exists())
		{
			dir.mkdir();
		}
		try {
			url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				File f = new File(fileName);
				if(f.exists())
				{
					f.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(f);
				InputStream is =  conn.getInputStream();
				byte b[] = new byte[1024];
				int byteCount = 0;
				while((byteCount = is.read(b))!=-1)
				{
					fos.write(b, 0, byteCount);
				}
				fos.close();
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
  
	}

	// if date2 > date1 return true;
	private boolean compareDate(String date1,String date2)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d1 = sdf.parse(date1);
			Date d2 = sdf.parse(date2);
			if(d1.compareTo(d2)<0)
			{
				return true;
			}
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			return false;
		}
	
		return false;
	}
}
