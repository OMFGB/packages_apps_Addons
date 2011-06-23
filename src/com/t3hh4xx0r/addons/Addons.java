package com.t3hh4xx0r.addons;

import com.t3hh4xx0r.utils.fileutils.FileUtils;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.content.BroadcastReceiver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.util.DisplayMetrics;

import android.util.Log;
import android.util.Slog;

public class Addons extends PreferenceActivity {
	public static String DATE = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(new Date());

	private boolean DBG = true;
	
	private String TAG = "Addons";
	//Constants for addons, ties to android:key value in addons.xml
        private static final String GOOGLE_APPS = "google_apps_addon";
	private static final String SBC1 = "sbc_1";
	private static final String OMFT = "omft";

        private String DOWNLOAD_DIR;
        public static final String EXTENDEDCMD = "/cache/recovery/extendedcommand";

	public static String PREF_LOCATION;
	private static String OUTPUT_NAME;
	private static String DOWNLOAD_URL;
	private static File extStorageDirectory = Environment.getExternalStorageDirectory();

	private static final int FLASH_ADDON = 0;
	private static final int FLASH_COMPLETE = 1;
	private static final int INSTALL_ADDON = 2;
        private static final int DOWNLOAD_ADDON = 4;
	
        private long enqueue;
        private DownloadManager dm;

	private Preference mGoogleApps;
	private Preference mSBC1;
	private Preference mOMFT;

	private boolean mAddonIsFlashable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                addPreferencesFromResource(R.xml.addons);

                PreferenceScreen prefSet = getPreferenceScreen();

	        mGoogleApps = prefSet.findPreference(GOOGLE_APPS);
		mSBC1 = prefSet.findPreference(SBC1);
	        if ((!Build.MODEL.equals("Incredible"))) {
			PreferenceCategory kernelCategory = (PreferenceCategory) findPreference("kernel_category");
		        kernelCategory.removePreference(mSBC1);
		}

                mOMFT = prefSet.findPreference(OMFT);
		switch (getResources().getDisplayMetrics().densityDpi) {
		case DisplayMetrics.DENSITY_MEDIUM:
                        PreferenceCategory appsCategory = (PreferenceCategory) findPreference("apps_category");
                        appsCategory.removePreference(mOMFT);
		break;
		}

	}
	
	private boolean isSdCardPresent(){
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
	
	private boolean isSdCardWriteable(){
		return !Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState());
	}

	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
     		boolean value;
     		if(this.isSdCardPresent() && this.isSdCardWriteable()){
		        if (preference == mGoogleApps) {
				FileUtils.DOWNLOAD_URL = "http://r2doesinc.bitsurge.net/GAPPS.zip";
				FileUtils.OUTPUT_NAME = "Gapps.zip";
				mAddonIsFlashable = true;
			} else if (preference == mSBC1) {
                                FileUtils.OUTPUT_NAME = "OMFGBk-sbc_1.zip";
                                FileUtils.DOWNLOAD_URL = "http://r2doesinc.bitsurge.net/Addons/OMFGBk-sbc-1.zip";
                                mAddonIsFlashable = true;
                        } else if (preference == mOMFT) {
                                FileUtils.OUTPUT_NAME = "OMFT.apk";
                                FileUtils.DOWNLOAD_URL = "http://r2doesinc.bitsurge.net/Addons/OMFT.apk";
                                mAddonIsFlashable = false;

			}

			if (mAddonIsFlashable) {
                                        FileUtils.DOWNLOAD_DIR = "/sdcard/t3hh4xx0r/downloads/";
			} else {
                                        FileUtils.DOWNLOAD_DIR = "/mnt/sdcard/t3hh4xx0r/downloads/";
			}

			File f = new File (DOWNLOAD_DIR + OUTPUT_NAME);
			if (f.exists()) {
				if (mAddonIsFlashable) {
                	        	handler.sendEmptyMessage(FLASH_ADDON);
				} else {	
					handler.sendEmptyMessage(INSTALL_ADDON);
				}
                	} else {
                               handler.sendEmptyMessage(DOWNLOAD_ADDON);
                	}
     		}
			return true;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case FLASH_ADDON:
				flashPackage();
				break;
			case FLASH_COMPLETE:
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				break;
			case INSTALL_ADDON:
				installPackage();
				break;
                        case DOWNLOAD_ADDON:
                                FileUtils fileUtils = new FileUtils();
				fileUtils.downloadFile(DOWNLOAD_DIR + OUTPUT_FILE);
                                break;
			}
			return;
		}
	};

	public void downloadAddon() {

	File downloadDir = new File (DOWNLOAD_DIR);
	if (!downloadDir.isDirectory()) {
	    downloadDir.mkdir();
	}

        DownloadManager mDownloadManager = (DownloadManager) getSystemService (Context.DOWNLOAD_SERVICE);  

        Uri uri = Uri.parse(DOWNLOAD_URL);  
          
        DownloadManager.Request mRequest =  new  DownloadManager.Request (uri);  
        mRequest.setTitle ("T3hh4xx0r Addons");  
        mRequest.setDescription ("Downloading " + OUTPUT_NAME);  

	File file = new File(DOWNLOAD_DIR + OUTPUT_NAME);  
	mRequest.setDestinationUri(Uri.fromFile(file));

        mRequest.setShowRunningNotification(true);  
        mRequest.setVisibleInDownloadsUi(true);  
          
        long downloadId = mDownloadManager.enqueue(mRequest);
	}

	public void flashPackage() {

		Thread cmdThread = new Thread(){
			@Override
			public void run() {

                        File updateDirectory = new File ("/cache/recovery/");
                        if (!updateDirectory.isDirectory()) {
                                updateDirectory.mkdir();
                        }
				Looper.prepare();

				try{Thread.sleep(1000);}catch(InterruptedException e){ }

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

	public void installPackage() {
	
	Intent intent = new Intent(Intent.ACTION_VIEW);
	intent.setDataAndType(Uri.fromFile(new File(DOWNLOAD_DIR + OUTPUT_NAME)), "application/vnd.android.package-archive");
	startActivity(intent);
	}

	private void log(String msg) {
	    Log.d(TAG, msg);
  	}
}
