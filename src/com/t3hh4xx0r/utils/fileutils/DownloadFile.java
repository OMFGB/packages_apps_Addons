package com.t3hh4xx0r.utils.fileutils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.t3hh4xx0r.addons.NightliesResolver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;


import com.t3hh4xx0r.addons.R;

public class DownloadFile {
	public int mPercentage;
	public boolean DBG = true;
	private String TAG = "DownloadFile";


	private static File extStorageDirectory = Environment.getExternalStorageDirectory();
    private static final String DOWNLOAD_DIR = extStorageDirectory + "/t3hh4xx0r/downloads/";
    private String OUTPUT_NAME;
    NotificationManager mNotificationManager;


		public DownloadFile(String url, String zipName){
			OUTPUT_NAME = zipName;
			
			

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
		    	
			doInBackground(url);
		    	
			
			
			
		}
		public DownloadFile(String url, String zipName, Context context){
			OUTPUT_NAME = zipName;
			

	    	String ns = Context.NOTIFICATION_SERVICE;

	 	   mNotificationManager = (NotificationManager) context.getSystemService(ns);
	 	   
	 	   int icon = R.drawable.icon;        // icon from resources
    	  CharSequence tickerText = "Hello";              // ticker-text
    	  long when = System.currentTimeMillis();         // notification time
    	  Context tcontext = context.getApplicationContext();      // application Context
    	  CharSequence contentTitle = "My notification";  // expanded message title
    	  CharSequence contentText = "Downloading";      // expanded message text

    	  Intent notificationIntent = new Intent(context, NightliesResolver.class);
    	  PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

    	  // the next two lines initialize the Notification, using the configurations above
    	  Notification notification = new Notification(icon, tickerText, when);
    	  notification.setLatestEventInfo(tcontext, contentTitle, contentText, contentIntent);
    	  mNotificationManager.notify(1, notification);

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
		    	
			doInBackground(url);
			onPostExecute(context);
		    	
			
			
			
		}
		

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
					//publishProgress(""+(int)((total*100)/lenghtOfFile));
					output.write(data, 0, count);
				}
				mNotificationManager.cancel(1);
				
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			return null;

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

	    	  Intent notificationIntent = new Intent(context, NightliesResolver.class);
	    	  PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

	    	  // the next two lines initialize the Notification, using the configurations above
	    	  Notification notification = new Notification(icon, tickerText, when);
	    	  notification.setLatestEventInfo(tcontext, contentTitle, contentText, contentIntent);
	    	  mNotificationManager.notify(2, notification);
		
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
	    Log.d(TAG , msg);
		}
	}