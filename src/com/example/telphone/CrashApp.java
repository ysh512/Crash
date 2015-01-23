package com.example.telphone;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

public class CrashApp extends Application{

	private static final String TAG="CrashApp";
	private static Context mContext;
	
	private static List<Activity> mList = new LinkedList<Activity>();
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		Log.d(TAG,"onCreate");
		
		mContext = getApplicationContext();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		
	}
	
	public void addActivity(Activity activity)
	{
		if(!mList.contains(activity))
		{
			mList.add(activity);
		}
	}
	
	public void removeActivity(Activity activity)
	{
		if(mList.contains(activity))
		{
			mList.remove(activity);
		}
	}
	
	public static void finishAll()
	{
		for(Activity activity:mList)
		{
			activity.finish();
		}
	}
	
}
