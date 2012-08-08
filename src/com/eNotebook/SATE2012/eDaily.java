/* 
 * eDaily.java
 * Activity for creating a new eDaily
 */


package com.eNotebook.SATE2012;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class eDaily extends Activity implements View.OnClickListener{

	// Buttons for saving 
    Button save, back;
    
    // Text views inside the template
    EditText accomplishedtoday, accomplishedtomorrow;
    
    // Contains today's date in the format MM.dd.yyyy
    String datetoday;
    
    // Pop up a toast if there is something wrong
    Toast errormessage;

    // Get bundle of extras
    Bundle extras;
    
    @Override
    /* Called when the activity begins */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sets the xml layout
        setContentView(R.layout.edaily);
        
        // Assigns views and variables
        assignedObjects();
        
        if(extras.getBoolean("loadInitialText"))
        {
        	accomplishedtoday.setText(extras.getString("acctoday"));
        	accomplishedtomorrow.setText(extras.getString("acctomorrow"));
        }
    
        // Set the on click listener
        save.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    
    /* Function that assigns views and variables */
    private void assignedObjects()
    {
    	// Button for saving
        save = (Button) findViewById(R.id.bPreview);
        back = (Button) findViewById(R.id.bDailyBack);
        // Find today's date
        datetoday = getDateToday();
        
        // Views for eDaily template
        accomplishedtoday = (EditText) findViewById(R.id.etToday);
        accomplishedtomorrow = (EditText) findViewById(R.id.etTmrw);
        
        // Get bundle
        extras = getIntent().getExtras();
    }

    /* Event triggered on a button click */
    public void onClick(View view)
    {
    	if(view.getId() == R.id.bDailyBack)
    	{
    		Intent backIntent = new Intent("com.eNotebook.SATE2012." + "EDAILYMENU");
            startActivity(backIntent);
    	}
    	
    	else{
    		
	        // Check if the calendar returned correctly
	        if (datetoday.length() == 0)
	        	return;

            try
            {
                // Find all of the text from the views
                String myacctoday = accomplishedtoday.getText().toString();
                String myacctom = accomplishedtomorrow.getText().toString();

                // Check that none of the fields are empty
                if (myacctoday.length() == 0 || myacctom.length() == 0)
                {
                	errormessage = Toast.makeText(getApplicationContext(),
                				"Please fill in all the blanks or Dr. Williams will hunt you down!!! (with three exclamation marks)", 
                				Toast.LENGTH_LONG);
                	errormessage.show();
                	return;
                }
                
                // Finds the text directory and creates one if none exists
		        File textpath = new File(getFilesDir(), "Text");
		        if (!textpath.exists())
		            textpath.mkdir();
	        	// Create a new file for the new eDaily
	            File newtext = new File(textpath, datetoday);
	            try 
	            { newtext.createNewFile(); }
	            catch(IOException e) 
	            { e.printStackTrace(); } 
                
                // Create the string for going into the file
                String edailytext = getName() + "*****" + myacctoday + "*****" + myacctom;

                // Open the file stream and copy the text into the file
                FileOutputStream ostream = new FileOutputStream(newtext);
                ostream.write(edailytext.getBytes());
                ostream.close();
                
                // Start the preview activity
                Intent previewIntent = new Intent("com.eNotebook.SATE2012." + "EDAILYPREVIEW");
                previewIntent.putExtra("filename", datetoday);
                startActivity(previewIntent);
            }
            catch (Exception e)
            { e.printStackTrace(); }
    	}
    }
    
    /* Retrieves user's name */
    public String getName()
    {
    	File namepath = new File(getFilesDir(), "UserInformation/name");
    	
    	// Error handling for non-existent paths 
    	//  problem with code if this occurs
    	if (!namepath.exists())
    	{
    		errormessage = Toast.makeText(getApplicationContext(),
    				"FATAL: I could not find the path", 
    				Toast.LENGTH_LONG);
    		errormessage.show();
            return null;
    	}
    	
    	// Read the file, parse the string, and set the correct views
    	return readTextfromFile(namepath.toString());
    }
    
    /* Takes in a file path name and returns the text
     *  read in from that file 
     */
    private String readTextfromFile(String path)
    {
    	File edailyfile = new File(path);	// create the file
        FileInputStream instream;			// input stream to read file
        InputStreamReader instreamread; 	// reader for stream
        BufferedReader buf;					// buffer for reader
        
        String lineofdata = "";				// a line of the file
        String data = "";					// will contain complete string
       
        try 
        {
        	// Begin creating the instream buffer
            instream = new FileInputStream(edailyfile);
            instreamread = new InputStreamReader (instream);
            buf = new BufferedReader(instreamread);
            
            try
            {
            	// Read the line until end of file and add to data
                while((lineofdata = buf.readLine()) != null)
                    data += lineofdata + "\n";
            }
            catch(IOException e)
            { e.printStackTrace(); }
            
        }
        catch(FileNotFoundException e)
        { e.printStackTrace(); }
        
        // Return final result
        return data;
    }
    
    
    /* Return today's date in string format MM.dd.yyyy */
    private String getDateToday()
    {
    	// Create the format and calendar instance
    	SimpleDateFormat sdf = new SimpleDateFormat("MMMMMMMMM dd, yyyy");
    	Calendar cal = Calendar.getInstance();
    	
    	// Set the format and return
    	Date today = cal.getTime();
    	return sdf.format(today);
    }
    
    
}

