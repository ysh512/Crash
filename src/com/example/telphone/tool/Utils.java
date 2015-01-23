package com.example.telphone.tool;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.example.telphone.property.ContractInfo;
import com.example.telphone.property.SingleRecord;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.util.Log;

public class Utils {

	public static List<SingleRecord> getContacts(Context context)
	{
		
	List<SingleRecord> recordsList = new ArrayList<SingleRecord>();
	Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,                            
	        null, null, null, Calls.DEFAULT_SORT_ORDER);                                                                                                 
	if(cursor.moveToFirst()){                                                                                
	    do{                                                                                                  
//	        CallsLog calls =new CallsLog();                                                                  
	        //号码                                                                                     
	    	
	    	SingleRecord ci = new SingleRecord();
	    	
	        String number = cursor.getString(cursor.getColumnIndex(Calls.NUMBER));                           
	        //呼叫类型                                                                                           
	        int type;                                                                                     
	        switch (Integer.parseInt(cursor.getString(cursor.getColumnIndex(Calls.TYPE)))) {                 
	        case Calls.INCOMING_TYPE:                                                                        
	            type = Calls.INCOMING_TYPE;                                                                             
	            break;                                                                                       
	        case Calls.OUTGOING_TYPE:                                                                        
	            type = Calls.OUTGOING_TYPE;                                                                                 
	            break;                                                                                       
	        case Calls.MISSED_TYPE:                                                                          
	            type = Calls.MISSED_TYPE;                                                                                 
	            break;                                                                                       
	        default:                                                                                         
	            type = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Calls.TYPE))) ;//应该是挂断.根据我手机类型判断出的                                                              
	            break;                                                                                       
	        }                                                                                                
	        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");                              
	        Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(Calls.DATE))));
	        //呼叫时间                                                                                           
	        String time = sfd.format(date);                                                                  
	        //联系人                                                                                            
	        String name = cursor.getString(cursor.getColumnIndexOrThrow(Calls.CACHED_NAME));                 
	        //通话时间,单位:s                                                                                      
//	        String duration = cursor.getString(cursor.getColumnIndexOrThrow(Calls.DURATION));                
	        
	        ci.name = name;
	        ci.number = number;
	        ci.type = type;
	        ci.time = time;
	        recordsList.add(ci);
	        
//	        Log.d("Phone",name+":"+number);
	        
//	        String number = cursor.getString(cursor.getColumnIndexOrThrow(Calls.NUMBER));
	    }while(cursor.moveToNext());                                                                         
	          
	    
		}
	return recordsList;
	}
	
	
	//取本机通讯录
	public static HashMap<String, ContractInfo> getPhoneContracts(
			Context mContext) {
		HashMap<String, ContractInfo> map = new HashMap<String, ContractInfo>();
		ContentResolver resolver = mContext.getContentResolver();
		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, null, null,
				null, null); // 传入正确的uri
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				int nameIndex = phoneCursor.getColumnIndex(Phone.DISPLAY_NAME); // 获取联系人name
				String name = phoneCursor.getString(nameIndex);

				String phoneNumber = phoneCursor.getString(phoneCursor
						.getColumnIndex(Phone.NUMBER)); // 获取联系人number
				if (TextUtils.isEmpty(phoneNumber)) {
					continue;
				}
				// 以下是我自己的数据封装。
				
