package com.t3hh4xx0r.utils.fileutils;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;



import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.util.Log;

public class Downloads {
	
	public static String DATE = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(new Date());


	private boolean DBG = true;
private String TAG = "Download Utils";

private static File extStorageDirectory = Environment.getExternalStorageDirectory();
    private static final String DOWNLOAD_DIR = extStorageDirectory + "/t3hh4xx0r/downloads/";
    public static final String EXTENDEDCMD = "/cache/recovery/extendedcommand";

public static String PREF_LOCATION;
private static String OUTPUT_NAME = "OutPutTester";
private static String DOWNLOAD_URL;

private int DOWNLOAD_PROGRESS = 0;
private static final int FLASH_ADDON = 0;
private static final int FLASH_COMPLETE = 1;
private static final int INSTALL_ADDON = 2;

private ProgressDialog pbarDialog;


private boolean mAddonIsFlashable;

private boolean isSdCardPresent(){
	return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
}

private boolean isSdCardWriteable(){
	return !Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState());
}


private Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
		switch(msg.what){
		case FLASH_ADDON:
			flashPackage();
			break;
		case FLASH_COMPLETE:
			//pbarDialog.dismiss();
			break;
		case INSTALL_ADDON:
			installPackage();
			break;
		}
		return;
	}
};



public void installPackage() {

            //pbarDialog = new ProgressDialog(Addons.this);
           // pbarDialog.setMessage("Please Wait\n\nInstalling Addon...");
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            //pbarDialog.show();

            Thread cmdThread = new Thread(){
                    @Override
                    public void run() {
                            Looper.prepare();

                            try{Thread.sleep(1000);}catch(InterruptedException e){ }

                            final Runtime run = Runtime.getRuntime();
                            DataOutputStream out = null;
                            Process p = null;

                            try {
                                    p = run.exec("su");
                                    out = new DataOutputStream(p.getOutputStream());
				out.writeBytes("busybox mount -o rw,remount /system\n");
                                    out.writeBytes("busybox cp " + DOWNLOAD_DIR + OUTPUT_NAME + " /system/app/\n");
                                    out.writeBytes("busybox mount -o ro,remount /system\n");
                                    out.flush();
                            } catch (IOException e) {
                                    e.printStackTrace();
                                    return;
                            }

                            handler.sendEmptyMessage(FLASH_COMPLETE);
                    }
            };
            cmdThread.start();
    }

public void flashPackage() {

	//pbarDialog = new ProgressDialog(Addons.this);
	//pbarDialog.setMessage("Please Wait\n\nPreparing Addon for flashing...");
	//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	//pbarDialog.show();

	Thread cmdThread = new Thread(){
		@Override
		public void run() {

                    File updateDirectory = new File ("/cache/recovery/");
                    if (!updateDirectory.isDirectory()) {
                            updateDirectory.mkdir();
                    }
                    
			Looper.prepare();

			try
			{
				Thread.sleep(1000);
			}
			catch(InterruptedException e)
			{ 
				
			}

			final Runtime run = Runtime.getRuntime();
			DataOutputStream out = null;
			Process p = null;

			try {
                                p = run.exec("su");
				out = new DataOutputStream(p.getOutputStream());
				out.writeBytes("busybox echo 'install_zip(\"" + DOWNLOAD_DIR + OUTPUT_NAME +"\");' > " + EXTENDEDCMD + "\n");
                                    out.writeBytes("reboot recovery\n");
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			handler.sendEmptyMessage(FLASH_COMPLETE);		
		}
	};
	cmdThread.start();
}

public void DownloadFile(String url){
	

	File f = new File (DOWNLOAD_DIR + OUTPUT_NAME);
	/*
	  if(f.exists())
	 
	{
		if (mAddonIsFlashable) 
		{
		   // handler.sendEmptyMessage(FLASH_ADDON);
		} else 
		{
			//handler.sendEmptyMessage(INSTALL_ADDON);
		}
	
    } 
	*/
	//else {
    	
    	new DownloadFileAsync().execute(url);
    	
	//}
	
	
}

private class DownloadFileAsync extends AsyncTask<String, String, String> {

	int mPercentage = 0;
	private boolean DBG = true;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if(DBG )log("Pre executing download"); 
	
		//showDialog(DOWNLOAD_PROGRESS);
	}

	@Override
	protected String doInBackground(String... aurl) {

		if(DBG )log("do in background"); 
		int count;

		File downloadDir = new File (DOWNLOAD_DIR);
		if (!downloadDir.isDirectory()) {
			if(DBG )log("Creating download dir" + DOWNLOAD_DIR);
			downloadDir.mkdirs();
			
		}
		
		
		
		try {
			if(DBG )log("Creating connection");
			URL url = new URL(aurl[0]);
			URLConnection conexion = url.openConnection();
			conexion.connect();
			if(DBG )log("Connection complete");

			int lenghtOfFile = conexion.getContentLength();

			InputStream input = new BufferedInputStream(url.openStream());
			OutputStream output = new FileOutputStream(DOWNLOAD_DIR + "/" + OUTPUT_NAME);

			byte data[] = new byte[1024];

			long total = 0;

			while ((count = input.read(data)) != -1) {
				total += count;
				log("" + (int)((total*100)/lenghtOfFile));
				publishProgress(""+(int)((total*100)/lenghtOfFile));
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;

	}

	@Override
	protected void onProgressUpdate(String... progress) {
		mPercentage = Integer.parseInt(progress[0]);
	}

	@Override
	protected void onPostExecute(String unused) {

		if(DBG )log("Post executing download"); 
		//removeDialog(DOWNLOAD_PROGRESS);

		File f = new File (DOWNLOAD_DIR + OUTPUT_NAME);
                    if (f.exists()) {
                            if (mAddonIsFlashable) {
                                    handler.sendEmptyMessage(FLASH_ADDON);
                            } else {
                                    handler.sendEmptyMessage(INSTALL_ADDON);
                            }
		} else {

			if(DBG )log("finish here"); 
			//finish();
		}
	}
}

public void updateApp() {
	
       try {
   		if(DBG  )log("Updating app"); 
            String path ="http://r2doesinc.bitsurge.net/Addons/nightly_version.txt";
            String targetFileName = "available_version.txt";
            boolean eof = false;
            URL u = new URL(path);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(extStorageDirectory + "/t3hh4xx0r/" + targetFileName));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ( (len1 = in.read(buffer)) > 0 ) {
                f.write(buffer,0, len1);
            }
            f.close();
        } catch (IOException e) {
        e.printStackTrace();
        }
}

private void log(String msg) {
    Log.d(TAG, msg);
	}
}