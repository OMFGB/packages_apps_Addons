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
private static String TAG = "Downloads";

private static File extStorageDirectory = Environment.getExternalStorageDirectory();
        private static final String DOWNLOAD_DIR = "/sdcard/t3hh4xx0r/downloads/";
        public static final String EXTENDEDCMD = "/cache/recovery/extendedcommand";

public static String PREF_LOCATION;
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





public static void installPackage(String outputzip) {


		final String OUTPUT_NAME = outputzip;
	    Log.d(TAG,OUTPUT_NAME);
	    
            Thread cmdThread = new Thread(){
                    @Override
                    public void run() {

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

                    }
            };
            cmdThread.start();
    }

public static void flashPackage(String outputzip) {


	final String OUTPUT_NAME = outputzip;
	Log.d(TAG,OUTPUT_NAME);
	
	Thread cmdThread = new Thread(){
		@Override
		public void run() {

                    File updateDirectory = new File ("/cache/recovery/");
                    if (!updateDirectory.isDirectory()) {
                            updateDirectory.mkdir();
                    }
                    

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
	
		}
	};
	cmdThread.start();
}

}
