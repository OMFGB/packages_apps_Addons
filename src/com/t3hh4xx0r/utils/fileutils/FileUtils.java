package com.t3hh4xx0r.utils.fileutils;

import android.app.Activity;  
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Slog;
import android.view.View;
import android.widget.Button;

import java.io.File;

import com.t3hh4xx0r.addons.R;

public class FileUtils extends PreferenceActivity {
        private String TAG = "File Utils";

        public static String DOWNLOAD_DIR;
        public static String OUTPUT_NAME;
        public static String DOWNLOAD_URL;

        protected Context mContext = null;

        public void onCreate(Bundle savedInstanceState) {

	    Slog.d(TAG, DOWNLOAD_DIR + " - downloaddir");
            Slog.d(TAG, OUTPUT_NAME + " - outputname");
            Slog.d(TAG, DOWNLOAD_URL + " - downloadurl");


            addPreferencesFromResource(R.xml.utils);
	    setContentView(R.layout.main);

            File downloadDir = new File (DOWNLOAD_DIR);
    	        if (!downloadDir.isDirectory()) {
                downloadDir.mkdir();
        	}
	}

        public void downloadFile() {  

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
