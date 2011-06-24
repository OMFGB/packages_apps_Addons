package com.t3hh4xx0r.utils.fileutils;

import com.t3hh4xx0r.addons.*;

import android.app.Activity;  
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Slog;
import android.view.View;
import android.widget.Button;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.t3hh4xx0r.addons.R;

public class FileUtils extends PreferenceActivity {
        private String TAG = "File Utils";

        public static String DATE = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(new Date());
        public static final String EXTENDEDCMD = "/cache/recovery/extendedcommand";

        public static String DOWNLOAD_DIR;
        public static String OUTPUT_NAME;
        public static String DOWNLOAD_URL;

        private static final int FLASH_ADDON = 0;
        private static final int INSTALL_ADDON = 1;
        private static final int DOWNLOAD_ADDON = 2;

	protected Context mContext = null;

	CheckBoxPreference mSkipPrefsBox;

        public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);

	    Slog.d(TAG, DOWNLOAD_DIR + " - downloaddir");
            Slog.d(TAG, OUTPUT_NAME + " - outputname");
            Slog.d(TAG, DOWNLOAD_URL + " - downloadurl");


            addPreferencesFromResource(R.xml.utils);
	    setContentView(R.layout.main);

            File downloadDir = new File (DOWNLOAD_DIR);
    	        if (!downloadDir.isDirectory()) {
                downloadDir.mkdir();
        	}

        mSkipPrefsBox = (CheckBoxPreference) findPreference("skip_prefs");
        mSkipPrefsBox.setChecked(Addons.mSkipPrefs);

	}

        public static Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                        switch(msg.what){
                        case FLASH_ADDON:
                                flashFile();
                                break;
                        case INSTALL_ADDON:
                                installFile();
                                break;
                        case DOWNLOAD_ADDON:
				downloadFile();
                        }
                        return;
                }
        };

        public static void flashFile() {

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
                        }
                };
                cmdThread.start();
        }

        public static void installFile() {
        
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(DOWNLOAD_DIR + OUTPUT_NAME)), "application/vnd.android.package-archive");
        startActivity(intent);
        }

        public static void downloadFile(Context context) {  

	DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(DOWNLOAD_URL);  
          
        DownloadManager.Request mRequest =  new  DownloadManager.Request (uri);  
        mRequest.setTitle ("T3hh4xx0r Addons");  
        mRequest.setDescription ("Downloading " + OUTPUT_NAME);  

        File file = new File(DOWNLOAD_DIR + OUTPUT_NAME);  
        mRequest.setDestinationUri(Uri.fromFile(file));

        mRequest.setShowRunningNotification(true);  
        mRequest.setVisibleInDownloadsUi(true);  
	long downloadId = dm.enqueue(mRequest); 
        }
}
