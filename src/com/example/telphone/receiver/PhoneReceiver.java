package com.example.telphone.receiver;

import com.example.telphone.TelApplication;
import com.example.telphone.activity.Calling;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

public class PhoneReceiver extends BroadcastReceiver {

	private static final String TAG = "PhoneReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("action" + intent.getAction());
		// 如果是去电
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			String phoneNumber = intent
					.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			Log.d(TAG, "call OUT:" + phoneNumber);
		} else {
			// 查了下android文档，貌似没有专门用于接收来电的action,所以，非去电即来电.
			// 如果我们想要监听电话的拨打状况，需要这么几步 :
			// * 第一：获取电话服务管理器TelephonyManager manager =
			// this.getSystemService(TELEPHONY_SERVICE);
			// * 第二：通过TelephonyManager注册我们要监听的电话状态改变事件。manager.listen(new
			// MyPhoneStateListener(),
			// *
			// PhoneStateListener.LISTEN_CALL_STATE);这里的PhoneStateListener.LISTEN_CALL_STATE就是我们想要
			// * 监听的状态改变事件，初次之外，还有很多其他事件哦。
			// * 第三步：通过extends PhoneStateListener来定制自己的规则。将其对象传递给第二步作为参数。
			// * 第四步：这一步很重要，那就是给应用添加权限。android.permission.READ_PHONE_STATE

			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Service.TELEPHONY_SERVICE);
			tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
			// 设置一个监听器
		}
	}

	PhoneStateListener listener = new PhoneStateListener() {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// 注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:

//				System.out.println("挂断");
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
//				System.out.println("接听");
				//stop background music

				break;
			case TelephonyManager.CALL_STATE_RINGING:
//				System.out.println("响铃:来电号码" + incomingNumber);
				

				Calling.stopPlayBgMusic();
				
//				if(null!=Calling.mediaPlayer && Calling.mediaPlayer.isPlaying())
//				{
//					Calling.mediaPlayer.stop();
//					Calling.mediaPlayer.release();
//					Calling.mediaPlayer = null;
//				}
				
				// 输出来电号码
				if(TelApplication.getCallingRunning())
				{
					SharedPreferences s= Calling.context.getSharedPreferences("test", Activity.MODE_PRIVATE);				
					try
					{
						if(s.getBoolean("auto", false) && TelApplication.getCallingRunning())
						{
							Thread.sleep(1500);
							answerRingingCallWithBroadcast(Calling.context,Calling.tm);
							
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					

					if(null!=Calling.context)
					{
						((Activity) Calling.context).finish();
						Calling.context = null;
					}
				}
				

				
				TelApplication.setCallingRunning(false);

				
				break;
			}
		}
	};
	
	


	private void answerRingingCallWithBroadcast(Context context,
			TelephonyManager telmanager) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		// 判断是否插上了耳机
		if (!audioManager.isWiredHeadsetOn()) {
			// 4.1以上系统限制了部分权限， 使用三星4.1版本测试提示警告：Permission Denial: not allowed to
			// send broadcast android.intent.action.HEADSET_PLUG from pid=1324,
			// uid=10017
			// 这里需要注意一点，发送广播时加了权限“android.permission.CALL_PRIVLEGED”，则接受该广播时也需要增加该权限。但是4.1以上版本貌似这个权限只能系统应用才可以得到。测试的时候，自定义的接收器无法接受到此广播，后来去掉了这个权限，设为NULL便可以监听到了。

			if (android.os.Build.VERSION.SDK_INT >= 15) {
				Intent meidaButtonIntent = new Intent(
						Intent.ACTION_MEDIA_BUTTON);
				KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP,
						KeyEvent.KEYCODE_HEADSETHOOK);
				meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
				context.sendOrderedBroadcast(meidaButtonIntent, null);
			} else {
				// 以下适用于Android2.3及2.3以上的版本上 ，但测试发现4.1系统上不管用。
				Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
				localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				localIntent1.putExtra("state", 1);
				localIntent1.putExtra("microphone", 1);
				localIntent1.putExtra("name", "Headset");
				context.sendOrderedBroadcast(localIntent1,
						"android.permission.CALL_PRIVILEGED");

				Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
				KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,
						KeyEvent.KEYCODE_HEADSETHOOK);
				localIntent2.putExtra(Intent.EXTRA_KEY_EVENT, localKeyEvent1);
				context.sendOrderedBroadcast(localIntent2,
						"android.permission.CALL_PRIVILEGED");

				Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
				KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,
						KeyEvent.KEYCODE_HEADSETHOOK);
				localIntent3.putExtra(Intent.EXTRA_KEY_EVENT, localKeyEvent2);
				context.sendOrderedBroadcast(localIntent3,
						"android.permission.CALL_PRIVILEGED");

				Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
				localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				localIntent4.putExtra("state", 0);
				localIntent4.putExtra("microphone", 1);
				localIntent4.putExtra("name", "Headset");
				context.sendOrderedBroadcast(localIntent4,
						"android.permission.CALL_PRIVILEGED");
			}

		} else {
			Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
			KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP,
					KeyEvent.KEYCODE_HEADSETHOOK);
			meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
			context.sendOrderedBroadcast(meidaButtonIntent, null);
		}
	}
	
	
	

}