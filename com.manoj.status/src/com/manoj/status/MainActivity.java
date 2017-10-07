package com.manoj.status;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

public class MainActivity extends ActionBarActivity {

	private boolean api_key = false;
	private boolean call_key = false;
	Context context;
	ArrayList<Contact> final_contacts;
	String api_key_value;
	String call_key_value;
	
	TextView statusText;
	EditText userName;
	EditText userEmail;
	EditText userPhone;
	Button signUp;
	ProgressBar progressStatus;
	CounterClass timer;
	ProgressDialog progress;
	
	public void check_registered()
	{
		/*timer.cancel();
		//display loading screen
		progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Authentication Success.. Populating your contacts for first time...");
        progress.show();
		//load the contacts
        Contacts_task contacts_task2 = new Contacts_task();
        contacts_task2.execute(new String[] {  });
        
        */
        try{
		if(((api_key==true)&&(call_key==true)))
		{
			if(api_key_value.equalsIgnoreCase(call_key_value))
         	{
				timer.cancel();
		   		statusText.setText("Authentication Success..");
			onboard task = new onboard();
            task.execute(new String[] {});
         	}
	         else
	         {
	        	 timer.cancel();
	        	 statusText.setText("Authentication Unsuccessful. Check your phone number and please try again.");
	        	 progressStatus.setVisibility(4);
	        	 signUp.setEnabled(true);
	         }
		}
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
			
	}
	SharedPreferences mPrefs;
    final String welcomeScreenShownPref = "welcomeScreenShown";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        //bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#78A61F")));
        context = getApplicationContext();
        progress = new ProgressDialog(getApplicationContext());
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
            mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            globals.mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            globals.mEditor = mPrefs.edit();
            globals.db = new DataBaseHandler(context);
            
            Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);
           //welcomeScreenShown = false;
            if(welcomeScreenShown){
            	Intent goToNextActivity = new Intent(MainActivity.this,list.class);
    			startActivity(goToNextActivity);   
             }
        else
        {
        	
        statusText = (TextView)findViewById(R.id.textView4);
        userName = (EditText)findViewById(R.id.editText2);
        userEmail = (EditText)findViewById(R.id.EditText01);
        userPhone = (EditText)findViewById(R.id.editText1);
        signUp = (Button)findViewById(R.id.button1);
        progressStatus = (ProgressBar)findViewById(R.id.progressBar1);
        
        
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);        
        PhoneStateListener callStateListener = new PhoneStateListener() {
             public void onCallStateChanged(int state, String incomingNumber) 
             {
                     
                     
                     if(state==TelephonyManager.CALL_STATE_RINGING)
                     {
                    	call_key_value=incomingNumber.substring(incomingNumber.length()-4);;
                    	call_key = true;
                    	//telephonyService.endCall();
                    	check_registered();      
                     }
             }
        };
        telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);
        
        
        
        timer = new CounterClass(60000,1000); 
        signUp.setOnClickListener(new OnClickListener() { 
        	@Override public void onClick(View v) { 
        		statusText.setVisibility(0);
        		
        		if(userName.getText().toString().length()<=10)
        		{
        			statusText.setText("Please enter your mobile number with country code");
        		}
        		else
        		{
        			progressStatus.setVisibility(0);
            		progressStatus.setMax(60);
            		timer.start(); 
        		//Registration_task task = new Registration_task();
                //task.execute(new String[] { userName.getText().toString() });
            		onboard task = new onboard();
                    task.execute(new String[] {});
                signUp.setEnabled(false);
        		}
        		} 
        	}); 
        }
    }

    private class onboard extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... numbers) {
        	try{
        	
     
			String URL = globals.serverIP+"createuser";
			HttpPost reg_user =new HttpPost(URL);
			BasicHttpResponse reg_user_resp; 
			HttpClient client = new DefaultHttpClient();
			//String body = "{\"mobile_number\":\""+ userName.getText().toString().substring(userName.getText().toString().length()-10)+"\",\"name\":\""+userPhone.getText().toString()+"\",\"type\":\""+"android"+"\"}";
			//StringEntity entity = new StringEntity(body,"UTF-8");
			//reg_user.setEntity(entity);
			reg_user.setHeader("name", userPhone.getText().toString());
			reg_user.setHeader("number", userName.getText().toString());
			reg_user.setHeader("email", userEmail.getText().toString());
			Random r = new Random();
			Integer i1 = r.nextInt(99999 - 10000) + 10000;
			reg_user.setHeader("password", i1.toString());
			reg_user_resp = (BasicHttpResponse) client.execute(reg_user);
			int resp_code = reg_user_resp.getStatusLine().getStatusCode();
			if(resp_code==200)
			{
				return "Success";
			}
			else{
				return "failure";
			}
         	
        	}
        	catch(Exception e){
        		e.printStackTrace();
        		return "failure";
        	}
        }

        @Override
        protected void onPostExecute(String result) {
        	if(result.compareToIgnoreCase("Success")==0)
        	{
        	timer.cancel();
			//display loading screen
			
			progress.setTitle("Loading");
			progress.setMessage("Authentication Success.. Populating your contacts...");
			statusText.setText("Registration Success.. Populating your contacts...");
			//progress.show();
			SharedPreferences.Editor editor = mPrefs.edit();
			globals.mEditor = mPrefs.edit();
			editor.putBoolean(welcomeScreenShownPref, true);
			editor.putInt("pokes", 0);
			//editor.putInt("total_pokes", 0);
			editor.putString("mynumber", userName.getText().toString().substring(userName.getText().toString().length()-10));
			editor.commit();
			//load the contacts
			Contacts_task contacts_task = new Contacts_task();
			contacts_task.execute(new String[] {  });
        	}
        	else{
        		timer.cancel();
           	 statusText.setText("Authentication Success but Registration Failed. Poke server couldnt be reached.");
           	 progressStatus.setVisibility(4);
           	 signUp.setEnabled(true);
        	}
        }
      }
    
    private class Registration_task extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... numbers) {
          String number = numbers[0].toString();
          Registration reg = new Registration();
          return reg.register_number(number);
        }

        @Override
        protected void onPostExecute(String result) {
        	api_key_value = result.substring(result.length()-4);
        	api_key=true;
        	check_registered();
        }
      }
    

    private class Contacts_task extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... numbers) {
          LoadContacts contacts = new LoadContacts();
          contacts.initiateContactsFetch(context.getContentResolver());   	
          return "Success";
        }

        @Override
        protected void onPostExecute(String result) {
        	progress.dismiss();
        	Intent goToNextActivity = new Intent(MainActivity.this,list.class);
			startActivity(goToNextActivity);
        }
      }
      
    public class CounterClass extends CountDownTimer { 
    	public CounterClass(long millisInFuture, long countDownInterval) { 
    		super(millisInFuture, countDownInterval); 
    		} 
    	@Override public void onFinish() { 
    		check_registered();
    		statusText.setText("Authentication Unsuccessful. Check your phone number, network connectivity and please try again."); 
    		signUp.setEnabled(true);
    		progressStatus.setVisibility(4);
    		} 

    	@SuppressLint({ "NewApi", "DefaultLocale" }) @Override public void onTick(long millisUntilFinished) {
    		String hms = Long.toString(millisUntilFinished/1000);
    		int pr = Integer.parseInt(hms);
    		statusText.setText(pr + " seconds remaining"); 
    		progressStatus.setProgress(pr);
    		} 
    	} 


    

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
