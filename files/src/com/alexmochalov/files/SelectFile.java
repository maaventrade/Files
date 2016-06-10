package com.alexmochalov.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.alex.mochalov.files.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 *    
 * @author Alexey Mochalov (maaventrade@gmail.com)
 * 
 * Activity for selection file from storage
 *
 */
public class SelectFile extends Activity {
	ListView list_files; 

	ArrayList<String> last_files;
	
	ArrayList<String> listItems=new ArrayList<String>(); 
	ArrayList<File> listFiles=new ArrayList<File>(); 
	MyArrayAdapter<String> adapter;
	File dir;
	String fileExt[] = {};
	Activity a;
	
	class FileNameComparator implements Comparator<File> {   
		public int compare(File fileA, File fileB) {
			if ((fileA.isDirectory())&&(!fileB.isDirectory()))
				return -1;
			else if ((!fileA.isDirectory())&&(fileB.isDirectory()))
				return 1;
			else
				return fileA.getName().compareToIgnoreCase(fileB.getName());
			}
	}
	
	private class MyArrayAdapter<String> extends ArrayAdapter{      
		MyArrayAdapter() {      
			super(SelectFile.this, R.layout.raw,R.id.weekofday,listItems);  
		}    
		public View getView(int position, View convertView, ViewGroup parent) {      
			View row = super.getView(position, convertView, parent);      
			ImageView icon = (ImageView) row.findViewById(R.id.icon);
			if (listItems.get(position).equals(".."))
				icon.setImageResource(R.drawable.folder);
			else if (listFiles.get(position).isDirectory())
				icon.setImageResource(R.drawable.folder);
			else
				icon.setImageResource(R.drawable.voidf);
			return (row);   
			} 	
		}	
	
	void readDir(String path){
		listItems.clear();
		listFiles.clear();
        dir = new File(path); 
        File[] files = dir.listFiles();
        if (files != null )
            for (int i=0; i<files.length; i++){
            	boolean addFile = false;
            	if (files[i].isDirectory()) addFile = true;
            	else if (fileExt.length == 0) addFile = true;
            	else for (int j=0; j<fileExt.length; j++)
            		if (files[i].getName().endsWith(fileExt[j])){
            			addFile = true;
            			break;
            		}
            	if (addFile) listFiles.add(files[i]);
        	}	
        FileNameComparator fnc = new FileNameComparator();
        Collections.sort(listFiles, fnc);

        for (int i=0; i<listFiles.size(); i++)
    		listItems.add(listFiles.get(i).getName());
		
		listFiles.add(0,null);  
		listItems.add(0,"..");  

		
        this.setTitle(dir.toString());
        
        adapter = new MyArrayAdapter<String>(); 
        list_files.setAdapter(adapter);
	}
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		//super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        
        super.onCreate(savedInstanceState);
        super.setTheme( android.R.style.Theme_Black);
        
        setContentView(R.layout.selectfile);
    
        ActionBar ab = getActionBar();
        ab.setDisplayShowHomeEnabled(false);
        //ab.setTitle("");
		ab.setHomeButtonEnabled(false);
		
	    TextView textViewCurrentDir;
        textViewCurrentDir = (TextView)findViewById(R.id.textViewCurrentDir);
        textViewCurrentDir.setVisibility(View.INVISIBLE);
		
		// Set color of the ActionBar
		int titleId = getResources().getIdentifier("action_bar_subtitle", "id", "android");
		TextView abTitle = (TextView) findViewById(titleId);
		abTitle.setTextColor(Color.WHITE);
		
        //currentFile
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        a = this;
        
        Intent myIntent = getIntent(); 
        
        // 
        list_files=(ListView)findViewById(R.id.FileList);
        String initPath = myIntent.getStringExtra("initPath"); 
        fileExt = myIntent.getStringArrayExtra("fileExt");
        String message  = myIntent.getStringExtra("message");

        int editName = myIntent.getIntExtra("editName", 0);
        if (editName == 0){
        	LinearLayout linearLayoutNew = (LinearLayout)findViewById(R.id.LinearLayoutNew);
        	linearLayoutNew.setVisibility(View.INVISIBLE);
        }
        
        // Set subtitle of the action bar
        String addInfo = myIntent.getStringExtra("addInfo");
        if ( addInfo != null)
    		ab.setSubtitle(addInfo);
        
		File file = new File(initPath);                                    
		if (file.exists()) readDir(initPath);
		else readDir("/");
        
        list_files.setOnItemClickListener(new OnItemClickListener() { 
            public void onItemClick(AdapterView arg0, View v, int position, long arg3) {
            	String selectedFile = listItems.get(position);
            	String currentdDir = dir.toString();
            	
            	if (selectedFile.equals("..")){
                	int slashposition= currentdDir.lastIndexOf("/");
                	if (slashposition == 0)
                		readDir("/");
                	else if (slashposition > 0)
                        readDir(currentdDir.substring(0,slashposition));
            	}
            	else if (listFiles.get(position).isDirectory()){
                    readDir(listFiles.get(position).getAbsolutePath());
            	}
            	else{
                	Intent intent = new Intent();
                	intent.putExtra("returnedData", listFiles.get(position).getAbsolutePath()); //
                    setResult(RESULT_OK, intent);
                    finish();
            	}
            } 
        }); 

        Button btnSave = (Button)findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
            	Intent intent = new Intent();
            	EditText editTextName = (EditText)findViewById(R.id.editTextName); 
            	intent.putExtra("returnedData", dir.toString()+"/"+editTextName.getText().toString()); //
                setResult(RESULT_OK, intent);
                finish();
			}
        });

        Button btnMail = (Button)findViewById(R.id.buttonMail);
        btnMail.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
            	Intent intent = new Intent();
            	EditText editTextName = (EditText)findViewById(R.id.editTextName); 
            	intent.putExtra("returnedData", "send picture by email"); //
                setResult(RESULT_OK, intent);
                finish();
			}
        });
        
        if (message != null
        && message.length() > 0){
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
            .setTitle(message)
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which) 
                {       
                    dialog.dismiss();           
            }
            });             
        AlertDialog alert = builder.create();
                alert.show();        }
	}
}
