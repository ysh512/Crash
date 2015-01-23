package com.example.telphone.activity;

import com.example.telphone.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaseActivity extends Activity{

	public  TextView tv_title;
	private  RelativeLayout rl_content;
	private  RelativeLayout rl_title;
	private ImageView iv_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.base);		
		tv_title = (TextView)findViewById(R.id.tv_title);
		rl_content = (RelativeLayout)findViewById(R.id.rl_content);
		rl_title = (RelativeLayout)findViewById(R.id.rl_title);
		iv_back = (ImageView)findViewById(R.id.iv_b_back);
		
		iv_back.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				BaseActivity.this.finish();
			}
			
		});
	}
	
	
	public void setLinearView(int ResId)
	{
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(ResId, rl_content,false);
		rl_content.addView(v);
	}
	
//	public void setScrollView(int ResId)
//	{
//		
//	}
	
	public void setTitle(String title)
	{
		tv_title.setText(title);
	}

	public void hideTitle()
	{
		rl_title.setVisibility(View.GONE);
	}
	
	public void showTitle()
	{
		rl_title.setVisibility(View.VISIBLE);
	}
	
	public void showBackPic()
	{
		iv_back.setVisibility(View.VISIBLE);
	}
}
