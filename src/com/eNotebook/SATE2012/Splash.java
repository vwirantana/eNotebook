/* Splash.java
 * 
 * Not needed, but left here in case Dr. Williams
 *  would like logo on the Splash screen
 */

package com.eNotebook.SATE2012;

import android.app.Activity;
import android.content.Intent;
//import android.media.MediaPlayer;
import android.os.Bundle;

public class Splash extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		// Create a new thread to run (occurs for loading) 
		Thread timer = new Thread(){
			
			public void run(){
				try{
					sleep(1000);
				} 
				catch (InterruptedException e){	
					e.printStackTrace();
				} 
				finally{
					Intent openMain = new Intent("com.eNotebook.SATE2012.MENU");
					startActivity(openMain);
				}
				
			}
			
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//ourSong.release();
		finish();
	}

	
	
}