//				if(map.containsKey(name))
//				{
//					ContractInfo c = map.get(name);
//					c.phoneList.add(phoneNumber);
//				}
//				else
//				{
//					ContractInfo contractInfo = new ContractInfo();
//					contractInfo.name = name;
//				// contractInfo.setName(name);
//					contractInfo.phoneList = new ArrayList();
//					contractInfo.phoneList.add(phoneNumber);
////				contractInfo.phoneNum = phoneNumber;
//				// contractInfo.setPhoneNumber(getNumber(phoneNumber));
//				// contractInfo.setFrom(PHONE);
//					contractInfo.source = 0;
//					map.put(name, contractInfo);
//				}
			}
			phoneCursor.close();
		}
		return map;
	}

	// 接下来看获取sim卡的方法，sim卡的uri有两种可能content://icc/adn与content://sim/adn
	// （一般情况下是第一种）
	public static HashMap<String, ContractInfo> getSimContracts(Context mContext) {
		// 读取SIM卡手机号,有两种可能:content://icc/adn与content://sim/adn
		HashMap<String, ContractInfo> map = new HashMap<String, ContractInfo>();

		ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://icc/adn");
		Cursor phoneCursor = resolver.query(uri, null, null, null, null);
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				String name = phoneCursor.getString(phoneCursor
						.getColumnIndex("name"));
				String phoneNumber = phoneCursor.getString(phoneCursor
						.getColumnIndex("number"));
				if (TextUtils.isEmpty(phoneNumber)) {
					continue;
				}
				// 以下是我自己的数据封装。
			}
			phoneCursor.close();
		}
		return map;
	}
	
	
	public static HashMap<String, ContractInfo> getAllContracts(Context mContext,List<String> nameList) {

		// 读取SIM卡手机号,有两种可能:content://icc/adn与content://sim/adn
		HashMap<String, ContractInfo> map = new HashMap<String, ContractInfo>();
		ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse("content://icc/adn");
		try {
			Cursor phoneCursor = resolver.query(uri, null, null, null, null);
			if (phoneCursor != null) {
				while (phoneCursor.moveToNext()) {
					String name = phoneCursor.getString(phoneCursor
							.getColumnIndex("name"));
					String phoneNumber = phoneCursor.getString(phoneCursor
							.getColumnIndex("number"));
					if (TextUtils.isEmpty(phoneNumber)) {
						continue;
					}
					// 以下是我自己的数据封装。

					while (map.containsKey(name)) {
						name = name + " ";
					}
					ContractInfo contractInfo = new ContractInfo();
					contractInfo.name = name;
					contractInfo.phone = phoneNumber;
					contractInfo.source = 0;
					map.put(name, contractInfo);
					nameList.add(name);

				}
				phoneCursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ContentResolver resolver2 = mContext.getContentResolver();
		// 获取手机联系人
		Cursor phoneCursor2 = resolver.query(Phone.CONTENT_URI, null, null,
				null, null); // 传入正确的uri
		if (phoneCursor2 != null) {
			while (phoneCursor2.moveToNext()) {
				int nameIndex = phoneCursor2.getColumnIndex(Phone.DISPLAY_NAME); // 获取联系人name
				String name = phoneCursor2.getString(nameIndex);
				
				int phoneIndex = phoneCursor2.getColumnIndex(Phone.NUMBER);
				
				String phoneNumber = phoneCursor2.getString(phoneIndex);
//				String phoneNumber = phoneCursor2.getString(phoneCursor
//						.getColumnIndex(Phone.NUMBER)); // 获取联系人number
				if (TextUtils.isEmpty(phoneNumber)) {
					continue;
				}

				while(map.containsKey(name))
				{
					name = name+" ";
				}
				ContractInfo contractInfo = new ContractInfo();
				contractInfo.name=name;
				contractInfo.phone = phoneNumber;
				contractInfo.source = 0;
				map.put(name, contractInfo);
				nameList.add(name);
//				if (map.containsKey(name)) {
//					ContractInfo c = map.get(name);
//					c.phoneList.add(phoneNumber);
//				} else {
//					ContractInfo contractInfo = new ContractInfo();
//					contractInfo.name = name;
//					contractInfo.phoneList = new ArrayList();
//					contractInfo.phoneList.add(phoneNumber);
//					contractInfo.source = 0;
//					map.put(name, contractInfo);
//					nameList.add(name);
//				}
			}
			phoneCursor2.close();
		}
		return map;
	}	
	
	public static byte[] decodeBitmap(String path) {  
        BitmapFactory.Options opts = new BitmapFactory.Options();  
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高  
        BitmapFactory.decodeFile(path, opts);  
        opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);  
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true  
        opts.inPurgeable = true;  
        opts.inInputShareable = true;  
        opts.inDither = false;  
        opts.inPurgeable = true;  
        opts.inTempStorage = new byte[16 * 1024];  
        FileInputStream is = null;  
        Bitmap bmp = null;  
        ByteArrayOutputStream baos = null;  
        try {  
            is = new FileInputStream(path);  
            bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);  
            double scale = getScaling(opts.outWidth * opts.outHeight,  
                    1024 * 600);  
            Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,  
                    (int) (opts.outWidth * scale),  
                    (int) (opts.outHeight * scale), true);  
            bmp.recycle();  
            baos = new ByteArrayOutputStream();  
            bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
            bmp2.recycle();  
            return baos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                is.close();  
                baos.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            System.gc();  
        }  
        return baos.toByteArray();  
    }  
  
    private static double getScaling(int src, int des) {  
        /** 
         * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49 
         */  
        double scale = Math.sqrt((double) des / (double) src);  
        return scale;  
    }  
  
    public static int computeSampleSize(BitmapFactory.Options options,  
            int minSideLength, int maxNumOfPixels) {  
        int initialSize = computeInitialSampleSize(options, minSideLength,  
                maxNumOfPixels);  
  
        int roundedSize;  
        if (initialSize <= 8) {  
            roundedSize = 1;  
            while (roundedSize < initialSize) {  
                roundedSize <<= 1;  
            }  
        } else {  
            roundedSize = (initialSize + 7) / 8 * 8;  
        }  
  
        return roundedSize;  
    }  
  
    private static int computeInitialSampleSize(BitmapFactory.Options options,  
            int minSideLength, int maxNumOfPixels) {  
        double w = options.outWidth;  
        double h = options.outHeight;  
  
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math  
                .sqrt(w * h / maxNumOfPixels));  
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(  
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));  
  
        if (upperBound < lowerBound) {  
            return lowerBound;  
        }  
  
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {  
            return 1;  
        } else if (minSideLength == -1) {  
            return lowerBound;  
        } else {  
            return upperBound;  
        }  
    }  
	
}
