package com.example.telphone;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import com.example.telphone.tool.Variable;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.Environment;




public class TelApplication extends Application{

	private static Context appContext;
	private static ArrayList<Activity> mList = new ArrayList<Activity>();
	
	 
	 public static String phone;
	 
	 public static boolean CallingRunning = false;
	@Override
	public void onCreate() {
		super.onCreate();
		appContext = getApplicationContext();
		
		//create log

		
		//Update Appinfo cache
		
	}
	
	public static void init(Context context)
	{
		SharedPreferences s= context.getSharedPreferences(Variable.SHARE_PRE_NAME, Activity.MODE_PRIVATE);
		Editor e = s.edit();
//		if(e)
		if(!s.contains("auto"))
		{
			e.putBoolean("auto", true);
			e.commit();
		}
		if(s.contains("phone"))
		{
			phone = s.getString("phone", "");
			QueryInfo qi = new QueryInfo(context,s.getString("phone", ""));
			new Thread(qi).start();
		}
		
		
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	public static Context getAppContext(){
		return appContext;
	}
	
	public static void add(Activity act) {
		// add to the list.
		
		if( mList.contains(act)== false ){
			mList.add(act);
		}
	}
	
	public static void remove(Activity act) {
		// remove from the list.
		
		if( mList.contains(act)== true ){
			mList.remove(act);
		}
	}

	public static void finishAll() {
		// call finish of all activities in the list.
		// Check for reference validity before calling a method on it.
		
		Iterator<Activity> ite = mList.iterator();
		while( ite.hasNext() ){
			ite.next().finish();
		}
	}
	
	public static void setCallingRunning(Boolean b)
	{
		CallingRunning = b;
	}
	
	public static boolean getCallingRunning()
	{
		return CallingRunning;
	}
	
}
