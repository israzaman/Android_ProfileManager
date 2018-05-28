package my.android.profileManager;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;

public class ProfileManagerService extends Service implements SensorEventListener {

	SensorManager sMgr;
	Sensor accSensor;
	Sensor proxySensor;
	double valueX;
	double valueY;
	double valueZ;
	double proxyVal;
	boolean accelerometer=false;
	boolean proximity=false;
	private AudioManager ringmode;
	
	private final IBinder mBinder = new ServiceBinder();

	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
        sMgr = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        accSensor = sMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        proxySensor= sMgr.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        
        sMgr.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
		sMgr.registerListener(this, proxySensor, SensorManager.SENSOR_DELAY_FASTEST);
        ringmode = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		return Service.START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		sMgr.unregisterListener(this);
		super.onDestroy();
		
	}
	
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub		
		
			if(event.sensor.getType()==Sensor.TYPE_PROXIMITY)
			{
				proxyVal=event.values[0];
				proximity=true;
			}
			
			if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
			{
				 valueX=event.values[0];
				 valueY=event.values[1];
				 valueZ=event.values[2];
				 accelerometer=true;
				
			}
			
			if((valueY >=9) || ( valueY <= -9)) 
			{ 
				if(proxyVal==0) 
				{
					 if(valueY<0) 
					{ 
						 ringmode.setRingerMode(AudioManager.RINGER_MODE_VIBRATE); 
					} 
					else if(valueY>0) 
					{ 
						ringmode.setRingerMode(AudioManager.RINGER_MODE_NORMAL); 
					} 
				} 
			}
			else 
			{ 
				if(valueZ<0) 
				{ 
					ringmode.setRingerMode(AudioManager.RINGER_MODE_SILENT); 
				} 
				else if(valueZ>0) 
				{ 
					ringmode.setRingerMode(AudioManager.RINGER_MODE_NORMAL); 
				} 
			}
			
	}
	
	 public class ServiceBinder extends Binder {
		 ProfileManagerService getService() {
	            // Return this instance of LocalService so clients can call public methods
	            return ProfileManagerService.this;
	        }
	    }

	    @Override
	    public IBinder onBind(Intent intent) {
	        return mBinder;
	    }


}
