package com.t3hh4xx0r.utils.fileutils;


import com.t3hh4xx0r.addons.NightliesResolver;
import com.t3hh4xx0r.addons.R;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DownloadService extends IntentService {



	  /** 
	   * A constructor is required, and must call the super IntentService(String)
	   * constructor with a name for the worker thread.
	   */
	  public DownloadService() {
		  
	      super("HelloIntentService");
	  }

	  /**
	   * The IntentService calls this method from the default worker thread with
	   * the intent that started the service. When this method returns, IntentService
	   * stops the service, as appropriate.
	   */
	  @Override
	  protected void onHandleIntent(Intent intent) {
	      // Normally we would do some work here, like download a file.
	      // For our sample, we just sleep for 5 seconds.
		  Log.d("DownloadService","running");
		  
		  
		  
		     
		  Intent i = intent;
		  String url = i.getStringExtra("URL");
		  String zip = i.getStringExtra("ZIP");
		  boolean installable = i.getBooleanExtra("INSTALLABLE", false);
		  

          //displayNotificationMessage("starting download Service");
          DownloadFile d = new DownloadFile(url,zip,this,!installable);
          //displayNotificationMessage("download Service preparing to stop");
          
	    
	      
	  }
	  
	  @Override
      public void onDestroy()
      {
		  //displayNotificationMessage("Downaload service stopping");
		  Log.d("DownloadService","stopping");
          super.onDestroy();
      }


      private void displayNotificationMessage(String message)
      {
    	  String ns = Context.NOTIFICATION_SERVICE;
    	  NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
    	  
    	  int icon = R.drawable.icon;        // icon from resources
    	  CharSequence tickerText = "Hello";              // ticker-text
    	  long when = System.currentTimeMillis();         // notification time
    	  Context context = getApplicationContext();      // application Context
    	  CharSequence contentTitle = "My notification";  // expanded message title
    	  CharSequence contentText = message;      // expanded message text

    	  Intent notificationIntent = new Intent(this, NightliesResolver.class);
    	  PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

    	  // the next two lines initialize the Notification, using the configurations above
    	  Notification notification = new Notification(icon, tickerText, when);
    	  notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
    	  mNotificationManager.notify(1, notification);
          
      }

	  
	}
