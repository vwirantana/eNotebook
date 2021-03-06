/* 
 * eDailyPreview.java
 * Activity that displays the edited eDaily's preview before
 * sending it off via e-mail. Only previews today's eDaily (since
 * previous eDailies can not be edited)
 */


package com.eNotebook.SATE2012;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class eDailyPreview extends Activity implements View.OnClickListener{
	
	// Navigation buttons
	Button edit, menu;
	
	// TextViews to create the eDaily image
	TextView tvdate, tvname, tvacctoday, tvacctom;
	
	// Serves as the filename
	String date;
	
	// Extras from previous page
	Bundle extras;
	
	// User description
	Toast errormessage;
	
	DataPassing dp = new DataPassing();
	
    @Override
    /* Upon creation of the activity (after user saves) */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the xml layout
		setContentView(R.layout.edailypreview);
		
		// Assign the views and variables
		assignedObjects();
		
		// Set on click listeners
		menu.setOnClickListener(this);
		edit.setOnClickListener(this);
		
		// Create the layout
		setLayout();
	}
    
    /* Function that assigns views and variables */
    private void assignedObjects()
    {
    	// Sets the date to today's date
        tvdate = (TextView) findViewById(R.id.tvDate);
        
        // Find the other views on the eDaily image
        tvname = (TextView) findViewById(R.id.tvName);
        tvacctoday = (TextView) findViewById(R.id.tvAccToday);
        tvacctom = (TextView) findViewById(R.id.tvAccTomorrow);
        
        // Bundle of extras
        extras = getIntent().getExtras(); 
        // Get the filename, which is the date
        date = extras.getString("filename");
        tvdate.setText("Today's Date: " + date);
        
        // Set navigation buttons
        menu = (Button) findViewById(R.id.bPreviewMenu);
        edit = (Button) findViewById(R.id.bEdit);
        
    }
    
    /* Sets the layout for the eDaily image */
    private void setLayout()
    {
    	// Find where the current eDaily text file is
    	if(extras == null)
    		return;
    	
    	File textpath = new File(getFilesDir(), "Text/" + date);
    	
    	// Check if the edaily is today's, and hide the edit button if it is not
    	if(!date.equalsIgnoreCase(dp.getDateToday()))
    		edit.getLayoutParams().width = 0;
    	
    	// Error handling for non-existent paths 
    	//  problem with code if this occurs
    	if (!textpath.exists())
    	{
    		errormessage = Toast.makeText(getApplicationContext(),
    				"FATAL: I could not find the path", 
    				Toast.LENGTH_LONG);
    		errormessage.show();
            return;
    	}
    	
    	// Read the file, parse the string, and set the correct views
    	String mystring = dp.readTextfromFile(textpath.toString());
    	parseText(mystring);
    	
    }    
    
    
    /* Parses a given string read from a text file into 
     *  components and sets the correct views
     */
    private void parseText(String textfromfile)
    {
    	// Split the string given the regular expression
    	String[] components = textfromfile.split("\\*\\*\\*\\*\\*");
    	
    	// Make sure that the split has not failed
    	//  problem with code if this happens
    	if (components.length == 0)
    	{
    		errormessage = Toast.makeText(getApplicationContext(),
    				"FATAL: Nothing was inputted.", 
    				Toast.LENGTH_LONG);
    		errormessage.show();
    		return;
    	}
    	// Set the respective views on the eDaily
    	tvname.setText("Student's Name: " + components[0]);
    	tvacctoday.setText(components[1]);
    	tvacctom.setText(components[2]);
	
    }
    
    public void onClick(View view)
    {
    	Intent ourintent;
    	
    	// Check which button was pressed
    	if(view.getId() == R.id.bPreviewMenu)
    		ourintent = new Intent("com.eNotebook.SATE2012." + "EDAILYMENU");
    	else
    	{
    		// Add extra variables into the intent (for editting purposes)
    		ourintent = new Intent("com.eNotebook.SATE2012." + "EDAILY");
    		ourintent.putExtra("loadInitialText", true);
    		ourintent.putExtra("acctoday", tvacctoday.getText());
    		ourintent.putExtra("acctomorrow", tvacctom.getText());
    	}
    	
    	startActivity(ourintent);
    }
    
    
}
