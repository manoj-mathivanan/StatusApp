package com.manoj.status;

import java.util.ArrayList;

import android.content.SharedPreferences;

public class globals {
public static SharedPreferences mPrefs;
public static SharedPreferences.Editor mEditor;
static DataBaseHandler db;
public static String serverIP="http://polar-lowlands-9405.herokuapp.com/";
public static ArrayList<Contact> temp_contacts;
}
