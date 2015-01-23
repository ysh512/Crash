package com.example.telphone.model;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MenuAdAdapter extends PagerAdapter{
	public static final String TAG = "MenuAdAdapter";
	private ImageView[] mImageViews;
	
	public MenuAdAdapter(ImageView[] views)
	{
		this.mImageViews = new ImageView[views.length];
		for(int i=0;i<views.length;i++)
		{
			mImageViews[i]= views[i];
		}
//		this.mImageViews = views;
	}
	
	@Override
	public int getCount(){ return mImageViews.length;} // max value for permanent loop

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {return arg0 == arg1;}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(mImageViews[position % mImageViews.length]);
	}
	
	//载入图片进去，用当前的position 除以 图片数组长度取余数是关键
	@Override
	public Object instantiateItem(View container, int position) {
//		 ((ViewPager) container).addView(mImageViews[position], position);
//		 
//		 Log.d(TAG,"POSITION:"+position);
//	        return mImageViews[position];
		((ViewPager)container).addView(mImageViews[position],0);
		Log.d(TAG,"mImageViews length:"+mImageViews.length);
		Log.d(TAG,"position:"+position);
		return mImageViews[position];
		
	}
}
