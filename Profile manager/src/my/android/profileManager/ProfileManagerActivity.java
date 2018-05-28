package my.android.profileManager;

import my.android.profileManager.ProfileManagerService.ServiceBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ProfileManagerActivity extends Activity {
	ToggleButton togglebtn;
	TextView text;	
	int serviceOn=0;
	SharedPreferences sharedPref;
	
	ProfileManagerService mService;
    boolean mBound = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sharedPref = getSharedPreferences("toggleState", Context.MODE_PRIVATE);
        togglebtn=(ToggleButton) findViewById(R.id.toggleButton1);
        text=(TextView)findViewById(R.id.textView2);
        togglebtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) 
                {
                    // The toggle is enabled
                	text.setText(R.string.toggleoff);
            		Intent intent=new Intent(ProfileManagerActivity.this,ProfileManagerService.class);
            		startService(intent);
            		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);               	
                	SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("state", true); 
                    editor.commit();
                } 
                else 
                {
                    // The toggle is disabled
                	text.setText(R.string.toggleon);               	
                	unbindService(mConnection);
                    mBound = false;
            		Intent intent=new Intent(ProfileManagerActivity.this,ProfileManagerService.class);
                    stopService(intent);
                	SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("state", false); 
                    editor.commit();
                }
            }
        });
        
        sharedPref = getSharedPreferences("toggleState", Context.MODE_PRIVATE);
        boolean tgState = sharedPref.getBoolean("state", false);  //default is true
        if (tgState == true) 
        {
        	togglebtn.setChecked(true);
        }
        else
        {
        	togglebtn.setChecked(false);
        }
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
    
    private ServiceConnection mConnection = new ServiceConnection() {
    	
        public void onServiceConnected(ComponentName className,IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ServiceBinder binder = (ServiceBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    	
}