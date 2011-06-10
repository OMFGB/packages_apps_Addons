package com.t3hh4xx0r.addons.nightlies;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.t3hh4xx0r.addons.R;

public class Nightlies extends ListActivity {
    /** Called when the activity is first created. */
	
	private ArrayList<NightlyObject> mNightlies = null;
	private NightlyAdapter mAdapter;
	private ProgressDialog mProgressDialog = null; 

    private Runnable viewNightlies;
    private boolean mHasDownloadScript = true;
    private String mScriptURL;
    private String mDownloadPath;
    private String mFileReadPath;
    	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nightlies);
        mNightlies = new ArrayList<NightlyObject>();
        
        this.mAdapter = new NightlyAdapter(this, R.layout.row, mNightlies);
        
        setListAdapter(this.mAdapter);

        Intent i = this.getIntent();
        Bundle extras = i.getBundleExtra("DownloadScript");
        if(extras == null){
        	mHasDownloadScript = false;
        }else{
        	mScriptURL = mDownloadPath + extras.getString("DownloadSCript");
        }
        
        // This is where the script comes in
        // We need to set the script location 
        // sdcard or from a url stream(sdcard is better, probaly should only do it this way)
        // Once that is set then set the file read path.

        //  mFileReadPath;
        viewNightlies = new Runnable(){
            @Override
            public void run() {
                getNightlies();
            }
        };
        
        Thread thread =  new Thread(null, viewNightlies, "MagentoBackground");
        thread.start();
        mProgressDialog = ProgressDialog.show(Nightlies.this,    
                "Please wait...", "Retrieving data ...", true);
    }
    
    private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
            if(mNightlies != null && mNightlies.size() > 0){
            	mAdapter.notifyDataSetChanged();
                for(int i=0;i<mNightlies.size();i++)
                mAdapter.add(mNightlies.get(i));
            }
            mProgressDialog.dismiss();
            mAdapter.notifyDataSetChanged();
        }
      };
    
    private void getNightlies(){
    	
    	try
        {
            mNightlies = new ArrayList<NightlyObject>();
    		
            String x = "";
            InputStream is;
            // Need to actually put our sript locatio here
            if(this.mHasDownloadScript == false)
            	is = this.getResources().openRawResource(R.raw.jsonomfgb);
            else{
            	
            	// Pull it from the sdcard and read it
            	// here to compile
            	// 
            	is = this.getResources().openRawResource(R.raw.jsonomfgb);
            }
                
            byte [] buffer = new byte[is.available()];
            while (is.read(buffer) != -1);
            String jsontext = new String(buffer);
            JSONArray entries = new JSONArray(jsontext);

            x = "JSON parsed.\nThere are [" + entries.length() + "]\n\n";

            int i;
            for (i=0;i<entries.length();i++)
            {
            	NightlyObject n = new NightlyObject();
                JSONObject post = entries.getJSONObject(i);
                
		n.setDate(post.getString("date"));
                n.setBase(post.getString("base"));
                n.setDevice(post.getString("device"));
                n.setURL(post.getString("url"));
                n.setVersion(post.getString("version"));

                mNightlies.add(n);
            }
            Thread.sleep(2000);
        }
        catch (Exception je)
        {

            Log.e("BACKGROUND_PROC", je.getMessage());
             je.printStackTrace();
        }
          runOnUiThread(returnRes);
      }
    
    /*
    public void ResolveNighlies(){
        
        // What i will do here is pass an intent to ourself
        // that will tell it which JSON file to download and use
        // We can optionally add a cache location

	Intent intent = new Intent(Intent.ACTION_MAIN);
	intent.setClassName("com.t3hh4xx0r.addons", "com.t3hh4xx0r.addons.nightlies.Nightlies");
	if ((Build.MODEL.equals("Incredible"))) {
	intent.putExtra("DownloadScript", "inc.js");
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	startActivity(intent);
	
	} else if ((Build.MODEL.equals("Eris"))) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        } else if ((Build.MODEL.equals("Evo"))) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        } else if ((Build.MODEL.equals("Hero"))) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

	}
    }
    */
    
    private class NightlyAdapter extends ArrayAdapter<NightlyObject>{

    	private ArrayList<NightlyObject> mNightly;
    	
    	public NightlyAdapter(Context context, int textViewResourceId, ArrayList<NightlyObject> items) {
    		super(context, textViewResourceId, items);
    		// TODO Auto-generated constructor stub
    		mNightly = items;
    	}
    	
    	@Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);
                }
                NightlyObject o = mNightly.get(position);
                if (o != null) {
                        TextView nightly = (TextView) v.findViewById(R.id.nightly_ver);
                        TextView rom = (TextView) v.findViewById(R.id.rom_ver);
			TextView date = (TextView) v.findViewById(R.id.comp_date);
                        if (nightly != null) {
                              nightly.setText("Nightly: "+o.getVersion());
                        }
                        if (rom != null) {
                              rom.setText("Rom Version: "+ o.getBase());
                        }
			if (date != null) {
			     date.setText("Complied On: "+ o.getDate());
			}
                } 
                return v;
        }
    }
}
