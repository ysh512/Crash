package com.example.telphone.activity;

import com.example.telphone.R;

import android.os.Bundle;

public class Tel extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
	}

	private void initView() {
		this.setLinearView(R.layout.tel);
	}

	
}
