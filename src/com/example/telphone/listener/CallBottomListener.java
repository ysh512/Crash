package com.example.telphone.listener;

import com.example.telphone.R;
import com.example.telphone.activity.Calling;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CallBottomListener implements OnClickListener{

	private TextView tv_title;
	private LinearLayout ll_bottom;
	private RelativeLayout ll_callPad;
	private LinearLayout ll_numPad;
	private Context context;
	
	private Button bt_tel;
	private Button bt_cancel;
	
	private Dialog dialog;
//	private View oriView;
//	private Context context;
	
	public CallBottomListener( Context context, TextView title,LinearLayout bottom,RelativeLayout call_pad, LinearLayout num_pad)
	{
		tv_title = title;
		ll_bottom = bottom;
		ll_callPad = call_pad;
		ll_numPad = num_pad;
		this.context = context;
//		oriView = view;
//		this.context = context;
	}
	@Override
	public void onClick(View arg0) {
		
		switch (arg0.getId())
		{
		case R.id.iv_cb_call:
			
//			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			AlertDialog.Builder builder = new AlertDialog.Builder(context);
//			View v = inflater.inflate(R.layout.dialog_tel, null);
//			builder.setView(v);
//			bt_tel = (Button)v.findViewById(R.id.bt_dt_tel);
//			bt_cancel = (Button)v.findViewById(R.id.bt_dt_cancel);
//			
//			dialog = builder.show();
//			
//			bt_tel.setOnClickListener(this);
//			bt_cancel.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View arg0) {
//					dialog.dismiss();
//				}
//				
//			});
			
			
			Intent it = new Intent(context,Calling.class);
			it.putExtra("number", tv_title.getText());
			context.startActivity(it);
			
			break;
		case R.id.iv_cb_keyboard:
			ImageView iv = (ImageView)arg0;
			if(View.INVISIBLE ==ll_numPad.getVisibility())
			{
				ll_numPad.setVisibility(View.VISIBLE);
				iv.setImageResource(0);
				iv.setImageResource(R.drawable.calldown);
			}
			else
			{
				ll_numPad.setVisibility(View.INVISIBLE);
				iv.setImageResource(0);
				iv.setImageResource(R.drawable.callup);
			}
			break;
		case R.id.iv_cb_del:
			String title = tv_title.getText().toString();
			if(title.length()>=2)
			{
				String t = title.substring(0, title.length()-1);
				tv_title.setText(t);
			}
			else
			{
				tv_title.setText(R.string.company_name);
				ll_callPad.setVisibility(View.GONE);
				ll_bottom.setVisibility(View.VISIBLE);
				ll_numPad.setVisibility(View.VISIBLE);

			}
			break;
			
		case R.id.bt_dt_tel:
			
			Intent it2 = new Intent(context,Calling.class);
			it2.putExtra("number", tv_title.getText());
			context.startActivity(it2);
			dialog.dismiss();
			
			break;
		default:
			break;
		}
		
	}
	private void startActivity(Intent it) {
		// TODO Auto-generated method stub
		
	}

}
