package com.alexmochalov.files;

import android.os.Handler;
import android.view.MotionEvent;

/**
 * The Slider class calculates value (sladeValue) in pixels of text sliding.
 * Sliding starts when ACTION_UP event arose. Value of sliding
 * depends of time gap between ACTION_DOWN and ACTION_AP and 
 * y coordinate gap in this time.
 * Slider calls procedure onSlide every timeDelayed milliseconds
 * and decreases sladeValue while sladeValue not equal to zero. 
 */
public class Slider {
	// Handler to repeat sliding
	private Handler handler = new Handler();
	// Value in pixel of sliding. It is calculated when event ACTION_UP raises and decreases 
	// on every handler call
	// till it will be zero. Then sliding stops.
	private int sladeValue = 0; 
	// Y coordinate. Is used do test if it was moving on vertical 
	private float y = 0;
	// Time for calculating velocity of the moving and set value of the sladeValue.  
	private long eventTime;
	// Interface to call procedure from owner class.
	private OnSlideListener listener;

	private int timeDelayed = 10;
	
	public interface OnSlideListener{
		public void onSlide(int sladeValue);
	}

	public void setCustomEventListener(OnSlideListener onSlideListener) {
		listener = onSlideListener;
	}

	// Slider is cleared on any
	/*
	public void clear(){
		sladeValue = 0;
	}
	*/
	
	/**
	 * Clear sliding value and store y coordinate and time. 
	 * @param event
	 */
	public void init(MotionEvent event){
		sladeValue = 0;
		y = event.getY();
		eventTime = event.getEventTime();
	}

	/**
	 * Calculates sliding value and start sliding 
	 * @param event
	 */
	public void start(MotionEvent event){
		int Y = (int) event.getY();
	    long eventTime1 = event.getEventTime();
	    
	    if (y > Y) sladeValue = -Math.round((y - Y)/(eventTime1-eventTime)*50);
	    else if (y < Y) sladeValue = -Math.round((y - Y)/(eventTime1-eventTime)*50);
	    else y = 0;
	    if (sladeValue !=0){
	    	handler.postDelayed(updateTimeTask, timeDelayed);
	    }	
	}
	
	/**
	 * Decrease slade value and call procedure from the owner 
	 */
	private void slade() {
		if (sladeValue > 0) sladeValue--;
		else if (sladeValue < 0) sladeValue++;
		
		if (listener != null)
			listener.onSlide(sladeValue);
		
	}
	
	/*
	 * Slide and post message to call this procedure again.
	 * If sladeValue is equal to zero, stop sliding. 
	 */
    private Runnable updateTimeTask = new Runnable() { 
		   public void run() { 
			   slade();
			   handler.postDelayed(this, timeDelayed);
		       if (sladeValue ==0)
			   	   handler.removeCallbacks(updateTimeTask); 
		   } 
		};        
}
