package com.manoj.status;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

public class LoadContacts {

	public String getContactName(String number) {

	    String name = null;
	    int i=0;
	    Iterator<Contact> iterator = globals.temp_contacts.iterator();
		 while (iterator.hasNext()) {
			 if(globals.temp_contacts.get(i).getNumber().compareToIgnoreCase(number)==0)
			 {
				 name=globals.temp_contacts.get(i).getName();
				 globals.temp_contacts.get(i).setNumber("0");
				 break;
			 }
			 i++;
		 }
	    return name;
	}

	public String remove_spaces(String num)
	{
		String final_num="";
		for(int i=0;i<num.length();i++)
		{
			switch(num.charAt(i))
			{
			case '0':case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':
				final_num=final_num+num.charAt(i);
			}
		}
		if(final_num.length()>10)
			return final_num.substring(final_num.length()-10);
		else
			return final_num;
	}

	public void initiateContactsFetch(ContentResolver contentResolver) {
		ArrayList<String> alContacts = new ArrayList<String>();
		globals.temp_contacts = new ArrayList<Contact>();
		String contactNumber;
		String contactName;
		String num;

        Cursor cur = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                  String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                  contactName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                  if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                     Cursor pCur = contentResolver.query(
                               ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                               null,
                               ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                               new String[]{id}, null);
                     while (pCur.moveToNext()) {
                         contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
     					num = remove_spaces(contactNumber);
     					int flag=0;
     					for(int i=0;i<alContacts.size();i++)
     					{
     						if(alContacts.get(i).compareToIgnoreCase(num)==0)
     						{
     							flag=1;
     						}
     					}
     					if((flag==0)&&(num.length()>9))
     					{
     					alContacts.add(num);
     					globals.temp_contacts.add(new Contact(contactName,num));
     					}

                     }
                    pCur.close();
                }
            }
        }
        
        globals.db.droptables();
        Iterator<String> iterator = alContacts.iterator();
		 String json_body="{\"contacts\": [";
		 while (iterator.hasNext()) {
			 json_body=json_body+"{\"number\":\"" + iterator.next() + "\",\"statusupdated\":\"0\",\"photoupdated\":\"0\"},";
		 }
		String json_contacts = json_body.substring(0,json_body.length()-1)+"]}";
		//String number = globals.mPrefs.getString("mynumber", "0");
		
		String contacts;
		try{
		String URL = globals.serverIP+"getdetails";
		HttpPost pokers_post =new HttpPost(URL);
		BasicHttpResponse pokers_resp; 
		HttpClient client = new DefaultHttpClient();
		StringEntity entity = new StringEntity(json_contacts,"UTF-8");
		pokers_post.setEntity(entity);
		//pokers_post.setHeader("mobilenumber",number);
		//pokers_post.setHeader("Authorization", "Basic cG9rZXJhcHA6cHJpY2s=");
		pokers_resp = (BasicHttpResponse) client.execute(pokers_post);
		int resp_code = pokers_resp.getStatusLine().getStatusCode();
		if(resp_code==200)
		{
			contacts = EntityUtils.toString(pokers_resp.getEntity());
			JSONObject mainObject2 = new JSONObject(contacts);
			JSONArray contacts_list1 = mainObject2.getJSONArray("contacts");
			for(int i =0;i<contacts_list1.length();i++)
			{
			JSONObject contact = contacts_list1.getJSONObject(i);
			Contact cnt = new Contact(getContactName(contact.getString("number")),contact.getString("number"),contact.getString("email"),contact.getString("status"),contact.getString("statusupdated"),contact.getString("photoflag"));
			globals.db.addContact(cnt);
			
			}
		}
		globals.temp_contacts = globals.db.getAllContacts();
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}



}
