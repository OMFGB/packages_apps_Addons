package com.t3hh4xx0r.utils.fileutils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;


import com.t3hh4xx0r.addons.R;
import com.t3hh4xx0r.addons.nightlies.Nightlies;

public class DownloadFile {
	public int mPercentage;
	public static boolean DBG = true;
	private static String TAG = "DownloadFile";


	private static File extStorageDirectory = Environment.getExternalStorageDirectory();
    private static final String DOWNLOAD_DIR = extStorageDirectory + "/t3hh4xx0r/downloads/";
    private String OUTPUT_NAME;
    NotificationManager mNotificationManager;
    private boolean IsBeingNotified = false;
	private boolean mAddonIsFlashable = true;
	String FULL_PATH_TO_FLASHABLE;
	private static boolean mIsCompleted = false;
	private static boolean mUserWantToflash = false;

	/**
	 * Downloads a file from the specified URL, upon finishing download it will be
	 * called {@link zipName}.  
	 * 
	 * @param url The url that will be downloaded
	 * @param zipName The name of the zip/apk once downloaded
	 * @param flashable To determine if the pacakage should be flashed through recovery or if installation is OK
	 */
	
	public static boolean isCompleted(){
		return mIsCompleted;
	} 
		public DownloadFile(String url, String zipName, boolean flashable){
			OUTPUT_NAME = zipName;
			mAddonIsFlashable = flashable;
			

			
			doInBackground(url);
			
			
			
		    	
			
			
			
		}
		/**
		 * Downloads a file from the specified URL, upon finishing download it will be
		 * called {@link zipName}. Notifications can be passed from this constructor
		 * to make a system notification 
		 * 
		 * @param url The url that will be downloaded
		 * @param zipName The name of the zip/apk once downloaded
		 * @param context A context so that notifications can be passed to the system
		 * @param flashable To determine if the pacakage should be flashed through recovery or if installation is OK
		 */
		public DownloadFile(String url, String zipName, Context context, boolean flashable){
			OUTPUT_NAME = zipName;
			mAddonIsFlashable = flashable;
			

	    	String ns = Context.NOTIFICATION_SERVICE;

	 	   mNotificationManager = (NotificationManager) context.getSystemService(ns);
	 	   
	 	   int icon = R.drawable.icon;        // icon from resources
    	  CharSequence tickerText = "Hello";              // ticker-text
    	  long when = System.currentTimeMillis();         // notification time
    	  Context tcontext = context.getApplicationContext();      // application Context
    	  CharSequence contentTitle = "My notification";  // expanded message title
    	  CharSequence contentText = "Downloading";      // expanded message text

    	  Intent notificationIntent = new Intent(context, Nightlies.class);
    	  PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

    	  // the next two lines initialize the Notification, using the configurations above
    	  Notification notification = new Notification(icon, tickerText, when);
    	  notification.setLatestEventInfo(tcontext, contentTitle, contentText, contentIntent);

    	  File f = new File (DOWNLOAD_DIR + OUTPUT_NAME);

		  
			
    	 
	    	IsBeingNotified = true;
	    	mNotificationManager.notify(1, notification);
			doInBackground(url);
			onPostExecute(context);
	    
			
			
			
		}
		

		/**
		 * This is the worker function of the service. It handles 
		 * all downloads not associated with updating the app.
		 * 
		 * @param aurl The pacakage URL to download
		 * @return
		 */
		private String doInBackground(String... aurl) {

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
					///log("" + (int)((total*100)/lenghtOfFile));
					output.write(data, 0, count);
				}
				if(IsBeingNotified)mNotificationManager.cancel(1);
				
				if(((int)((total*100)/lenghtOfFile)) == 100 ){
					log("The download has finished");
					mIsCompleted  = true;
				}
				
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			return null;

		}
		public static boolean checkFileIsCompleted(String url, String outputname){
	
			
			/*
			 * What needs to be done here is to check that the downloaded file is the same bit stream as the 
			 * URL that is was downloaded from.
			 * If they are the same return true else the files are not the same and return false
			 * 
			 */
			
            if(1 != 1){
            	
            	return false;
            	
            }
            
			
			return true;
			
			
		}

		protected void onPostExecute(Context context) {

			if(DBG )log("Post executing download"); 
			//removeDialog(DOWNLOAD_PROGRESS);
			 int icon = R.drawable.icon;        // icon from resources
	    	  CharSequence tickerText = "Hello";              // ticker-text
	    	  long when = System.currentTimeMillis();         // notification time
	    	  Context tcontext = context.getApplicationContext();      // application Context
	    	  CharSequence contentTitle = "My notification";  // expanded message title
	    	  CharSequence contentText = "Download Finished";      // expanded message text

	    	  Intent notificationIntent = new Intent(context, Nightlies.class);
	    	  PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

	    	  // the next two lines initialize the Notification, using the configurations above
	    	  Notification notification = new Notification(icon, tickerText, when);
	    	  notification.setLatestEventInfo(tcontext, contentTitle, contentText, contentIntent);
	    	  mNotificationManager.notify(2, notification);
		
			} 

	      

		
	
	/**
	 * 
	 * 
	 * @param device The device script to pass
	 * @return The string to the full path to the update script
	 */
	public static String updateAppManifest(String device) {
			
		  String targetFileName = device;
		  String path ="https://raw.github.com/OMFGB/NightlyBuildsManifest/master/" + targetFileName;
    
	      File downloadDir = new File (DOWNLOAD_DIR);
			if (!downloadDir.isDirectory()) {
				if(DBG )Log.d(TAG,"Creating download dir" + DOWNLOAD_DIR);
				downloadDir.mkdirs();
				
			}
			
			
			
			try {

				int count;
				URL url = new URL(path);
				URLConnection conexion = url.openConnection();
				conexion.connect();
				if(DBG )Log.d(TAG,"Connection complete");

				int lenghtOfFile = conexion.getContentLength();

				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(DOWNLOAD_DIR + "/" + targetFileName);

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					Log.d(TAG,"" + (int)((total*100)/lenghtOfFile));
					output.write(data, 0, count);
				}
				
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				
				e.printStackTrace();
			}

	        return (DOWNLOAD_DIR + targetFileName);
	}

	private void log(String msg) {
	    Log.d(TAG , msg);
		}
	
	
	}