package it.sssup.idontfall;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MyUtility {
	private Handler mhandler;

	public MyUtility(Handler handler){
		super();
		this.mhandler = handler;
		}

public void notifyToast(String str) {
		
        String Refr;
	    Refr = "Toast";
    	
    	Message msg = mhandler.obtainMessage();
    	
    	
    	
        Bundle b = new Bundle();
        
        b.putString(Refr, ""+str);
        msg.setData(b);
        mhandler.sendMessage(msg);
      }	
	

public void blinkText(String str, int Tbox) {
	
    String Refr;
    Refr = "blinkText"+Tbox;
	
	Message msg = mhandler.obtainMessage();
	
	
	
    Bundle b = new Bundle();
    
    b.putString(Refr, ""+str);
    msg.setData(b);
    mhandler.sendMessage(msg);
  }


public void notifyMessageText(String str, int Tbox) {
		
        String Refr;
	    Refr = "refreshText"+Tbox;
    	
    	Message msg = mhandler.obtainMessage();
    	
    	
    	
        Bundle b = new Bundle();
        
        b.putString(Refr, ""+str);
        msg.setData(b);
        mhandler.sendMessage(msg);
      }

public void changeB(String str, int Tbox) {
	
    String Refr;
    Refr = "button"+Tbox;
	
	Message msg = mhandler.obtainMessage();
	
	
	
    Bundle b = new Bundle();
    
    b.putString(Refr, ""+str);
    msg.setData(b);
    mhandler.sendMessage(msg);
  }


public void changeC(String str, int Tbox) {
	 String Refr;
	    Refr = "changeColorText"+Tbox;
		
		Message msg = mhandler.obtainMessage();
		
		
		
	    Bundle b = new Bundle();
	    
	    b.putString(Refr, ""+str);
	    msg.setData(b);
	    mhandler.sendMessage(msg);
	
}


public void changeT(String str, int Tbox) {
	
    String Refr;
    Refr = "changeTextBack"+Tbox;
	
	Message msg = mhandler.obtainMessage();
	
	
	
    Bundle b = new Bundle();
    
    b.putString(Refr, ""+str);
    msg.setData(b);
    mhandler.sendMessage(msg);
  }

public void changeBackL() {
	
    String Refr;
    Refr = "cback";
	
	Message msg = mhandler.obtainMessage();
	
	
	
    Bundle b = new Bundle();
    
    b.putString(Refr, "");
    msg.setData(b);
    mhandler.sendMessage(msg);
  }


public void changePbar(int str, int Tbox) {
	
    String Refr;
    Refr = "pBar"+Tbox;
	
	Message msg = mhandler.obtainMessage();
	
	Bundle b = new Bundle();
    
    b.putString(Refr, ""+Integer.toString(str));
    msg.setData(b);
    mhandler.sendMessage(msg);
  }

public void clockStop(String string, int Tbox) {
	
    String Refr;
    Refr = "chronometer"+Tbox;
	
	Message msg = mhandler.obtainMessage();
	
	Bundle b = new Bundle();
    
    b.putString(Refr, ""+string);
    msg.setData(b);
    mhandler.sendMessage(msg);
  }


public void unvisiblePbar(int str, int Tbox) {
	
    String Refr;
    Refr = "pBarU"+Tbox;
	
	Message msg = mhandler.obtainMessage();
	
	Bundle b = new Bundle();
    
    b.putString(Refr, ""+Integer.toString(str));
    msg.setData(b);
    mhandler.sendMessage(msg);
  }

}

