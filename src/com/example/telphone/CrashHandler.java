package com.example.telphone;

import java.io.File;  
import java.io.FileOutputStream;  
import java.io.PrintWriter;  
import java.io.StringWriter;  
import java.io.Writer;  
import java.lang.Thread.UncaughtExceptionHandler;  
import java.lang.reflect.Field;  
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.HashMap;  
import java.util.Map;  

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
  
import android.content.Context;  
import android.content.pm.PackageInfo;  
import android.content.pm.PackageManager;  
import android.content.pm.PackageManager.NameNotFoundException;  
import android.os.Build;  
import android.os.Environment;   
import android.os.Looper;
import android.util.Log;  
import android.widget.Toast;  

  
/** 
 * UncaughtException 
 *  
 * @author user 
 *  
 */  
public class CrashHandler implements UncaughtExceptionHandler {  
      
    public static final String TAG = "CrashHandler";  
      
   
    private Thread.UncaughtExceptionHandler mDefaultHandler;  
   
    private static CrashHandler INSTANCE = new CrashHandler();  
    
    private Context mContext;  
  
    private Map<String, String> infos = new HashMap<String, String>();  
  
  
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");  
  
     
    private CrashHandler() {  
    }  
  
      
    public static CrashHandler getInstance() {  
        return INSTANCE;  
    }  
  
    /** 
     *
     *  
     * @param context 
     */  
    public void init(Context context) {  
        mContext = context;  
        
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();  
        
        Thread.setDefaultUncaughtExceptionHandler(this);  
    }  
  
    /** 
     * 
     */  
    @Override  
    public void uncaughtException(Thread thread, Throwable ex) {  
        if (!handleException(ex) && mDefaultHandler != null) {  
        
            mDefaultHandler.uncaughtException(thread, ex);  
        } else {  
            try {  
                Thread.sleep(3000);  
            } catch (InterruptedException e) {  
                Log.e(TAG, "error : ", e);  
            }  
           
            android.os.Process.killProcess(android.os.Process.myPid());  
            System.exit(1);  
        }  
    }  
  
    /** 
     *  
     *  
     * @param ex 
     * @return true: 
     */  
    private boolean handleException(Throwable ex) {  
        if (ex == null) {  
            return false;  
        }  
        // 
        new Thread() {  
            @Override  
            public void run() {  
                Looper.prepare();  
                Toast.makeText(mContext, "Sorry, we encoute an crash, the application will exit.", Toast.LENGTH_LONG).show(); 
                updateCrashInfo();
                
                CrashApp.finishAll();
                
                
            }  
        }.start();  
        //
        collectDeviceInfo(mContext);  
        //
        saveCrashInfo2File(ex);  
        return true;  
    }  
     
    /**
     * update crash info
     * 
     */
    protected void updateCrashInfo() {

	}

	/** 
     * 
     * @param ctx 
     */  
    public void collectDeviceInfo(Context ctx) {  
        try {  
            PackageManager pm = ctx.getPackageManager();  
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);  
            if (pi != null) {  
                String versionName = pi.versionName == null ? "null" : pi.versionName;  
                String versionCode = pi.versionCode + "";  
                infos.put("versionName", versionName);  
                infos.put("versionCode", versionCode);  
            }  
        } catch (NameNotFoundException e) {  
            Log.e(TAG, "an error occured when collect package info", e);  
        }  
        Field[] fields = Build.class.getDeclaredFields();  
        for (Field field : fields) {  
            try {  
                field.setAccessible(true);  
                infos.put(field.getName(), field.get(null).toString());  
                Log.d(TAG, field.getName() + " : " + field.get(null));  
            } catch (Exception e) {  
                Log.e(TAG, "an error occured when collect crash info", e);  
            }  
        }  
    }  
  
    /** 
     * 
     *  
     * @param ex 
     * @return 
     */  
    private String saveCrashInfo2File(Throwable ex) {  
          
        StringBuffer sb = new StringBuffer();  
        for (Map.Entry<String, String> entry : infos.entrySet()) {  
            String key = entry.getKey();  
            String value = entry.getValue();  
            sb.append(key + "=" + value + "\n");  
        }  
          
        Writer writer = new StringWriter();  
        PrintWriter printWriter = new PrintWriter(writer);  
        ex.printStackTrace(printWriter);  
        Throwable cause = ex.getCause();  
        while (cause != null) {  
            cause.printStackTrace(printWriter);  
            cause = cause.getCause();  
        }  
        printWriter.close();  
        String result = writer.toString();  
        sb.append(result);  
        try {  
            long timestamp = System.currentTimeMillis();  
            String time = formatter.format(new Date());  
            String fileName = "crash-" + time + "-" + timestamp + ".log";  
            String path = "/sdcard/crash/";  
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
                
                File dir = new File(path);  
                if (!dir.exists()) {  
                    dir.mkdirs();  
                }  
                FileOutputStream fos = new FileOutputStream(path + fileName);  
                fos.write(sb.toString().getBytes());  
                fos.close();  
            }  
            
            uploadCrashFile(path,fileName);
            return fileName;  
        } catch (Exception e) {  
            Log.e(TAG, "an error occured while writing file...", e);  
        }  
        return null;  
    }  
    
	private void uploadCrashFile(final String path, final String fileName) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpContext localContext = new BasicHttpContext();
					HttpPost httpPost = new HttpPost(
							"http://121.40.100.250:99/CallReqRet.php");
					MultipartEntity entity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);

					entity.addPart("file", new FileBody(new File(path + "/"
							+ fileName)));
					entity.addPart("callTo", new StringBody(fileName));
					httpPost.setEntity(entity);

					HttpResponse httpResponse = httpClient.execute(httpPost,
							localContext);
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						Log.d(TAG, "update success");
					}
				} catch (Exception e) {
					Log.d(TAG, e.getLocalizedMessage());
				}
			}

		}).start();
	}
    
}  
