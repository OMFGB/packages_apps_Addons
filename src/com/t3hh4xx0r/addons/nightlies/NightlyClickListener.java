package com.t3hh4xx0r.addons.nightlies;

import android.view.View;
import android.view.View.OnClickListener;


public class NightlyClickListener implements OnClickListener {
	
    private int position;
    private onNightlyClickListener mOnNightlyClickListener ;
 
        // Pass in the callback (this'll be the activity) and the row position
    public NightlyClickListener(View v, int pos) {
        position = pos;
    }
 
        // The onClick method which has NO position information
    @Override
    public void onClick(View v) {
    	
    	mOnNightlyClickListener.OnNightlyClick(v, position);
    }
    
    public interface onNightlyClickListener {
    	
    	
    	public void OnNightlyClick(View v, int position);
        // Feel free to add other methods of use. OnCustomTouch for example :)
    }		

    
    public void setOnNightlyClickListener(onNightlyClickListener l) {
    	 //if (DBG) log("Setting the listners");
    	this.mOnNightlyClickListener = l;
    }


}
