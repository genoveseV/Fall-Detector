package it.sssup.idontfall;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;





import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;



import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import android.os.Vibrator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import android.graphics.Color;
import android.telephony.TelephonyManager;

import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;

import android.view.Menu;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;


public class MainActivity extends Activity implements OnInitListener {
	
	
	
	
	
	/// Prova N3
	
	HttpsTransportSE androidHttpsTransportGetIp;
	HttpsTransportSE androidHttpsTransportAlarm;
	HttpTransportSE androidHttpTransportGetIp;
	
	
    SoapObject requestForAlarm;
    SoapSerializationEnvelope envelopeForAlarm;
    
	
	ConstantsT batteryV = new ConstantsT() ;


		PowerManager mgr; 
		WakeLock wakeLock;
	


	int bLevel_old = 1000;
	//int battCounter = 0;
	String  myBluetoothDevice = "";
	//String screenLock="";
	Thread threadBlue;
	int selDev = -1;
	BluetoothSocket sync_mmSocket;
	private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	String msg_GETNAME = "G" + "\r";
	String msg_RESET   = "R" + "\r";
	String msg_UNLOCK  = "U" + "\r";
	String msg_LOCK    = "L" + "\r";
	String msg_GETDATA = "D" + "\r";
	String msg_PING    = "P" + "\r";
	String msg_TRESH   = "T";
	String msg_PAR     = "A" + "\r";
	
	
	//String msg_CLOSELOG   = "C" + "\r";
	//String msg_TIME    = "T";
	String msg_ADL = "I"+"\r";
	//String msg_LTIME = "P"+"\r";
	EditText textW;
	EditText textAdl;
	EditText textBatt;
	ProgressBar pBar1;
	ProgressBar pBar2;
	ProgressBar pBar3;
	boolean wdGuard=false;
	Chronometer chronometer1;
	int pERC1=40,pERC2;
	
	float lowTreshold,medianTreshold,highTreshold,tresholdMOD,tresholdMODHP,tresholdZ2;
	boolean trainingMode = false;
	boolean scoreView = false;
	boolean menuView = false;
	String msg1;
	String msg2;
	String msg3;
	String msg4;
	
	Timer adl = new Timer();
	Timer wdEthernet  = new Timer();
	Timer wdWimu  = new Timer();
	Timer msgF = new Timer();
	
	Handler hand = new MyIMUHandler();
	MyUtility mUtil = new MyUtility(hand);
	
	Float adlScore = (float) 0.0;
	Float adlScoreNew = (float) 0.0;
	
	FileWriter writer = null;
	//FileWriter writerBatt = null;
	BufferedWriter out = null;
	//BufferedWriter outBatt = null;
	long unixTime ;
	String currentTimeString;
	
	long wdEthernetTimeout = 5000;  // Watch dog period in milliseconds
	long wdWimuTimeout = 5000;  // Watch dog period in milliseconds
	
	
	long adlTimeout = 1000; // ADL index period in milliseconds
	long msgTimeout = 15000;
	long adlScoreTimeout = 100;//30*60; // ADL score time window in seconds
	int nScore = (int) (adlScoreTimeout*1000/adlTimeout);
	int scoreCounter = 0;
	
	String myLang;
	String myHome; 
	
	File fileWIMUINFO ;
	File fileWIMUDEVELOP;
	File fileUSERINFO;
	File fileCCENTER;
	File logFile;
	File setFile;
	File fileAlarmID;
	File dirDataOut;
	
	String nameSpace = "";
	
    String methoNameAlert = "";
    String methoNameGetIP = "";
    String fieldusername = "";
    String username		 = "";
    String fieldpassword = "";
    String passWord  = "";
    
    
 	
    String fieldtimestamp = "";
    String timestamp  = "";
    String fieldAlarmID = "";
    String AlarmID  = "";
    
    String fieldEventType = "";
    String EventType  = "";
    String fieldDeviceID = "";
    String DeviceID  = "";
    String fieldManufacturer = "";
    String Manufacturer  = "";
    String fieldModel = "";
    String Model  = "";
    
    
    String fieldGUI  = "";
    String gui       = "";
    String fieldDevice = "";
    String deviceId  = "";
    String fieldIp   = "";
	String ftpAddress	= "";
 	String ftpUser = "";
 	String ftpPassword	="";
 	String ftpFile = "";
 	String ftpDir ="";
 	PendingIntent intent;
 	private Menu mymenu;
 	String myIp;
	
    private  String URLalarm ="";// "http://80.18.87.214/TeSANAlarmWebService/Alarms.asmx?wsdl";
    private  String URLcheck ="";// "http://80.18.87.214/ChromedWebservices/ipaddress.asmx?wsdl";
    private  String SOAP_ACTION_ALARM ="";// "http://TeSAN.it/InsertNew";
    private  String SOAP_ACTION_GETIP ="";// "http://tesan.it/GetPublicIpAddress";
    private Integer httpsPort;
    //private String reportE;
	
	double fh=0;
	boolean onFTPtrasfer = false;
	
	//boolean alertCondition = false;
	InputStream tmpIn=null;
	TextToSpeech mTts = null;

	
	String msg5,msg6,msg7,msg8,msg9,msg10,msg11,msg12;
	String msg13,msg14,msg15,msg16,msg17,msg18,msg19,msg20,msg21,msg22,msg23;
	
