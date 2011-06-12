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
		  
	      super("AddonsDownloadService");
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
		  
          DownloadFile d = new DownloadFile(url,zip,this,!installable);
          
	    
	      
	  }
	  

	  
	}
