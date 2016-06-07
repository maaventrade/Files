package com.alexmochalov.files;

import com.alex.mochalov.files.R;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.view.View.*;
import android.content.*;
import android.util.*;

/**

**/
public class SaveAudio extends Activity 
implements OnClickListener
{

	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
	}

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m12);
		
		// Gets external intent
		Intent myIntent = getIntent();
		Log.d("my","int "+myIntent);
		// Gets parameters from external intent
		String fileName = myIntent.getStringExtra("fileName");
		/*Log.d("my","file "+fileName);
		Button b = (Button)findViewById(R.id.button_save);
		b.setOnClickListener(this);*/
    }
}