	@Override
	public boolean onContextItemSelected (MenuItem item) {

		
		
		if (item.getItemId() == 1) {
			sendFrameOverBluetooth(sync_mmSocket, msg_RESET);
			mUtil.notifyToast(msg13);
			StateOfDetector.alertCondition = false;
		}
		if (item.getItemId() == 2) {
			
			try {
				if (sync_mmSocket != null){ 
					
					sendFrameOverBluetooth(sync_mmSocket, msg_LOCK);	
					Thread.sleep(200);
					sync_mmSocket.close();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			adl.cancel();
			wdEthernet.cancel();
			wdWimu.cancel();
			try {
				if (out != null)
				out.close();
				
//				if (outBatt != null)
//					outBatt.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			wakeLock.release();
			unixTime= System.currentTimeMillis();
			
			appendLog("Session manual ended: "    + new SimpleDateFormat("MM dd, yyyy hh:mma").format(new java.util.Date(System.currentTimeMillis())));
			finish();
			System.exit(RESULT_OK);	
			
		}
		
		if (item.getItemId() == 3) {
			File directory = new File(myHome + "/Idontfall/WimuOut");

			// Get all files in directory

			File[] files = directory.listFiles();
			for (File file : files)
			{
			   // Delete each file

			   if (!file.delete())
			   {
			       // Failed to delete file

			       System.out.println("Failed to delete "+file);
			   }
			} 
		}
		
		if (item.getItemId() == 4) {
			
			WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
	        
	        int ip = wifiInfo.getIpAddress();
	        
	        String ipString = String.format(
	        		"%d.%d.%d.%d",
	        		(ip & 0xff),
	        		(ip >> 8 & 0xff),
	        		(ip >> 16 & 0xff),
	        		(ip >> 24 & 0xff));
	        
			if (wifiInfo.getSSID() != null)
			{
				mUtil.notifyToast("WLAN: " + wifiInfo.getSSID().toString() + "\nIp:  " + ipString);
	        	mUtil.notifyToast("DEVICE: " + myBluetoothDevice.toString());
			}
			else
			{
	        	mUtil.notifyToast("WLAN off");
	        	mUtil.notifyToast("DEVICE: " + myBluetoothDevice.toString());
			}
			
		}
		
		if (item.getItemId() == 5) {
			trainingMode = true;
			mUtil.notifyMessageText("TRAINING MODE ON", 2);
			mUtil.blinkText("", 2);
		}
		
		if (item.getItemId() == 6) {trainingMode = false;
			mUtil.notifyMessageText("", 2);
			}
		
		if (item.getItemId() == 7) {
			trainingMode = true;
			mUtil.notifyMessageText("TRAINING MODE ON", 2);
			mUtil.blinkText("", 2);
			
				sendFrameOverBluetooth(sync_mmSocket, msg_GETDATA);
				mUtil.notifyToast(msg13);
				StateOfDetector.alertCondition = false;
			}	
		
		
		if (item.getItemId() == 8) {
							
			sendFrameOverBluetooth(sync_mmSocket, msg_PING);
			
			}		
		if (item.getItemId() == 9) {
			   logFile =  new File (myHome + "/Idontfall/IDFlog.txt");
			   if (logFile.exists())
			   {
				   
				   logFile.delete();
			   }
		}
		
		
		if (item.getItemId() == 10) {
			
			
			sendFrameOverBluetooth(sync_mmSocket, msg_PAR);
		
			
						
					
		}
	    
		
		
			
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        FileInputStream fis = null;
        FileOutputStream fos = null;
     
        
        
		try {
			fis = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Idontfall/CCENTERinfoN.txt");
			fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Idontfall/CCENTERinfo.txt");
			EncryptAndDecrypt.encrypt("123456789987654321", fis, fos);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		/*try {
			fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Idontfall/CCENTERinfo.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        

        
        try {
			EncryptAndDecrypt.encrypt("123456789987654321", fis, fos);
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
     */
      
		
       
        
        
          intent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getIntent()), getIntent().getFlags());
            
        mgr = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();
        
        chronometer1 = (Chronometer) findViewById(R.id.chronometer1);
        chronometer1.setTextColor(Color.WHITE);
		chronometer1.start();        
        textW    = (EditText) findViewById(R.id.editText1);
        textAdl  = (EditText) findViewById(R.id.editText2);
        textBatt = (EditText) findViewById(R.id.editText3);
        pBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        pBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        pBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        //textAdl.setVisibility(View.GONE);
        textAdl.setTextColor(Color.WHITE);
        textAdl.setEnabled(false);
        
        textW.setBackgroundColor(Color.BLUE);
        textW.setText("System initialization",TextView.BufferType.EDITABLE);
        textW.setTextColor(Color.YELLOW);
        textW.setTextSize(50);
        textW.setEnabled(false);

        
        textBatt.setTextColor(Color.WHITE);
        textBatt.setEnabled(false);
        
        
        pBar1.setVisibility(ProgressBar.VISIBLE);
        pBar1.setProgress(0);
        pBar1.setMax(100);
        
        
        pBar2.setVisibility(ProgressBar.VISIBLE);
        pBar2.setProgress(0);
        pBar2.setMax(100);
        

    	
        
        
        final Button resetButton = (Button) findViewById(R.id.button1);
        resetButton.setTextColor(Color.WHITE);
        resetButton.setVisibility(View.GONE);
        
        mTts = new TextToSpeech(this,  this);
        
        //isInternetReachable();
               
        
     
        		
        		
        registerForContextMenu(findViewById(R.id.progressBar3));
        
        Thread initThread;
    	
		(initThread = new Thread(
				new Runnable() {
					
					
					public void run() {
						 myHome = Environment.getExternalStorageDirectory().getAbsolutePath();
						
						
						 int status = 0;
						 //int i;
					        
					        if (status == TextToSpeech.SUCCESS) {
					        	
								//@SuppressWarnings("null")
								int result=1;
//								BufferedReader inputSetup = null;
//								fileWIMUDEVELOP = new File (myHome + "/Idontfall/WIMUdevelop.txt");
//								try {
//									inputSetup = new BufferedReader( new FileReader(fileWIMUDEVELOP) );
//									
//									
//									for (i=1;i<=7;++i) myLang = inputSetup.readLine();
//									
//									inputSetup.close();
//								} catch (FileNotFoundException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								} catch (IOException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
								
								
								//if (myLang.compareTo("UK")    == 0)  result = mTts.setLanguage(Locale.UK); 
								//if (myLang.compareTo("ITALY") == 0)  result = mTts.setLanguage(Locale.ITALY); 
								
								
								
//								try {
//									Thread.sleep(1000);
//								} catch (InterruptedException e1) {
//									// TODO Auto-generated catch block
//									e1.printStackTrace();
//								}
								
								
								
					        	if ((result == TextToSpeech.LANG_MISSING_DATA)) {
					        		mUtil.notifyMessageText("TextToSpeech error",1);
						        	mUtil.changeC("Red", 1);
					        		textToS("TextToSpeech error. The application will be restarted!",pERC1);
						        	appendLog("TextToSpeech error: " + new SimpleDateFormat("MM dd, yyyy hh:mma").format(new java.util.Date(System.currentTimeMillis())));
						        	appendLog("Restart");
									restartApp(10000);
						        	return;
					        	} 
					        	} else {
						        	mUtil.notifyMessageText("Could not initialize TextToSpeech.",1);
						        	mUtil.changeC("Red", 1);
					        		textToS("TextToSpeech error! The application will be restarted!",pERC1);
						        	appendLog("TextToSpeech error: " + new SimpleDateFormat("MM dd, yyyy hh:mma").format(new java.util.Date(System.currentTimeMillis())));
						        	appendLog("Restart");
									restartApp(10000);
						        	return;
					        	}
						
						
						
						//Read setup congiguration
				        BufferedReader inputSetup = null;
				        try {

/////////////////////////////////////
				        	// http://developer.android.com/guide/topics/data/data-storage.html
				        	boolean mExternalStorageAvailable;// = false;
				        	boolean mExternalStorageWriteable = false;
				        	String state = Environment.getExternalStorageState();

				        	if (Environment.MEDIA_MOUNTED.equals(state)) {
				        	    // We can read and write the media
				        	    mExternalStorageAvailable = mExternalStorageWriteable = true;
				        	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
				        	    // We can only read the media
				        	    mExternalStorageAvailable = true;
				        	    mExternalStorageWriteable = false;
				        	} else {
				        	    // Something else is wrong. It may be one of many other states, but all we need
				        	    //  to know is we can neither read nor write
				        	    mExternalStorageAvailable = mExternalStorageWriteable = false;
				        	}				        	
/////////////////////////////////////				        	
				        	
				        	if (mExternalStorageWriteable != true) {
				        		mUtil.notifyMessageText("File system error", 1);
				        		mUtil.changeC("Red", 1);
				        		textToS("File system error! The application will be restarted!",30);
								appendLog("File system error: " + new SimpleDateFormat("MM dd, yyyy hh:mma").format(new java.util.Date(System.currentTimeMillis())));
					        	appendLog("Restart");
								restartApp(10000);
				        	}
				        	
				        	
				        	
				        	
				        	 FileInputStream fis1 = null;
				             FileOutputStream fos1 = null;
				             try {
				     			fis1 = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Idontfall/CCENTERinfo.txt");
				     		} catch (FileNotFoundException e1) {
				     			// TODO Auto-generated catch block
				     			e1.printStackTrace();
				     		}
				     		try {
				     			fos1 = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Idontfall/IDFtmp");
				     		} catch (FileNotFoundException e1) {
				     			// TODO Auto-generated catch block
				     			e1.printStackTrace();
				     		}
				             try {
				     			EncryptAndDecrypt.decrypt("123456789987654321", fis1, fos1);
				     		} catch (Throwable e1) {
				     			// TODO Auto-generated catch block
				     			e1.printStackTrace();
				     		}
				            fileCCENTER = new File (myHome + "/Idontfall/IDFtmp");
				            fileWIMUINFO = new File (myHome + "/Idontfall/WIMUinfo.txt");
				        	fileWIMUDEVELOP = new File (myHome + "/Idontfall/WIMUdevelop.txt");
				        	fileUSERINFO = new File (myHome + "/Idontfall/EUSMSGinfo.txt");
				        	//fileCCENTER = new File (myHome + "/Idontfall/CCENTERinfo.txt");
				        	fileAlarmID = new File (myHome + "/Idontfall/AlarmID.txt");
				        	dirDataOut = new File (myHome + "/Idontfall/WimuOut");
				        	
				        	
				        	if ( (dirDataOut.isDirectory() == false) || (fileWIMUINFO.exists() == false) || (fileWIMUDEVELOP.exists() == false) || (fileUSERINFO.exists() == false) || (fileCCENTER.exists() == false)  || (fileAlarmID.exists() == false)){
				        		mUtil.notifyMessageText("Configuration file error!", 1);
				        		mUtil.changeC("Red", 1);
				        		textToS("File system error! The application will be restarted!",30);
				        		appendLog("Configuration file error: "  + new SimpleDateFormat("MM dd, yyyy hh:mma").format(new java.util.Date(System.currentTimeMillis())));
					        	appendLog("Restart");
								restartApp(10000);
				        		return;
				        	}
				        	
				        	
				        	
						    inputSetup = new BufferedReader( new FileReader(fileWIMUINFO) );
							myBluetoothDevice 		= inputSetup.readLine();
							inputSetup.close();
							
							inputSetup = new BufferedReader( new FileReader(fileWIMUDEVELOP) );
							medianTreshold  	= Float.valueOf(inputSetup.readLine());
							scoreView   		= Boolean.valueOf(inputSetup.readLine());
							
							wdEthernetTimeout  	= Integer.valueOf(inputSetup.readLine());
							wdWimuTimeout  		= Integer.valueOf(inputSetup.readLine());
							adlTimeout  		= Integer.valueOf(inputSetup.readLine());
							
							menuView            = Boolean.valueOf(inputSetup.readLine());
							
							tresholdMOD  	= Float.valueOf(inputSetup.readLine());
							tresholdMODHP  	= Float.valueOf(inputSetup.readLine());
							tresholdZ2  	= Float.valueOf(inputSetup.readLine());
							
							nScore = (int) (adlScoreTimeout*1000/adlTimeout);
							
							myLang = inputSetup.readLine();
							inputSetup.close();
							
							inputSetup = new BufferedReader( new FileReader(fileUSERINFO) );
							msg1  		    = inputSetup.readLine();
							msg2  		    = inputSetup.readLine();
							msg3  		    = inputSetup.readLine();
							msg4  		    = inputSetup.readLine();
							pERC1  					= Integer.valueOf(inputSetup.readLine());
							pERC2  					= Integer.valueOf(inputSetup.readLine());
							msg5  		        = inputSetup.readLine();
							msg6  		        = inputSetup.readLine();
							msg7  		        = inputSetup.readLine();
							msg8  		        = inputSetup.readLine();
							msg9  		        = inputSetup.readLine();
							msg10  		        = inputSetup.readLine();
							msg11  		        = inputSetup.readLine();
							msg12  		        = inputSetup.readLine();
							
							msg13  		        = inputSetup.readLine();
							msg14  		        = inputSetup.readLine();
							msg15  		        = inputSetup.readLine();
							msg16  		        = inputSetup.readLine();
							msg17  		        = inputSetup.readLine();
							msg18  		        = inputSetup.readLine();
							msg19  		        = inputSetup.readLine();
							msg20  		        = inputSetup.readLine();
							msg21  		        = inputSetup.readLine();
							msg22  		        = inputSetup.readLine();
							
							msg23  		        = inputSetup.readLine();
							
							
							inputSetup.close();
						} catch (FileNotFoundException e) {
							mUtil.notifyMessageText("Setup file error",1);
				        	mUtil.changeC("Red", 1);
				        	textToS("Setup file error! The application will be restarted!",pERC1);
				        	appendLog("Setup file error: "  + new SimpleDateFormat("MM dd, yyyy hh:mma").format(new java.util.Date(System.currentTimeMillis())));
				        	appendLog("Restart");
							restartApp(10000);	
							return;
						} catch (IOException e) {
							mUtil.notifyMessageText("Setup file error",1);
				        	mUtil.changeC("Red", 1);
				        	textToS("Setup file error! The application will be restarted!",pERC1);
				        	appendLog("Setup file error: "  + new SimpleDateFormat("MM dd, yyyy hh:mma").format(new java.util.Date(System.currentTimeMillis())));
				        	appendLog("Restart");
							restartApp(10000);	
							return;
						}
				         
				        /*
				         http://TeSAN.it/
http://tesan.it/
http://80.18.87.214/TeSANAlarmWebService/Alarms.asmx?wsdl
http://80.18.87.214/ChromedWebservices/ipaddress.asmx?wsdl
http://TeSAN.it/InsertNew
http://tesan.it/GetPublicIpAddress
				         */
				        
				        
				        
				        try {
				        	inputSetup = new BufferedReader( new FileReader(fileCCENTER) );
							   
				         	nameSpace 		    = inputSetup.readLine();
				         	URLalarm 			= inputSetup.readLine();   	
				            URLcheck 			= inputSetup.readLine();
				            //SOAP_ACTION_ALARM 	= inputSetup.readLine();   
				            //SOAP_ACTION_GETIP 	= inputSetup.readLine();
				            httpsPort           = Integer.parseInt(inputSetup.readLine());
				         	methoNameAlert		= inputSetup.readLine();
				         	methoNameGetIP		= inputSetup.readLine();
				         	fieldusername 		= inputSetup.readLine();
				         	username	  		= inputSetup.readLine();
				         	fieldpassword 		= inputSetup.readLine();
				         	passWord			= inputSetup.readLine();
				        	fieldtimestamp 		= inputSetup.readLine();
				        	fieldAlarmID 		= inputSetup.readLine();
				        	fieldEventType 		= inputSetup.readLine();
				        	EventType			= inputSetup.readLine();
				        	fieldDeviceID 		= inputSetup.readLine();
				        	fieldManufacturer   = inputSetup.readLine();
				        	Manufacturer		= inputSetup.readLine();
				        	fieldModel   		= inputSetup.readLine();
				        	Model				= inputSetup.readLine();
				        	
				        	SOAP_ACTION_ALARM 	= nameSpace +  methoNameAlert; //inputSetup.readLine();   
					        SOAP_ACTION_GETIP 	= nameSpace +  methoNameGetIP; // inputSetup.readLine();
				        	
					        
					        String s0 = URLalarm.replaceAll("https://", "");
					        String host = s0.substring(0, s0.indexOf("/"));
					        String file = s0.substring(s0.indexOf("/"),s0.length());
					        androidHttpsTransportAlarm = new HttpsTransportSE(host, httpsPort, file, 50000);
					        
					        
					        s0 = URLcheck.replaceAll("https://", "");
					        host = s0.substring(0, s0.indexOf("/"));
					        file = s0.substring(s0.indexOf("/"),s0.length());
					        //androidHttpsTransportGetIp = new HttpsTransportSE(host, httpsPort, file, (int) (wdTimeout*90/100) );
					        //androidHttpTransportGetIp = new HttpTransportSE(URLcheck);
					        
					       
					        //envelopeForAlarm.dotNet=true;
					        
					        
					        
//				         	<s:element minOccurs="0" maxOccurs="1" name="username" type="s:string"/>
//				         	<s:element minOccurs="0" maxOccurs="1" name="password" type="s:string"/>
//				         	<s:element minOccurs="0" maxOccurs="1" name="timestamp" type="s:string"/>
//				         	<s:element minOccurs="0" maxOccurs="1" name="AlarmID" type="s:string"/>
//				         	<s:element minOccurs="0" maxOccurs="1" name="EventType" type="s:string"/>
//				         	<s:element minOccurs="0" maxOccurs="1" name="DeviceID" type="s:string"/>
//				         	<s:element minOccurs="0" maxOccurs="1" name="Manufacturer" type="s:string"/>
//				         	<s:element minOccurs="0" maxOccurs="1" name="Model" type="s:string"/>				         	
				         	
				         	
				         	
				         	
				         	
				         	
				         	//fieldGUI 			= inputSetup.readLine();
				         	//gui				= inputSetup.readLine();
//							         	fieldDevice			= inputSetup.readLine();
//							         	deviceId			= inputSetup.readLine();
//							         	fieldIp				= inputSetup.readLine();
//							         	fieldGUI 			= inputSetup.readLine();
				         	//ftpAddress		= inputSetup.readLine();
				         	//ftpUser		    = inputSetup.readLine();
				         	//ftpPassword		= inputSetup.readLine();
				         	//ftpFile		    = inputSetup.readLine();
				         	//ftpDir		    = inputSetup.readLine();
				         	inputSetup.close();
				         	
				         	
				         	fileCCENTER.delete();
				         	

				         	inputSetup = new BufferedReader( new FileReader(fileAlarmID) );
				         	AlarmID					= inputSetup.readLine();
				         	inputSetup.close();

				         	
				         	TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				         	deviceId = tm.getDeviceId();
				         	
				         	
				         	
				         	
				         	
				         }
							catch( IOException e ) {
								//Toast.makeText(getApplicationContext(), "Error reading setup filed! The application will be closed", Toast.LENGTH_LONG).show();
								mUtil.notifyMessageText("Setup file error",1);
					        	mUtil.changeC("Red", 1);
					        	textToS("Setup file error! The application will be restarted!",pERC1);
					        	appendLog("Setup file error: "  + new SimpleDateFormat("MM dd, yyyy hh:mma").format(new java.util.Date(System.currentTimeMillis())));
					        	appendLog("Restart");
								restartApp(10000);	
								return;
							} 	
				        // Check for Internet connection
				        ConnectivityManager connectivityManager  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
				  
				        if (activeNetworkInfo == null) {
				        	mUtil.notifyMessageText(msg5,1);
				        	mUtil.changeC("Red", 1);
				        	textToS(msg5 + msg21,pERC1);
				        	appendLog("No network interface at start: "  + new SimpleDateFormat("MM dd, yyyy hh:mma").format(new java.util.Date(System.currentTimeMillis())));
				        	appendLog("Restart");
							restartApp(10000);
				        	return;
				        	
				        } else
				        	//Toast.makeText(getApplicationContext(), "Network interface active!", Toast.LENGTH_LONG).show();
				        mUtil.notifyToast(msg6);
				        
//				        boolean pp = false;
//				        try {
//							pp=  InetAddress.getByName("https://www.google.it/").isReachable(5000);
//						} catch (UnknownHostException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						} catch (IOException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
				        
//				        SntpClient client = new SntpClient();
//				        if (!client.requestTime("time-nw.nist.gov",3000)) {
//				        	
//							mUtil.notifyMessageText(msg7,1);
//					     	mUtil.notifyToast(msg7);
//					     	mUtil.changeC("Red", 1);
//					     	textToS(msg7+"." + msg2,pERC1);
//					     	return;
//				         }
				       
				       if (! webServiceReq(nameSpace,URLcheck,methoNameGetIP)){
				    	    mUtil.notifyMessageText(msg7,1);
				    	    mUtil.changeC("Red", 1);
					     	mUtil.notifyToast(msg7);
					     	textToS(msg7 + msg21,pERC1);
					     	appendLog("No Internet at start: "   + new SimpleDateFormat("MM dd, yyyy hh:mma").format(new java.util.Date(System.currentTimeMillis())));
					     	appendLog("Restart");
							restartApp(10000);
					     	return;        
				       }
				        
				        
				        
//							if (!DoPing("www.google.com"))  {
//     	mUtil.notifyMessageText(msg7,1);
//     	mUtil.notifyToast(msg7);
//     	mUtil.changeC("Red", 1);
//     	textToS(msg2,pERC1);
//     	
//      return;} 
	 // else
     	//Toast.makeText(getApplicationContext(), "Network interface active!", Toast.LENGTH_LONG).show();
      mUtil.notifyToast(msg8);
					
				        
				        // CHECK FOR WIMU-BLUETOOTH STATUS
				        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

				        // Check for Bluetooth adapter
				        if (mBluetoothAdapter == null) {
				        	
				        	mUtil.notifyMessageText(msg17,1);
				        	mUtil.changeC("Red", 1);
				        	textToS(msg17 + ".",pERC1);
				        	appendLog("Bluetooth adapter not found at start: "   + new SimpleDateFormat("MM dd, yyyy hh:mma").format(new java.util.Date(System.currentTimeMillis())));
				        	appendLog("Restart");
							restartApp(10000);	
							return;
				        	
				        		} else
				        			//Toast.makeText(getApplicationContext(), "Bluetooth adapter found!", Toast.LENGTH_LONG).show();
				        			mUtil.notifyToast(msg18);
				        			
				        		// Step 2
				        		if (!mBluetoothAdapter.isEnabled()) {
//				        			Intent enableBtIntent = new Intent(
//				        					BluetoothAdapter.ACTION_REQUEST_ENABLE);
//				        			startActivityForResult(enableBtIntent, 1);
//				        			
				        				
				        				mBluetoothAdapter.enable();
				        				try {
				        					Thread.sleep(2000);
				        				} catch (InterruptedException e) {
				        					// TODO Auto-generated catch block
				        					e.printStackTrace();
				        				}
				        				//Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				        				//startActivityForResult(enableBtIntent, 1);
				        			
				        			
				        		}


				        		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

				        		for (BluetoothDevice device : pairedDevices) {
				        				
				        				if ( device.getName().toString().compareTo(myBluetoothDevice) == 0) {
				        					//Toast.makeText(getApplicationContext(), "Bluetooth device " + myBluetoothDevice + " found!" , Toast.LENGTH_LONG).show();
				        					//mUtil.notifyToast("Bluetooth device " + myBluetoothDevice + " associated!");
				        					threadBlue = new Thread(new ConnectThread(device, mBluetoothAdapter));
				        					threadBlue.start();
				        					break;
				        				}
				        			}        
						
					}
				})).start();
		
              
        
        

        		
        		


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////        		
        		
        		
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////        		
        		resetButton.setOnClickListener(new View.OnClickListener() {

        			public void onClick(View v) {
        				
        				sendFrameOverBluetooth(sync_mmSocket, msg_RESET);
        				mUtil.notifyToast(msg13);
        				StateOfDetector.alertCondition = false;
        				
        				        			}
        		});
  
        		
        		resetButton.setOnTouchListener(new View.OnTouchListener(){
        	        public boolean onTouch(View v, MotionEvent me){
        	            switch(me.getAction()){
        	            case MotionEvent.ACTION_DOWN:
        	            	mUtil.changeB("P", 1);
        	                // TODO: Activate Push To Talk
        	                break;
        	            case MotionEvent.ACTION_UP:
        	            	mUtil.changeB("R", 1);
        	                // TODO: End Push To Talk
        	                break;
        	            }
        	            return false;
        	        }
        	    });
		        
        
        
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {	 
    	
    	
    	if ( keyCode == KeyEvent.KEYCODE_MENU  ){
    		PackageInfo pInfo = null;
    		try {
    			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
    		} catch (NameNotFoundException e2) {
    			// TODO Auto-generated catch block
    			e2.printStackTrace();
    		}
    		String versionName = pInfo.versionName;
    		//String packageName = pInfo.packageName;
    		int versionCode = pInfo.versionCode ;
    		
    		mUtil.notifyToast("WIMU " + versionName + " v" + String.valueOf(versionCode)   );
    	}
    	
		if ( keyCode == KeyEvent.KEYCODE_BACK ) //{// && (screenLock.compareTo("LOCK")==0))
		return true;
		else
//		try {
//			sync_mmSocket.close();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		adl.cancel();
//		wdEthernet.cancel();
//		try {
//			if (out != null)
//			out.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		finish();
//		System.exit(RESULT_OK);
//		}
		return super.onKeyDown(keyCode, event);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			if (sync_mmSocket != null) sync_mmSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		finish();
		
		System.exit(RESULT_OK);
	}

    
    
    
    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private class ConnectThread implements Runnable {

		private final BluetoothDevice mmDevice;
		private BluetoothAdapter mBa;

		public ConnectThread(BluetoothDevice device,
				BluetoothAdapter mBluetoothAdapter) {
			// Use a temporary object that is later assigned to mmSocket,
			mmDevice = device;
			mBa = mBluetoothAdapter;

			// MY_UUID is the app's UUID string, also used by the server code
					try {
						
						sync_mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//Toast.makeText(getApplicationContext(), "Error creating socket" , Toast.LENGTH_LONG).show();
						mUtil.notifyToast(msg19);
						textToS(msg2.toLowerCase(),pERC1);
						appendLog("Error creating socket: "   + new SimpleDateFormat("MM dd, yyyy hh:mma").format(new java.util.Date(System.currentTimeMillis())));
			        	try {
							Thread.sleep(4000);
						} catch (InterruptedException ee) {
							// TODO Auto-generated catch block
							ee.printStackTrace();
						}
						finish();
						System.exit(RESULT_OK);	
						
						return;
					}
				
	

		}

		public void cancel() {
			try {
				if (sync_mmSocket != null) sync_mmSocket.close();
			} catch (IOException e) {
			}
		}

		@SuppressLint("NewApi")
		@Override
		public void run() {
			
			
			int connectCounter = 0;
			// Cancel discovery because it will slow down the connection
			mBa.cancelDiscovery();

			// Connect the device through the socket. This will block
			// until it succeeds or throws an exception
			
			while (sync_mmSocket.isConnected() == false)
				try {
					sync_mmSocket.connect();
				} catch (IOException e4) {
					// TODO Auto-generated catch block
					//e4.printStackTrace();
					if (++connectCounter >= 1) {
						mUtil.notifyMessageText(msg9,1);
						mUtil.changeC("Red", 1);
						textToS(msg9.toLowerCase()+msg2+msg21,pERC1);
						appendLog("WIMU connection error: "   + new SimpleDateFormat("MM dd, yyyy hh:mma").format(new java.util.Date(System.currentTimeMillis())));
						appendLog("Restart");
			        	restartApp(10000);
						return;
					}
					
				}
			
			
//				try {
//					sync_mmSocket.connect();
//					//sendFrameOverBluetooth(sync_mmSocket, msg_RESET);
//					
//					
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					
//					mUtil.notifyMessageText(msg9,1);
//					mUtil.changeC("Red", 1);
//					textToS(msg2,pERC1);
//					try {
//						threadBlue.sleep(6000);
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
////					finish();
////					System.exit(RESULT_OK);
//					return;
//				}

				
				
				
//				Calendar c = Calendar.getInstance(); 				
//				int year = c.get(Calendar.YEAR);
//				int month = c.get(Calendar.MONTH);
//				int day = c.get(Calendar.DAY_OF_MONTH);
//				String sy = new Integer(year).toString();
//				String sm = new Integer(month).toString();
//				String sd = new Integer(day).toString();
				
				unixTime = System.currentTimeMillis(); // 1000L;
				currentTimeString = Long.valueOf(unixTime).toString();
				
////////////////////////////////////////////////////////////////////////////
//				try {
//					writer = new FileWriter(myHome + "/Idontfall/WimuOut/"+ currentTimeString + ".txt", true);
//					//writerBatt  = new FileWriter(myHome + "/Idontfall/WimuOut/BATT"+ currentTimeString + ".txt", true);
//				} catch (IOException e3) {
//					// TODO Auto-generated catch block
//					e3.printStackTrace();
//				}
//				
//				
//				out     = new BufferedWriter(writer);
///////////////////////////////////////////////////////////////////////////				
				//outBatt = new BufferedWriter(writerBatt);
				
				myIp = getIpAddressText(getApplicationContext()).toString();

			
				/*			
				sendFrameOverBluetooth(sync_mmSocket, msg_TIME+currentTimeString+"\r");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				*/


				
				InputStream tmpIn=null;

				byte MsgB[];
				MsgB = new byte[100];
				
				
//				sendFrameOverBluetooth(sync_mmSocket, msg_UNLOCK);
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException e4) {
//					// TODO Auto-generated catch block
//					e4.printStackTrace();
//				}
				sendFrameOverBluetooth(sync_mmSocket, msg_GETNAME);
				
				try {
					tmpIn = sync_mmSocket.getInputStream();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return;
				}
				int i = 0;
				
				
				do 	
				{
				try {
					tmpIn.read(MsgB, i, 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				++i;
				} while (MsgB[i-1] != '\r');
				
				String Msg_str  = new String(MsgB);
				String Msg_strP = new String();
				String Msg_strD = new String();
				
				if (Msg_str.toString().substring(0, i -1).compareTo(myBluetoothDevice ) == 0) {
					mUtil.notifyMessageText(msg10,1);
					textToS(msg10,pERC1);
					textToS(msg3,pERC1);
				}
				
				String treshold = msg_TRESH + " " + String.format(Locale.US," %2.2f",tresholdMOD) + " " + String.format(Locale.US," %2.2f",tresholdMODHP) + " " + String.format(Locale.US," %2.2f",tresholdZ2) +"\r";
				
				sendFrameOverBluetooth(sync_mmSocket, treshold);
				
				
				adl.scheduleAtFixedRate(new adlMonitor(), 10000, adlTimeout);
				wdEthernet.scheduleAtFixedRate(new wdEthernetMonitor(), 10000, wdEthernetTimeout);
				wdWimu.scheduleAtFixedRate(new wdWimuMonitor(), 10000, wdWimuTimeout);
				
				
				
				appendLog("Session started: " + new SimpleDateFormat("MM dd, yyyy hh:mma").format(new java.util.Date(System.currentTimeMillis())));
				
				
				
				
				while (true){
				try {
					
					i=0;
					MsgB = new byte[500];
					do 	
					{
						try {
							tmpIn.read(MsgB, i, 1);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return;
						}
					
					++i;
					} while (MsgB[i-1] != '\r');
					
					Msg_str  = new String(MsgB);
					Msg_strP = new String();
					Msg_strD = new String();
					
					Msg_strP = Msg_str.toString().substring(0, 1);
					if (i>1)
					Msg_strD = Msg_str.toString().substring(1, i -1);
					
					int bLevel;
					int indBatt = 0;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////					
					if (Msg_strP.compareTo("B") == 0 )	
						{
						wdGuard=false;
						bLevel = Integer.valueOf(Msg_strD);
						
						while	(bLevel <batteryV.battThres[indBatt])  {
							indBatt = indBatt+1;
							if (indBatt>=batteryV.battThres.length) break;
						}
						
						bLevel = 100-2*(indBatt-1);
							
						//Pc = Double.valueOf(Msg_strD)*100/4096;

						//bLevel =  (int) (P3*Math.pow(Pc,3) + P2*Math.pow(Pc,2) + P1*Pc + P0);
						
//						if (battCounter == 5) {
//							battCounter = 0;
//						try {
//							outBatt.write(Msg_strD);//+ " "    + currentTimeString );
//							//outBatt.write(Msg_strD);// + " " + Integer.toString(bLevel) + " "    + currentTimeString );
//							outBatt.newLine();
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						}
//						else
//							battCounter = battCounter+1;
							
						
						if (bLevel > bLevel_old) bLevel=bLevel_old;
						bLevel_old = bLevel;
						
						
						
						
						
						if (bLevel > 100) bLevel = 100;
						if (bLevel < 0  ) bLevel = 0;
						if (scoreView == true)
							mUtil.notifyMessageText( Integer.toString(bLevel) + "%" + String.format(Locale.US," %.1f",adlScoreNew) ,3);
						else
							mUtil.notifyMessageText( Integer.toString(bLevel) + "%"  ,3);
						mUtil.changePbar(bLevel, 1);
						}
						
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////					
					if (Msg_strP.compareTo("D") == 0 )
					{
						wdGuard=false;
						
						
						++scoreCounter;
						if ( (scoreCounter == 1) || (scoreCounter == (nScore+1) ) )
						adlScore = adlScore + Float.valueOf(Msg_strD)/(float)2.0; 
						else
						adlScore = adlScore + (float)Float.valueOf(Msg_strD);
						
						if (scoreCounter == nScore+1) {
							adlScoreNew = adlScore/((float)adlScoreTimeout);
							scoreCounter = 1;
							adlScore = Float.valueOf(Msg_strD)/(float)2.0;
							}
						
						//mUtil.notifyMessageText(Msg_strD,2);
						/*
						if  (Float.valueOf(Msg_strD) <= lowTreshold)                                     mUtil.changeT("Green", 2);
						if ((Float.valueOf(Msg_strD) >  lowTreshold) && (Float.valueOf(Msg_strD) <= medianTreshold))  mUtil.changeT("Orange", 2);
						if  (Float.valueOf(Msg_strD) >  medianTreshold)                                  mUtil.changeT("Red", 2);
					    */
						 unixTime = System.currentTimeMillis();// / 1000L;
						 currentTimeString = new Long(unixTime).toString();
						 			
						try {
							
							if (out == null){
								try {
									writer = new FileWriter(myHome + "/Idontfall/WimuOut/"+ currentTimeString + ".txt", true);
									//writerBatt  = new FileWriter(myHome + "/Idontfall/WimuOut/BATT"+ currentTimeString + ".txt", true);
								} catch (IOException e3) {
									// TODO Auto-generated catch block
									e3.printStackTrace();
								}
								out     = new BufferedWriter(writer);
							}
							
							
							out.write(Msg_strD + String.format(Locale.US," %.1f ",adlScoreNew) + currentTimeString );
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						try {
							out.newLine();
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						
						
						
						double adl = (Float.valueOf(Msg_strD)/medianTreshold)*100.0;
						if (adl> 100.0) adl = 100.0;
						mUtil.changePbar((int) adl, 2);
						
					}
						
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////						
					if (Msg_strP.compareTo("F") == 0 ) 
						{
						    mUtil.clockStop("chronometer",1);
							sendFrameOverBluetooth(sync_mmSocket, msg_RESET);
							ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
						    tg.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE);
						    // vibration lasts 300 milliseconds
						    ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(5000);
						    textToS(msg1,pERC2);
						    StateOfDetector.alertCondition = false;
						    
						    unixTime = System.currentTimeMillis();/// 1000L;
							currentTimeString = new Long(unixTime).toString();
							
								
								
								if (out == null){
									try {
										writer = new FileWriter(myHome + "/Idontfall/WimuOut/"+ currentTimeString + ".txt", true);
										//writerBatt  = new FileWriter(myHome + "/Idontfall/WimuOut/BATT"+ currentTimeString + ".txt", true);
									} catch (IOException e3) {
										// TODO Auto-generated catch block
										e3.printStackTrace();
									}
									out     = new BufferedWriter(writer);
								}
							
							
							try {
								out.write("FALL DETECTED " + currentTimeString );
							} catch (IOException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
							try {
								out.newLine();
							} catch (IOException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
						    
							mUtil.unvisiblePbar(1, 1); 
							mUtil.unvisiblePbar(1, 2); 
							//mUtil.unvisiblePbar(1, 3); 
							
							adl.cancel();
							wdEthernet.cancel();
							wdWimu.cancel();
							msgF.scheduleAtFixedRate(new msgSender(), msgTimeout, msgTimeout);
							
		                    if (StateOfDetector.alertCondition == false){ 
		                    	Thread sendAlertThread;
		                    	StateOfDetector.alertCondition=true;
									(sendAlertThread = new Thread(
											new Runnable() {
												
												
												public void run() {
													try {  
														Date df;
														String vv;
																mUtil.notifyToast(msg20);
																mUtil.notifyMessageText(msg11, 1);
																textToS(msg11, pERC2);
																mUtil.changeC("Red", 1);
																
																mUtil.blinkText("", 1);
																
																
									                    		SoapObject request = new SoapObject(nameSpace, methoNameAlert);
									                    		
														      //Pass value for fname variable of the web service
														       // PropertyInfo fnameProp =new PropertyInfo();
														      
														      //Pass value for lname variable of the web service
														        PropertyInfo usernameProp =new PropertyInfo();
														        usernameProp.setName(fieldusername);
														        usernameProp.setValue(username);
														        usernameProp.setType(String.class);
														        
														        PropertyInfo passwProp =new PropertyInfo();
														        passwProp.setName(fieldpassword);
														        passwProp.setValue(passWord);
														        passwProp.setType(String.class);
														        
														        PropertyInfo AlarmIDProp =new PropertyInfo();
														        AlarmIDProp.setName(fieldAlarmID);
														        AlarmIDProp.setValue(AlarmID);
														        AlarmIDProp.setType(String.class);

														        PropertyInfo deviceProp =new PropertyInfo();
														        deviceProp.setName(fieldDeviceID);
														        deviceProp.setValue(deviceId);
														        deviceProp.setType(String.class);
													        
														        PropertyInfo timestampProp =new PropertyInfo();
														        timestampProp.setName(fieldtimestamp);
														        currentTimeString = new Long(unixTime).toString();
																timestampProp.setValue(currentTimeString);
																timestampProp.setType(String.class);
														        
																PropertyInfo EventTypeProp =new PropertyInfo();
																EventTypeProp.setName(fieldEventType);
														        EventTypeProp.setValue(EventType);
														        EventTypeProp.setType(String.class);
														        
														        
														        PropertyInfo ManufacturerProp =new PropertyInfo();
														        ManufacturerProp.setName(fieldManufacturer);
														        ManufacturerProp.setValue(Manufacturer);
														        ManufacturerProp.setType(String.class);
																
														        PropertyInfo ModelProp =new PropertyInfo();
														        ModelProp.setName(fieldModel);
														        ModelProp.setValue(Model);
														        ModelProp.setType(String.class);
																
																
																//ipProp.setValue(myIp);
																
														        
													        
													        request.addProperty(usernameProp);
													        request.addProperty(passwProp);
													        request.addProperty(timestampProp);
													        request.addProperty(AlarmIDProp);
													        request.addProperty(EventTypeProp);
													        request.addProperty(deviceProp);
													        request.addProperty(ManufacturerProp);
													        request.addProperty(ModelProp);
													         
													        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
													        
													        envelope.setOutputSoapObject(request);
													        envelope.dotNet=true;

													        /*String s0 = URLalarm.replaceAll("https://", "");
													        String host = s0.substring(0, s0.indexOf("/"));
													        String file = s0.substring(s0.indexOf("/"),s0.length());
													        
													        //HttpsTransportSE androidHttpsTransport = new HttpsTransportSE("secure.tesanonline.it", httpsPort, "/idf/Alarms.asmx", 50000);
													        
													        androidHttpsTransportAlarm = new HttpsTransportSE(host, httpsPort, file, 50000);*/
													        //HttpTransportSE androidHttpsTransport = new HttpTransportSE(URLalarm);
													        SoapPrimitive response = null;
													        
													        if (trainingMode == false){
														        try {
														        	long timeStart = System.currentTimeMillis();
														        	
														        	androidHttpsTransportAlarm.call(SOAP_ACTION_ALARM, envelope);
														            response = (SoapPrimitive)envelope.getResponse();
														            long dt = (System.currentTimeMillis()-timeStart);
														            
														            if (menuView) mUtil.notifyToast(response.toString() + String.format(Locale.US,": %d (S)", dt/1000));
														            
														            if (response.toString().compareToIgnoreCase("accepted") == 0) {
														            	df = new java.util.Date(unixTime);
																		vv = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);
																		appendLog("Fall Detected: " + vv);
														            	appendLog("Alarm response: " + String.format(Locale.US,"%d mS", dt));
														            	msg1 = msg4;
														            }
														            
														            if (response.toString().compareToIgnoreCase("NotKnown") == 0) {
														            	df = new java.util.Date(unixTime);
																		vv = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);
																		appendLog("Fall Detected: " + vv);
														            	appendLog("Alarm not recognized: " + String.format(Locale.US,"%d mS", dt));
														            	msg1 = msg23;
														            }
														            	
														            
														        } catch (Exception e) {
														        	//reportE = "error";//readStackTrace(e);
														        }//////////////////											
													        }
													        else mUtil.notifyToast(msg22);
													        
													    BufferedReader inputF = null;
									                   
									                    String sendToAddress = "";
									                    String sendFromAddress = "";
									                    String pwd="";
									                    
									                    try {
									    					inputF = new BufferedReader( new FileReader(myHome + "/Idontfall/myCoordinate.txt") );
									    				}
									    				catch( IOException e ) {
									    					//System.err.println("ERROR ERROR");
									    					return;
									    				}   
									                    	sendFromAddress = inputF.readLine();
									    		        	pwd = inputF.readLine();	  
									    		        	inputF.close();
									    		        	
									    		        	
									    		        	GMailSender sender = new GMailSender(sendFromAddress, pwd);
									    		        	
									    				try {
									    					inputF = new BufferedReader( new FileReader(myHome + "/Idontfall/AlarmListAddress.txt") );
									    				}
									    				catch( IOException e ) {
									    					//System.err.println("ERROR ERROR");
									    					return;
									    				}
									    					try {
									    		        	while ((sendToAddress = inputF.readLine()) != null) {
									    		        		  sender.sendMail("Fall alarm!",   
												                            "Help please. I just fell!!",   
												                            sendFromAddress,
												                            sendToAddress);	
									    		        	}
									    				} catch (IOException e5) {
									    					// TODO Auto-generated catch block
									    					e5.printStackTrace();
									    				}
									    					inputF.close();
									    		        	
									    					

									                } catch (Exception e) {   
									                    Log.e("SendMail", e.getMessage(), e);   
									                } 		
												}


											})).start();
									
		                    }
		                    
						    // RECEIVE THE LOGGED DATA
		                    byte MsgLogged[];
							MsgLogged = new byte[1500];
							//do 	
							//{
							i = 0;
							do {
							tmpIn.read(MsgLogged, i, 1);
							++i;
							} while (i < 1500);
							//++i;
							//} while (i != 1500);

							File fileBin = new File(myHome + "/Idontfall/WimuOut/"+ currentTimeString + ".bin");
							FileOutputStream fop = new FileOutputStream(fileBin);
							fop.write(MsgLogged);
							fop.flush();
							fop.close();
							
							//mUtil.changeT("Red", 1);
							
							//mUtil.changeBackL();
							
							
							
						}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////					
					if (Msg_strP.compareTo("M") == 0 ) {
						
						  mUtil.notifyToast("Configuration data were acquired!");
						
						  Msg_str =  Msg_str.replace("; ", ";"+"\n");
						  setFile =  new File (myHome + "/Idontfall/setupLogWimu.txt");
					      BufferedWriter buf = new BufferedWriter(new FileWriter(setFile, false)); 
					      buf.write(Msg_str);
					      buf.close();
						
						
						
						
						
					}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////					
				
					if (Msg_strP.compareTo("P") == 0 ) {
						
						mUtil.notifyToast("Pong");
						
					}					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				
				
				}
				
				//sync_mmSocket.close();
				
		}

	}
	


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
    public void sendFrameOverBluetooth(BluetoothSocket bSock, String Command) {
		byte MsgB[];
		InputStream tmpIn   = null;
        OutputStream tmpOut = null;
        if (bSock == null) return;
    		try {
			tmpIn  = bSock.getInputStream();
			tmpOut = bSock.getOutputStream();
			MsgB = Command.getBytes();
			tmpOut.write(MsgB);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   
    return;
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.activity_main, menu);
        MenuItem it;
        mymenu = menu;
        
        it = mymenu.add(0, 1, 1, "Reset");
        it.setEnabled(menuView);
        it = mymenu.add(0, 2, 0, "Exit");
        it.setEnabled(true);
        it = mymenu.add(0, 3, 3, "Empty directory");
        it.setEnabled(menuView);
        it = mymenu.add(0, 4, 4, "Get network/WIMU info");
        it.setEnabled(true);
        
        
        SubMenu ms1;
        
        ms1 = mymenu.addSubMenu("Training MODE");
        ms1.getItem().setEnabled(menuView);
		it = ms1.add(0, 5, 5, "ON");
		it.setEnabled(menuView);
		it = ms1.add(0, 6, 6, "OFF");
		it.setEnabled(menuView);
        
		
		it = mymenu.add(0, 7, 7, "Fall simulation");
	    it.setEnabled(menuView);
	    
	    it = mymenu.add(0, 8, 8, "Send ping");
	    it.setEnabled(menuView);
	    
	    it = mymenu.add(0, 9, 9, "Log reset");
	    it.setEnabled(menuView);
	    
	    it = mymenu.add(0, 10, 10, "Get config");
	    it.setEnabled(true);
	    
		
        //return true;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    
    
    
    public class MyIMUHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();

			
			if (bundle.containsKey("Toast")) {
				String value = bundle.getString("Toast");
				Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
				return;
			}
			
			
			if (bundle.containsKey("refreshText1")){
				final EditText ee1 = (EditText) findViewById(R.id.editText1);
				String value = bundle.getString("refreshText1");
				ee1.setText(value);
				return;
			}
			if (bundle.containsKey("refreshText2")){
				final EditText ee2 = (EditText) findViewById(R.id.editText2);
				String value = bundle.getString("refreshText2");
				ee2.setText(value);
				return;
			}
			if (bundle.containsKey("refreshText3")){
				final EditText ee2 = (EditText) findViewById(R.id.editText3);
				String value = bundle.getString("refreshText3");
				ee2.setText(value);
				return;
			}
			
			if (bundle.containsKey("blinkText1")){
				final EditText ee2 = (EditText) findViewById(R.id.editText1);
				Animation anim = new AlphaAnimation(0.0f, 1.0f);
				anim.setDuration(1000); //You can manage the time of the blink with this parameter
				anim.setStartOffset(20);
				anim.setRepeatMode(Animation.REVERSE);
				anim.setRepeatCount(Animation.INFINITE);
				ee2.startAnimation(anim);
				
				return;
			}
			if (bundle.containsKey("blinkText2")){
				final EditText ee2 = (EditText) findViewById(R.id.editText2);
				Animation anim = new AlphaAnimation(0.0f, 1.0f);
				anim.setDuration(1000); //You can manage the time of the blink with this parameter
				anim.setStartOffset(20);
				anim.setRepeatMode(Animation.REVERSE);
				anim.setRepeatCount(Animation.INFINITE);
				ee2.startAnimation(anim);
				
				return;
			}
			if (bundle.containsKey("button1")){
				final Button ee1 = (Button) findViewById(R.id.button1);
				String value = bundle.getString("button1");
				if (value.compareTo("P") == 0) {
					ee1.setBackgroundDrawable(getResources().getDrawable(R.drawable.k1) );
				}
				else
					ee1.setBackgroundDrawable(getResources().getDrawable(R.drawable.k4) );
				return;
			}
			
			if (bundle.containsKey("changeTextBack2")){
				final EditText ee2 =  (EditText) findViewById(R.id.editText2);
				String value = bundle.getString("changeTextBack2");
				
				if (value.compareTo("Red")    == 0)  {ee2.setBackgroundColor(Color.RED );return;}
				if (value.compareTo("Green")  == 0)  {ee2.setBackgroundColor(0xFF228B22 );return;}
				if (value.compareTo("Orange") == 0)   ee2.setBackgroundColor(0xFFFF8C00);
				return;
			}
			
			if (bundle.containsKey("changeColorText1")){
				final EditText ee2 =  (EditText) findViewById(R.id.editText1);
				String value = bundle.getString("changeColorText1");
				
				if (value.compareTo("Red")    == 0)  {ee2.setTextColor(Color.RED );return;}
				if (value.compareTo("Green")  == 0)  {ee2.setTextColor(0xFF228B22 );return;}
				if (value.compareTo("Orange") == 0)   ee2.setTextColor(0xFFFF8C00);
				if (value.compareTo("Yellow") == 0)   ee2.setTextColor(Color.YELLOW);
				return;
			}
			
			if (bundle.containsKey("pBar1")){
				final ProgressBar pB = (ProgressBar) findViewById(R.id.progressBar1);
				String value = bundle.getString("pBar1");
				pB.setProgress(Integer.valueOf(value));
				return;
			}
			
			if (bundle.containsKey("pBar2")){
				final ProgressBar pB = (ProgressBar) findViewById(R.id.progressBar2);
				String value = bundle.getString("pBar2");
				pB.setProgress(Integer.valueOf(value));
				return;
			}
			
			if (bundle.containsKey("pBar3")){
				final ProgressBar pB = (ProgressBar) findViewById(R.id.progressBar3);
				String value = bundle.getString("pBar3");
				pB.setProgress(Integer.valueOf(value));
				return;
			}
			
			if (bundle.containsKey("pBarU3")){
				final ProgressBar pB = (ProgressBar) findViewById(R.id.progressBar3);
				String value = bundle.getString("pBarU3");
				if (Integer.valueOf(value) == 0) pB.setVisibility(View.VISIBLE); else pB.setVisibility(View.GONE);
				return;
			}
			
			if (bundle.containsKey("pBarU2")){
				final ProgressBar pB = (ProgressBar) findViewById(R.id.progressBar2);
				String value = bundle.getString("pBarU2");
				if (Integer.valueOf(value) == 0) pB.setVisibility(View.VISIBLE); else pB.setVisibility(View.GONE);
				return;
			}
			
			if (bundle.containsKey("pBarU1")){
				final ProgressBar pB = (ProgressBar) findViewById(R.id.progressBar1);
				String value = bundle.getString("pBarU1");
				if (Integer.valueOf(value) == 0) pB.setVisibility(View.VISIBLE); else pB.setVisibility(View.GONE);
				return;
			}
			
			if (bundle.containsKey("cback")){
				final EditText e0 = (EditText)findViewById(R.id.editText1);
				e0.setBackgroundColor(Color.RED);
				return;
			}
			
			if (bundle.containsKey("chronometer1")){
				final Chronometer e0 = (Chronometer)findViewById(R.id.chronometer1);
				e0.stop();
				return;
				
			}
			
			
		}
	}
    
    private class adlMonitor extends TimerTask {
		public void run() {
			sendFrameOverBluetooth(sync_mmSocket, msg_ADL);
			return;
		}
	}

    
    
    
    private class msgSender extends TimerTask {
		public void run() {
			textToS(msg1,pERC2);
			return;
		}
	}
    
    
    
    private class wdEthernetMonitor extends TimerTask {
		public void run() {
			//Date df;
			//String vv;
			if ((StateOfDetector.alertCondition == true) || (StateOfDetector.escapeCondition == true)) return;
			if (menuView) {
			mUtil.notifyToast("Connectivity test: START");
			ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, pERC2);
		    tg.startTone(ToneGenerator.TONE_CDMA_ANSWER);
			}
		    long timeStart = System.currentTimeMillis();
		    
			 if (! webServiceReq(nameSpace,URLcheck,methoNameGetIP)){
		    	   
				 if ((StateOfDetector.alertCondition == true) || (StateOfDetector.escapeCondition == true)) return;
		    	   mUtil.notifyMessageText(msg7,1);
			       mUtil.notifyToast(msg7);
			       mUtil.changeC("Red", 1);
			       textToS(msg2+msg21,pERC1);
			       try {
			    	   if (out != null)
						out.close();
						
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
			       appendLog("No internet "   + new SimpleDateFormat("MM dd, yyyy hh:mma").format(new java.util.Date(System.currentTimeMillis())));
			       appendLog("Restart");
		           restartApp(10000);
			     	        
		       }
			 else
			 {
				 if ((StateOfDetector.alertCondition == true) || (StateOfDetector.escapeCondition == true)) return;
				 Long dt = (System.currentTimeMillis()-timeStart);
				 appendLog("Cr: " + String.format(Locale.US,"%d mS", dt));
				 if (menuView) mUtil.notifyToast("Connectivity test: " + String.format(Locale.US,"%d (S)", dt/1000));
			 }

			return;
		}
	}
    
    
    
    private class wdWimuMonitor extends TimerTask {
		public void run() {
			Date df;
			String vv;
			if ((StateOfDetector.alertCondition == true) || (StateOfDetector.escapeCondition == true)) return;
			
			if (wdGuard == true) {
				mUtil.clockStop("chronometer",1);
				mUtil.notifyMessageText(msg12,1);
				mUtil.changeC("Red", 1);			
				textToS(msg2+msg21,pERC1);
			
				mUtil.unvisiblePbar(1, 1); 
				mUtil.unvisiblePbar(1, 2); 
				
				adl.cancel();
    	
				try {
					if (out != null)
					out.close();
					//outBatt.close();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				 unixTime = System.currentTimeMillis();/// 1000L;
				 df = new java.util.Date(unixTime);
				 vv = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);
				 appendLog("WIMU off " + vv);
				 appendLog("Restart");
				 restartApp(10000);
				
			}


			else 
				{
				mUtil.unvisiblePbar(0, 3);
				mUtil.notifyMessageText(msg10,1);		
				mUtil.changeC("Yellow", 1);
				} 
			
			
			wdGuard = true;
			return;
		}
	}

	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
//	private class tSS implements Runnable {
//
//		private final String hello ="";
//		private int pe = 1000;
//
//		public tSS(String hello,int pe) {
//			
//			AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
//			int amStreamMusicMaxVol = am.getStreamMaxVolume(am.STREAM_MUSIC);
//			am.setStreamVolume(am.STREAM_MUSIC, amStreamMusicMaxVol*pe/100, 0);
//
//			
//			// Select a random hello.
//			mTts.speak(hello,
//			TextToSpeech.QUEUE_FLUSH, // Drop allpending entries in the playback queue.
//			null);				
//		}
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			
//		}
//		}


    
void textToS(String hello, int pe){

					
					AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
					int amStreamMusicMaxVol = am.getStreamMaxVolume(am.STREAM_MUSIC);
					am.setStreamVolume(am.STREAM_MUSIC, amStreamMusicMaxVol*pe/100, 0);

					
					// Select a random hello.
					mTts.speak(hello,
					TextToSpeech.QUEUE_FLUSH, // Drop allpending entries in the playback queue.
					null);				
					

	
}


/////////////////////////////////////////////

/////////////////////////////////////////////
















String getIpAddressText(Context context) {
    WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    int ipAddress = wifiInfo.getIpAddress();
    String strIPAddess = ((ipAddress >> 0) & 0xFF) + "." + ((ipAddress >> 8) & 0xFF) + "."
            + ((ipAddress >> 16) & 0xFF) + "." + ((ipAddress >> 24) & 0xFF);
    return strIPAddess;
}


void exitProg() {
	try {
		out.close();
		//outBatt.close();
	} catch (IOException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	adl.cancel();
	wdEthernet.cancel();
	
	/*
	sendFrameOverBluetooth(sync_mmSocket, msg_CLOSELOG);
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	*/
	
	try {
		if (sync_mmSocket != null) sync_mmSocket.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	wakeLock.release();
	//return;
	finish();
	System.exit(RESULT_OK);
	
}



boolean  webServiceReq (String nameSpace,String urlString, String method){
	
	
	requestForAlarm = new SoapObject(nameSpace, method);
    envelopeForAlarm = new SoapSerializationEnvelope(SoapEnvelope.VER12);
    envelopeForAlarm.setOutputSoapObject(requestForAlarm);
    envelopeForAlarm.dotNet=true;
    		
	androidHttpTransportGetIp = new HttpTransportSE(urlString);
   
  try {
      androidHttpTransportGetIp.call(SOAP_ACTION_GETIP, envelopeForAlarm);
      SoapObject result = (SoapObject) envelopeForAlarm.bodyIn;
      if (result.getProperty(0).toString() != null) 
    	  return true; 
      			else 
      				return false; 
      
      
  } catch (Exception e) {
  	//reportE = "error";//readStackTrace(e);
  }//////////////////											
return false;

	
}


public void restartApp(Integer dele) {
	
	StateOfDetector.escapeCondition = true;
	adl.cancel();
	wdEthernet.cancel();
	wdWimu.cancel();
	
	
	try {
		Thread.sleep(dele);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
 	mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, intent);
 	System.exit(RESULT_OK);
}

public static boolean DoPing( String ipstr ) {  
    boolean retv=false;  
    try {  
        InputStream ins = Runtime.getRuntime().exec("ping "+ipstr ).getInputStream();  
        Thread.sleep( 3000 );  
        byte[] prsbuf = new byte[ins.available()];  
        ins.read( prsbuf );  
        String parsstr = new StringTokenizer( new String( prsbuf ), "%" ).nextToken().trim();  
        if( !parsstr.endsWith( "100" ) ) retv=true;  
    } catch( Exception e ) {  
        retv=false;  
    }  
    return retv;  
//	 public static boolean  DoPing(String IPtoPing) throws Exception{
//
//     String pingOutput = null;
//	 Process ping = Runtime.getRuntime().exec("ping " + IPtoPing + " -n 3");
//	 BufferedReader in = new BufferedReader(new InputStreamReader(ping.getInputStream()));
//     String line;
//     int lineCount = 0;
//     while ((line = in.readLine()) != null) {
//         //increase line count to find part of command prompt output that we want
//         lineCount++;
//         //when line count is 3 print result
//         if (lineCount == 3){
//             return true;//pingOutput = "Ping to " + IPtoPing + ": " + line + "\n";
//         }
//     }
//
// //return pingOutput;
//     return false;
	
}  



//In condizioni di piena carica la tensione misurata ai capi della batteria è stata 4.39 V.
//La condizione di batteria scarica è stata individuata a 3.09 V.
//Il tempo di piena operatività della WIMU (Fall Detector + Smartphone a display spento) è stato di circa:
//16 ore e 19 minuti.


class ConstantsT {
	 
	final Integer[] battThres = {3739,
		3703,
		3681,
		3661,
		3646,
		3633,
		3620,
		3616,
		3592,
		3558,
		3542,
		3530,
		3522,
		3514,
		3507,
		3499,
		3485,
		3475,
		3466,
		3449,
		3441,
		3427,
		3419,
		3412,
		3403,
		3396,
		3386,
		3380,
		3375,
		3372,
		3366,
		3361,
		3357,
		3350,
		3349,
		3344,
		3343,
		3339,
		3332,
		3325,
		3319,
		3312,
		3303,
		3295,
		3280,
		3275,
		3269,
		3256,
		3191,
		3073,
		2682,
		2626};

	
	}

public void appendLog(String text)
{       
   logFile =  new File (myHome + "/Idontfall/IDFlog.txt");
   if (!logFile.exists())
   {
      try
      {
         logFile.createNewFile();
      } 
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   try
   {
      //BufferedWriter for performance, true to set append to file flag
      BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
      buf.append(text);
      buf.newLine();
      buf.close();
   }
   catch (IOException e)
   {
      // TODO Auto-generated catch block
      e.printStackTrace();
   }
}

public String getStringFromInputStream(InputStream is) {
	 
	BufferedReader br = null;
	StringBuilder sb = new StringBuilder();

	String line;
	try {

		br = new BufferedReader(new InputStreamReader(is));
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	return sb.toString();

}
}
