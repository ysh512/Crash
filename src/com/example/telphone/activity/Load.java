package com.example.telphone.activity;


import com.example.telphone.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

public class Load extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.load);
		
		
		new Handler().postDelayed(new Runnable() {  
            public void run() {  
                //Go to main activity, and finish load activity  
                Intent mainIntent = new Intent(Load.this, Container.class);  
                Load.this.startActivity(mainIntent);  
                Load.this.finish();  
            }  
        }, 500);   
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
}
