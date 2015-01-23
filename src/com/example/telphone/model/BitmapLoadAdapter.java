package com.example.telphone.model;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class BitmapLoadAdapter extends PagerAdapter{
	public static final String TAG = "MenuAdAdapter";
//	private ImageView[] mImageViews;
	private ArrayList<ImageView> picList;
	
//	public BitmapLoadAdapter(ImageView[] views)
//	{
//		this.mImageViews = new ImageView[views.length];
//		for(int i=0;i<views.length;i++)
//		{
//			mImageViews[i]= views[i];
//		}
////		this.mImageViews = views;
//	}
	
	public BitmapLoadAdapter(ArrayList<ImageView> list)
	{
		this.picList = new ArrayList<ImageView>();
		for(int i=0;i<list.size();i++)
		{
			picList.add(list.get(i));
		}
	}
	
	@Override
	public int getCount(){ return picList.size();} // max value for permanent loop

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {return arg0 == arg1;}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(picList.get(position % picList.size()));
	}
	
	//载入图片进去，用当前的position 除以 图片数组长度取余数是关键
	@Override
	public Object instantiateItem(View container, int position) {
//		 ((ViewPager) container).addView(mImageViews[position], position);
//		 
//		 Log.d(TAG,"POSITION:"+position);
//	        return mImageViews[position];
		((ViewPager)container).addView(picList.get(position),0);
		Log.d(TAG,"mImageViews length:"+picList.size());
		Log.d(TAG,"position:"+position);
		return picList.get(position);
		
	}
}
